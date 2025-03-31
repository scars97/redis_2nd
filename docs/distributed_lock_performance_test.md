# 분산락 성능 테스트

## 목차
* [분산락을 적용하는 이유](#분산락을-적용하는-이유)
  * [DB 락 기반 동시성 제어의 한계](#db-락-기반-동시성-제어의-한계)
  * [분산락 적용 기대 효과](#분산락-적용-기대-효과)
* [성능 테스트 진행](#성능-테스트-진행)
  * [락 임대, 대기 시간 설정 기준](#락-임대-대기-시간-설정-기준)
  * [AOP 분산락](#aop-분산락)
  * [함수형 분산락](#함수형-분산락-적용)
* [결과](#결과)
  * [함수형 분산락 안정성 문제](#함수형-분산락-안정성-문제)
* [정리](#정리)

## 분산락을 적용하는 이유
    
앞서 낙관적, 비관적 락을 적용함으로써 예약 기능에 대한 동시 요청을 제어했지만, 다음과 같은 문제가 예상된다.

### DB 락 기반 동시성 제어의 한계
- **트래픽 증가 시 DB 부하 증가** 
  - 동시 요청이 많을 수록 DB 락 경합 ↑ 
  - 쿼리 지연과 데드락 가능성이 커지면서 성능 저하 발생
- **DB 스케일 아웃의 한계**
  - 샤딩, 레플리케이션은 비용이 많이 듦. 

이런 문제들로 애플리케이션 레벨에서 분산락을 관리하는 방식이 더 적합하다고 판단된다.

### 분산락 적용 기대 효과
- **비용 효율성**
  - DB 를 증설하는 것보다 캐시 시스템을 활용하는 것이 비용 측면에서 유리 
- **DB 트랜잭션 최적화** 
  - 동시 요청이 많아도 락을 획득한 요청만 트랜잭션 수행 → DB 부하 감소 
  - 트랜잭션 충돌과 재시도 요청이 줄어들어 처리 속도 향상

## 성능 테스트 진행
Redisson 라이브러리를 사용하여 AOP, 함수형 분산락을 구현하고, k6 성능 테스트를 진행한다.
→ 테스트 스크립트 5번 실행 후 평균 시간 계산 
    
- **가상 사용자** : 100명
- **요청 방식** : 각 사용자마다 1회씩 요청하며, 100명이 동시 요청.
- **확인 요소** :
    - http_req_blocked (대기 시간) → avg(평균), p(95)
    - http_req_duration (처리 시간) → avg(평균), p(95)
- **락 임대, 대기 시간 설정**
  - waitTime : 3초
  - leaseTime: 2초

### 락 임대, 대기 시간 설정 기준
**(1) leaseTime (락 임대 시간) 설정 기준**
- 락이 유지되는 동안 예매 로직이 정상적으로 끝나야 하기 때문에, 예매 처리 시간을 기준으로 설정.
- 예약 로직 5번 호출 평균 실행 시간 : 약 600ms
- 처리 시간의 2~3배 정도인 1 ~ 2초가 적절하다 생각된다.<br>너무 짧으면 처리 도중 락이 풀려버리고, 너무 길다면 다른 사용자의 예매가 지연될 수 있다.

**(2) waitTime (락 대기 시간) 설정 기준**
- waitTime을 길게 설정하면 leaseTime보다 로직이 빨리 처리되었을 때 사용자가 오래 기다릴 수 있다.<br>
반대로 너무 짧게 잡으면 락을 잡지 못하고 실패할 가능성이 높아진다.
- 얼마나 경쟁되느냐에 따라 달라질 수 있을 것 같다.
  - 경쟁이 심한 경우 (인기 영화, 인기 좌석) :
    - N 명이 동시 요청하는 경우가 많으므로 waitTime을 짧게 주어 사용자 대기 시간을 줄인다.
  - 경쟁이 적은 경우 (일반 영화, 낮은 예매율) :
    - 동시 요청은 거의 없지만 락을 반드시 얻어야 하는 경우 waitTime을 조금 더 길게 설정하는 것도 방법이다.

현 서비스는 인기 영화 상영으로 인해 **동시 요청이 많을 것으로 가정**한다.

경쟁이 심한 상황이기 때문에 대기 시간을 타이트하게 가져가야 한다.<br>
앞서 설정한 leaseTime보다 더 긴 3초가 적절하다 생각된다.
                
            
### k6 테스트 스크립트
```js
import http from 'k6/http';
import { check, sleep } from 'k6';

export const options = {
    scenarios: {
        gradual_uinque_users: {
            executor: 'per-vu-iterations',
            vus: 10,            // 10명의 가상 사용자
            iterations: 1,        // 각 사용자 1회 요청
            maxDuration: '5m',    // 최대 실행 시간
            startTime: '0s',
        },
    }
};

const BASE_URL = 'http://host.docker.internal:8080';

export default function () {
    const userId = __VU;
    const scheduleId = 1;
    const seatIds = [1, 2, 3]

    let res = http.post(`${BASE_URL}/api/reservations`,
        JSON.stringify({
            userId: userId,
            scheduleId: scheduleId,
            seatIds: seatIds
        }),
        { headers: { 'Content-Type': 'application/json' } }
    );

    check(res, {
        'reservation success': (r) => r.status === 201,
        'reservation conflict': (r) => r.status === 409,
        'response time < 200ms': (r) => r.timings.duration < 200,
    });

    sleep(1);
}
```
</details>
    

### AOP 분산락
    
```kotlin
@DistributedLock(key = "'reserve-' + #info.scheduleId")
@Transactional
fun createReservation(info: ReservationInfo): ReservationResult {
    validator.validate(info)

    val seats = seatService.getSeats(info.seatIds)

    Reservation.checkExceedLimit(info.seatIds.size)

    Reservation.checkContinuousSeats(seats)

    val reservation = reservationService.createReservation(Reservation.of(info.userId))

    seatService.updateForReserve(info.seatIds, reservation.reservationId)

    // App push 이벤트 발행
    eventPublisher.publishEvent(ReservationMessageEvent.of(reservation.reservationId))

    return ReservationResult.of(reservation, info.seatIds)
}
``` 
- **http_req_blocked** : 평균 102 ms 
  - **p(95)** : 평균 132 ms 
- **http_req_duration**: 평균 2.3 s 
  - **p(95)** : 평균 3 ms

### 함수형 분산락

```kotlin
fun createReservation(info: ReservationInfo): ReservationResult {
  validator.validate(info)

  return lockExecutor.lockWithTransaction("reserve-${info.scheduleId}", 5L, 2L) {
      val seats = seatService.getSeats(info.seatIds)

      Reservation.checkExceedLimit(info.seatIds.size)

      Reservation.checkContinuousSeats(seats)

      val reservation = reservationService.createReservation(Reservation.of(info.userId))

      seatService.updateForReserve(info.seatIds, reservation.reservationId)

      // App push 이벤트 발행
      eventPublisher.publishEvent(ReservationMessageEvent.of(reservation.reservationId))

      return@lockWithTransaction ReservationResult.of(reservation, info.seatIds)
  }
}
```
- **http_req_blocked**: 평균 93 ms 
  - **p(95)** : 평균 121 ms 
- **http_req_duration**: 평균 1.6 s 
  - **p(95)** : 평균 2.2 ms

## 결과

| 측정 항목 | AOP 분산락 적용 | 함수형 분산락 적용 | 차이점 |
| --- | --- | --- | --- |
| **Blocked (평균)** | 102 ms | 93 ms | 함수형이 약간 빠름 (-9 ms) |
| **Blocked (p95)** | 132 ms | 121 ms | 함수형이 약간 빠름 (-11 ms) |
| **Duration (평균)** | 2.3 s | 1.6 s | 함수형이 약 30% 더 빠름 (-0.7 s) |
| **Duration (p95)** | 3 ms | 2.2 ms | 함수형이 더 빠름 (-0.8 ms) |

전체적으로 함수형분산락 방식을 사용했을 때 성능 이점이 있었다.

AOP 분산락은 메서드 전체에 적용되기 때문에 락 범위가 넓다.

함수형 분산락의 경우, 특정 시점에 적용할 수 있어 락 범위를 좁힐 수 있기 때문에 성능 면에서 더 좋은 결과가 나왔다.

### 함수형 분산락 안정성 문제

함수형 분산락이 성능 면에서는 더 우수하지만, 안전하다고 말할 수는 없다.

트랜잭션과 락의 적용 시점에 따라 동시성 문제가 발생할 수 있는데,

**AOP 분산락**은 `@Order(Ordered.HIGHEST_PRECEDENCE)` 로 트랜잭션을 시작하기 전,<br> 
락을 먼저 획득할 수 있게 하여 **트랜잭션 시작 시점과 락 획득 순서를 명확히 관리**할 수 있다. 

또한, **락 기능과 트랜잭션을 분리해서 사용 가능**하다. → 이 방식이 더 명시적이고 유연성이 높다고 생각된다.
```kotlin
@DistributedLock(key = "'reserve-' + #info.scheduleId")
@Transactional
```
반면, 함수형 분산락은 기존에 트랜잭션이 적용되어야할 메서드 안에서 구현되기 때문에 트랜잭션이 시작되고 락을 획득하게 된다.

이를 방지하기 위해 락 획득 시, 트랜잭션이 시작되도록 propagtion을 따로 설정해줘야 한다.
```kotlin
@Component
class FunctionalForTransaction {

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    fun <T> proceed(action: () -> T): T {
        return action()
    }

}

// 분산락 구현 클래스
class DistributedLockExecutor(
    private val functionalForTransaction: FunctionalForTransaction
) {
    fun <T> lockWithTransaction(
        key: String, waitTime: Long, leaseTime: Long, action: () -> T): T {
        
        // ...
        
        return try {
            if (!rLock.tryLock(waitTime, leaseTime, TimeUnit.SECONDS)) {
                throw RuntimeException("락 획득 실패 : $key")
            }
        
            functionalForTransaction.proceed(action)
        } catch() {
            // ...
        } finally {
            // ...
        }
    }
}
```
락 기능 안에 트랜잭션이 포함될 수밖에 없는 구조가 된다.

이런 경우, **락을 잡은 후 트랜잭션이 시작되었는지 100% 확실하지 않다.** 

## 정리
**락은 동시성을 제어하고, 트랜잭션은 원자성을 보장한다.**

이 둘을 결합했을 때 문제는 다음과 같다.
- 책임이 불명확해지고 유지보수가 어렵다.
- 락이 필요한 경우와 트랜잭션이 필요한 경우가 다를 수 있는데, 이 둘을 결합하면 **불필요한 트랜잭션이 실행될 가능성** 있음.
- **트랜잭션이 오래 걸릴 경우 락이 먼저 해제될 위험.**

함수형 분산락이 어느정도 성능 이점이 있다는 것은 테스트로 확인했다.

하지만 락을 적용하는 가장 중요한 이유는 **안정성**이라 생각된다.

애플리케이션에서 발생하는 문제는 개발자가 예측하고 제어할 수 있어야 하는데,<br>
락과 트랜잭션이 결합되면 동작을 예측하기 어렵고, 예기치 않은 사이드 이펙트가 발생할 가능성이 커진다.

이러한 리스크를 줄이기 위해 **AOP 분산락을 적용하는 것이 더 적절하다고 판단**된다.