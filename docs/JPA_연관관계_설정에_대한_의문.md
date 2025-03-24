## 배경
**상영 중인 영화 목록 조회 시, 불필요한 JOIN이 발생하는 문제**

- JPA 연관관계를 설정한 상태에서 상영 중인 영화 목록을 조회할 때, 불필요한 JOIN이 수행되는 문제 발생.
- JPA의 연관 관계 설정으로 인한 문제로 판단
- **연관 관계를 설정한 경우와 설정하지 않은 경우의 차이점 분석.**

이 문서에서는 두 방식의 차이를 비교하고, 적용한 해결 방법을 정리한다.

## 특이사항
현 프로젝트에서는 **필요한 데이터를 각각 조회하여 UseCase에서 조합.**

**💠 테이블마다 조회하는 이유**
- 캐시를 적용할 수있는 선택폭이 넓어짐.
- 쿼리가 특정 기능에 종속되지 않아 재사용 가능.
- join을 사용하여 하나의 쿼리로 조회하는 경우, 데이터가 많을 수록 join 비용 ↑

## 문제 상황

영화 Id에 대한 상영일정을 조회할 때, jpa 연관관계 설정으로 인해 join이 수행되고 있음.

```kotlin
@Entity
class TheaterScheduleEntity (
    // 필드 생략 ...

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "movie_id", nullable = false, foreignKey = ForeignKey(ConstraintMode.NO_CONSTRAINT))
    val movie: MovieEntity,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "theater_id", nullable = false, foreignKey = ForeignKey(ConstraintMode.NO_CONSTRAINT))
    val theater: TheaterEntity
)
```

```sql
select
    tse.*
from
    theater_schedule_entity tse
     left join movie_entity m on m.movie_id = tse.movie_id
where
    m.movie_id in (?)
order by
    tse.start_time;
```

이미 영화 데이터를 조회한 상태에서 상영일정 데이터를 조회하기 때문에
영화 테이블에 대한 join은 수행하지 않아도 된다고 판단.

## 해결 방법

### **JPQL을 사용하여 상영일정 테이블만 조회할 수 있도록 제어.**

```sql
select 
    * 
from 
    theater_schedule_entity tse 
where 
    tse.movie_id in (?)
```

불필요한 join을 제거했지만 한가지 문제가 있다.

현재 프로젝트에서는 엔티티와 도메인 모델을 분리해서 관리하고 있는데,

엔티티 조회 후, 도메인 모델로 변환해주기 위해 movie와 theater에 대한 값이 필요하다.

**이때, N + 1 문제 발생한다.**

```kotlin
class TheaterScheduleMapper {
    companion object {
        fun toSchedule(entity: TheaterScheduleEntity): TheaterSchedule {
            return TheaterSchedule(
                scheduleId = entity.id,
                movieId = entity.movie.id, // MovieEntity 사용
                theaterId = entity.theater.id, // TheaterEntity 사용
                screeningDate = entity.screeningDate,
                startTime = entity.startTime,
                endTime = entity.endTime
            )
        }
    }
}
```

```sql
-- 영화 조회
select * from movie_entity 
where release_date <= ? order by me1_0.release_date

-- 상영일정 조회
select * from theater_schedule_entity 
where movie_id in (?) ORDER BY start_time

-- N + 1
select * from theater_entity where theater_id=?
select * from theater_entity where theater_id=?
...

-- 상영관 조회
select * from theater_entity 
where te1_0.theater_id in (?)
```

의아한 것은 movie와 theater가 아닌 theater에 대해서만 발생한 것이다.

**💠 movie 에 대한 추가 쿼리가 발생하지 않은 이유.**

이미 `movie` 데이터를 조회한 상태에서 `theater_schedule`을 조회하면,
JPA가 기존 영속성 컨텍스트에서 해당 `movie`엔티티를 관리하고 있기 때문에
프록시 객체를 새로 생성하지 않고, 추가 쿼리도 발생하지 않음.

프록시 객체를 통해 참조할 수 있는 필드는 id 값이므로, `entity.movie.id`를 조회할 때는 SELECT 쿼리가 실행되지 않는다.

하지만 `entity.movie.title` 같은 필드를 조회하면 **Lazy Loading이 동작**하면서 추가 쿼리가 발생.

> **💡 프록시 객체란?** <br>
> 
> JPA는 `@ManyToOne(fetch = FetchType.LAZY)`설정이 되어 있을 경우,<br>
실제 엔티티 대신 프록시 객체(대리 객체)를 반환하는데,<br>
이 객체는 **id 값만 가지고 있고 나머지는 데이터베이스에서 가져올 때까지 비워둔다.**<br>
`movie.title`같은 속성을 조회하려고 하면 실제 데이터를 가져오기 위해 SELECT 쿼리를 실행.

N + 1 문제를 해결하기 위해 Fetch Join, @EntityGraph 등을 사용할 수 있지만, 이 방법들은 결국 join을 사용한다는 것이다.

**불필요한 join을 제거하려 했지만 N + 1 문제로 다시 join을 사용해야 하는 상황**이다.

### **JPA 연관관계 설정 제거.**

단순하게 JPA 연관관계 설정을 제거하면 어떻게 될까.

`TheatScheduleEntity`는 `MovieEntity`와 `TheaterEntity` 를 의존하는 게 아닌, id 값만 설정하는 것이다.

```kotlin
@Entity
class TheaterScheduleEntity (
    // 필드 생략 ...

    val movieId: Long,

    val theaterId: Long
)
```

```kotlin
class TheaterScheduleMapper {
    companion object {
        fun toSchedule(entity: TheaterScheduleEntity): TheaterSchedule {
            return TheaterSchedule(
                ...,
                movieId = entity.movieId,
                theaterId = entity.theaterId,
                ...
            )
        }
    }
}
```

**실행된 쿼리**

```sql
-- 영화 조회
select * from movie_entity 
where release_date <= ? order by me1_0.release_date

-- 상영 일정 조회
select * from theater_schedule_entity 
where movie_id in (?) order by start_time

-- 상영관 조회
select * from theater_entity 
where te1_0.theater_id in (?)
```

불필요한 join 없이 각 테이블만 조회하게 되었다.


## 정리
**JPA 연관관계를 제거하는 방법을 사용**했다.

불필요한 join이 수행되는 것은 해결했지만 여전히 문제점은 있다.  

**💠 JPA 연관관계 설정을 제거했을 때의 문제점**

- 객체지향적인 설계가 깨짐 → 데이터 중심적인 개발 방식이 됨
- 한 번에 조회가 불가능 → 여러 개의 쿼리를 직접 실행해야 함
- JPA가 제공하는 자동 기능(Cascade, 삭제, 업데이트 등)을 사용할 수 없음

꽤나 많은 것들을 포기해야 한다.<br>
그럼에도 이 방법을 선택한 이유는 **의존성을 줄이기 위함**이다.

상영일정은 영화, 상영관과 연결되어 있다.<br>
만약 도메인 별로 서비스가 나뉜다면 어떻게 될까. 상영일정은 영화와 상영관에 의존하고 있기 때문에 그 의존성을 모두 풀어내야 한다.

무엇보다 DB 쿼리는 개발자가 제어할 수 있어야 한다고 생각한다.<br>
연관관계로 설정으로 인해 발생하는 사이드이펙트(불필요한  join, N+1)를 해결하는 비용도 무시할 수 없다고 생각한다.