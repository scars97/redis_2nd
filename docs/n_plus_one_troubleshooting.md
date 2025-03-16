## N + 1 문제

### 배경
- 상영 중인 영화들의 상영 일정 목록을 조회할 때 N + 1 문제 발생.
- 상영 일정 테이블은 영화와 상영관 테이블의 N:N 관계를 풀어내기 위한 중간 테이블임.

### 코드 & 실행 쿼리

```kotlin
@Repository
class TheaterScheduleCoreRepositoryImpl(
    private val jpaRepository: TheaterScheduleJpaRepository
): TheaterScheduleRepository {
    override fun getScheduleByMovieIds(movieIds: List<Long>): List<TheaterSchedule> {
        val schedules = jpaRepository.findByMovieIdIn(movieIds)

        // N + 1 문제 발생 지점
        return schedules.stream()
            .map { TheaterMapper.toSchedule(it) }
            .toList()
    }
}

interface TheaterScheduleJpaRepository: JpaRepository<TheaterScheduleEntity, Long> {
    fun findByMovieIdIn(movieIds: List<Long>): List<TheaterScheduleEntity>
}
```
```sql
select tse1_0.*
from theater_schedule_entity tse1_0 
    left join movie_entity m1_0 on m1_0.movie_id = tse1_0.movie_id 
where m1_0.movie_id in (?)

select te1_0.* from theater_entity te1_0 where te1_0.theater_id=?
select te1_0.* from theater_entity te1_0 where te1_0.theater_id=?
```

### 원인

예상했던 쿼리는 **`select * from theater_schedule_entity where movie_id in (?)`** 이지만 <br>실제로는 movie_entity를 join 하고 theater_entity를 조회하고 있다.

> **movie_entity를 join 하는 이유**

물리적 FK만 맺지 않았을 뿐, @ManyToOne이 설정되어 있어 jpa 기능은 사용한다.<br>
jpa는 @ManyToOne 관계가 설정되어 TheaterScheduleEntity와 MovieEntity가 관련 있다고 판단하여 `where movie_id in (?)` 을 수행할 때, <br>관련된 movie_entity 데이터를 가져오기 위해 자동으로 Join을 수행할 수 있음.
결국, 물리적 FK가 없어도 JPA 매핑 정보에 따라 Join이 수행됨.

해결 방법은 **Naive Query 를 사용해서 join을 방지**하거나,
**@ManyToOne 연관관계를 끊고 ID 값을 직접 사용**하는 방법이 있다.

> **theater_entity가 조회된 이유**

theater 또한 @ManyToOne와 , FetchType.LAZY 가 설정되어 있다.

이런 경우 theater의 필드 값에 접근하면, Hibernate는 실제 데이터를 로드하기 위해 추가 쿼리를 실행한다.
코드에서는 TheaterScheduleEntity 목록을 조회하고 Mapper를 통해 도메인 모델로 변환할 때, theater에 접근한다.
조회되는 theater가 많을 수록 N + 1 문제가 발생한다.

해결 방법은 **직접 쿼리를 작성하여 fetch join으로  theater까지 조회**하거나,
**@EntityGraph를 사용하여 theater 를 함께 조회**할 수 있도록 하는 방법이 있다.

### 해결
- 상영 일정을 조회하기 위해 영화 ID는 필수 조건으로 들어가야 한다. 
- movie_entity가 join되는 것은 문제가 아니라 판단했고, 
- theater_entity의 N + 1 문제를 해결하기 위해 `findByMovieIdIn`에 @EntityGraph를 사용하여 theater_entity 까지 join하도록 했다.

```kotlin
interface TheaterScheduleJpaRepository: JpaRepository<TheaterScheduleEntity, Long> {

    // TheaterScheduleEntity의 theater_entity 필드명
    @EntityGraph(attributePaths = ["theater"]) 
    fun findByMovieIdIn(movieIds: List<Long>): List<TheaterScheduleEntity>

}
```
```sql
select 
    tse1_0.*, 
    t1_0.*
from theater_schedule_entity tse1_0 
    left join movie_entity m1_0 on m1_0.movie_id = tse1_0.movie_id 
    join theater_entity t1_0 on t1_0.theater_id = tse1_0.theater_id 
where m1_0.movie_id in (?)
```