# Week 10

## 주제

10주차는 **DB 연동**을 배우는 주차다.

지금까지(2~7주차)는 컨트롤러, 빈, DI, 라이프사이클, AOP 같은 스프링 컨테이너 안쪽 개념을 다뤘다면,
10주차는 컨테이너 밖에 있는 **데이터베이스**와 스프링이 어떻게 연결되는지를 본다.

핵심 파일:

- `Member`
- `MemberRowMapper`
- `MemberRepository`
- `MemberService`
- `DBServiceController`
- `application.properties` (DataSource 자동 설정)
- `src/main/resources/sql/week10-schema.sql`
- `src/main/resources/sql/week10-data.sql`

## 이 주차가 왜 중요한가

순수 JDBC API로 DB를 쓰면 `Connection`, `Statement`, `ResultSet`을 매번 열고 닫고,
예외 처리까지 직접 작성해야 한다.
핵심 SQL 한 줄을 실행하기 위해 반복 코드가 너무 많다.

스프링 JDBC(`JdbcTemplate`)는 이 반복을 제거한다.

즉 10주차의 역할은:

- 순수 JDBC의 반복 구조를 이해하고
- `DataSource`가 어떻게 자동 등록되는지 확인하고
- `JdbcTemplate`의 `query`, `queryForList`, `queryForObject`, `update`, `batchUpdate`를 구분하고
- `RowMapper`, `PreparedStatementCreator`, `BatchPreparedStatementSetter` 함수형 인터페이스를 람다식으로 작성하고
- 자동 생성 키(PK)를 `KeyHolder`로 가져오고
- `@Transactional`로 SQL 오류 시 롤백되는 것을 확인하는 것

이다.

## 선수 개념

- [용어 사전](../glossary.md)
- [공통 기초 문서](../foundation.md)
- [프로젝트 구조 해설](../project-structure.md)
- [4주차](../week04/README.md)의 DI 개념
- [5주차](../week05/README.md)의 빈 객체 관리 개념
- [7주차](../week07/README.md)의 AOP 개념 (`@Transactional`은 AOP 기반)

## 문서 구성

- [이론 문서](theory.md)
- [실습 문서](practice.md)

## 이 주차에서 꼭 잡아야 하는 질문

- 순수 JDBC API의 단점은 무엇인가?
- `JdbcTemplate`을 쓰면 무엇이 줄어드는가?
- `DataSource`는 어떻게 자동 등록되는가?
- `application.properties`의 `spring.datasource.*` 속성은 무엇을 의미하는가?
- DTO, Service, Repository, Domain은 각각 어떤 책임을 가지는가?
- `RowMapper`는 무엇을 변환하는가?
- `query()`, `queryForList()`, `queryForObject()`, `update()`, `batchUpdate()`는 어떻게 다른가?
- 함수형 인터페이스와 람다식을 왜 같이 배워야 하는가?
- `PreparedStatementCreator`와 `BatchPreparedStatementSetter`는 언제 쓰는가?
- `KeyHolder`로 자동 생성된 PK를 가져오는 방법은?
- `@Transactional`은 언제 롤백하는가? `rollbackFor`는 무엇을 추가하는가?

## 추천 읽기 순서

1. `theory.md`에서 JDBC의 반복 구조와 `JdbcTemplate`이 무엇을 줄여주는지 확인
2. `application.properties`의 `spring.datasource.*` 설정과 H2 콘솔 진입
3. `Member`(Entity) → `MemberRowMapper` 순서로 매핑 방식 읽기
4. `MemberRepository`에서 `query`, `update`, `batchUpdate`, `KeyHolder`, `@Transactional`을 한 줄씩 확인
5. `MemberService`에서 어떤 SQL을 어떤 파라미터로 호출하는지 읽기
6. `DBServiceController`에서 `/week10/...` 라우트와 JSP 화면 연결 확인
7. `practice.md`에서 각 라우트의 실행 결과 확인

## 빠른 요약

- 10주차는 JDBC 반복 코드를 제거하는 `JdbcTemplate` 사용법을 배우는 주차다.
- `DataSource`는 `application.properties`로 자동 등록되며, 학습용으로 H2 임베디드 DB를 MySQL 호환 모드로 사용한다.
- `RowMapper`, `PreparedStatementCreator`, `BatchPreparedStatementSetter`는 함수형 인터페이스이며 람다식으로 작성한다.
- DTO/Domain은 데이터 전송과 매핑, Service는 핵심 로직, Repository는 DB 입출력을 담당한다.
- `KeyHolder`는 INSERT 시 자동 생성된 PK를 받는다.
- `@Transactional`은 SQL 오류 발생 시 자동 롤백을 시켜준다.
- `@Transactional`은 내부적으로 스프링 AOP 프록시 기반으로 동작한다 → 7주차와 연결된다.

## 이전 주차 연결

- [7주차 AOP 문서](../week07/README.md)
  - `@Transactional`은 AOP 프록시가 트랜잭션 시작/커밋/롤백을 자동 적용하는 예시다.
