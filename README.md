## 목차

* [Architecture](#Architecture)
* [멀티 모듈 설계](#멀티-모듈-설계)
* [ERD](#erd)
* [시퀀스 다이어그램](#시퀀스-다이어그램)

## Architecture
- **Clean + Layered**
- 기본적인 계층 구조를 유지하면서 비즈니스 중심 구조로 설계.
- Repository 인터페이스를 비즈니스 계층에 정의하고, infra 계층에서 이를 구현하여 DIP 적용.

## 멀티 모듈 설계

### movie-api
- 클라이언트와의 인터페이스를 담당.
- 클라이언트 요청을 moive-application 모듈로 전달.

### movie-application
- 유즈케이스 역할 수행.
- 여러 도메인 서비스를 조합하여 비즈니스 처리.

### movie-business
- 비즈니스 규칙을 정의하고, 도메인 로직 수행.
- movie-application에 필요한 서비스를 제공.

### movie-infrastructure
- 데이터 저장소 및 외부 API 연동.
- Redis, Kafka 등 인프라 기능 제공.

## ERD
- 작성자,수정자,작성일,수정일 컬럼은 실제 테이블에 추가됩니다. <br> 가독성을 위해 ERD 상에서만 제외했습니다.
- 테이블 간의 느슨한 결합을 위해 논리적 FK만 맺도록 했습니다. 

![cinema_erd](docs/erd.png)

### Movie(영화) & Theater(상영관) → N : N 
- Theater-Schedule 을 두어 1:N 으로 풀어냄.

### Theater-Schedule(상영 일정) & Seat(좌석) →  1 : N
- 상영일정마다 좌석 상태 값이 다를 수 있음.

### Seat(좌석) & Reseravation(예약) → 1 : 1
- 좌석은 해당 **상영 일정에서 단 한 번만 예약될 수 있음.** 
  - 한 좌석을 두 명이 동시에 예약할 수 없음.
  - 상영 일정이 지나면 해당 좌석은 예약할 수 없어야 함.
- **상영 일정(ID)마다 좌석이 개별적으로 관리됨.**
  - 같은 장소, 같은 좌석 번호를 갖고 있더라도 **상영 일정이 다르면 다른 좌석으로 취급**됨.

### User(사용자) & Reservation(예약) → 1 : N
- 한 좌석에 대한 예약 내역은 반드시 하나지만, 한 명의 사용자가 여러 좌석을 예약할 수 있음.

## 시퀀스 다이어그램

<details>
<summary><b>영화 목록 조회</b></summary>

![view_movie_list](docs/view_movie_list.png)

</details>