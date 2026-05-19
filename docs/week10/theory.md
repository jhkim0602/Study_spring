# Week 10 Theory

## 주제

10주차는 **DB 연동**을 다룬다.

지금까지 컨테이너 안쪽(빈, DI, scope, AOP)을 봤다면,
10주차에서는 그 빈들이 실제 데이터베이스와 어떻게 연결되는지를 본다.

## 이 문서를 읽기 전에

10주차를 이해하려면 다음 흐름을 기억해야 한다.

- 3주차: 빈은 스프링이 관리하는 객체다
- 4주차: 컨트롤러는 필요한 빈을 DI로 주입받는다
- 5~6주차: 빈은 scope, lifecycle 같은 운영 정책을 가진다
- 7주차: 메서드 실행 전후에 공통 기능(AOP)을 적용할 수 있다

10주차는 이 흐름 위에서 다음 질문으로 넘어간다.

> "스프링 빈에서 데이터베이스에 SQL을 실행하려면 어떻게 해야 하는가?"
> "JDBC API의 반복 코드를 어떻게 제거하는가?"
> "INSERT/UPDATE 도중 오류가 나면 어떻게 되돌리는가?"

## 현재 프로젝트에서 먼저 볼 코드

- [`Member.java`](../../src/main/java/Lect_B/week10/Member.java)
- [`MemberRowMapper.java`](../../src/main/java/Lect_B/week10/MemberRowMapper.java)
- [`MemberRepository.java`](../../src/main/java/Lect_B/week10/MemberRepository.java)
- [`MemberService.java`](../../src/main/java/Lect_B/week10/MemberService.java)
- [`DBServiceController.java`](../../src/main/java/Lect_B/week10/DBServiceController.java)
- [`Week10IndexController.java`](../../src/main/java/Lect_B/week10/Week10IndexController.java)
- [`application.properties`](../../src/main/resources/application.properties)
- [`week10-schema.sql`](../../src/main/resources/sql/week10-schema.sql)
- [`week10-data.sql`](../../src/main/resources/sql/week10-data.sql)
- [`src/main/webapp/views/week10`](../../src/main/webapp/views/week10)

## 목차

- [1. 10주차의 핵심 질문](#1-10주차의-핵심-질문)
- [2. JDBC API의 반복 구조](#2-jdbc-api의-반복-구조)
- [3. 스프링 JDBC가 무엇을 줄여 주는가](#3-스프링-jdbc가-무엇을-줄여-주는가)
- [4. DataSource 자동 설정](#4-datasource-자동-설정)
- [5. DB 관련 객체들의 역할](#5-db-관련-객체들의-역할)
- [6. JdbcTemplate 만들기](#6-jdbctemplate-만들기)
- [7. 함수형 인터페이스와 람다식](#7-함수형-인터페이스와-람다식)
- [8. SELECT 쿼리 실행](#8-select-쿼리-실행)
- [9. INSERT/UPDATE/DELETE 쿼리 실행](#9-insertupdatedelete-쿼리-실행)
- [10. PreparedStatement와 일괄 처리](#10-preparedstatement와-일괄-처리)
- [11. 자동 생성 키(KeyHolder)](#11-자동-생성-키keyholder)
- [12. @Transactional과 롤백](#12-transactional과-롤백)
- [13. 현재 프로젝트에서 구현한 실습 구조](#13-현재-프로젝트에서-구현한-실습-구조)
- [14. 자주 헷갈리는 질문](#14-자주-헷갈리는-질문)
- [15. 시험 대비 핵심 정리](#15-시험-대비-핵심-정리)

## 1. 10주차의 핵심 질문

10주차를 한 문장으로 요약하면:

> "JDBC 반복 코드를 제거하고, 스프링이 관리하는 DataSource와 JdbcTemplate으로 SQL을 안전하게 실행한다"

이다.

서비스 메서드는 원래 업무 로직에만 집중해야 한다.
그런데 순수 JDBC를 쓰면 Connection을 열고, Statement를 만들고, ResultSet을 읽고, 예외를 처리하고, 자원을 닫는 코드가 매번 반복된다.

스프링은 이 반복을 `JdbcTemplate` 한 줄로 줄인다.

## 2. JDBC API의 반복 구조

순수 JDBC 코드는 다음 흐름을 가진다.

```java
Connection conn = null;
Statement stmt = null;
ResultSet rs = null;
try {
    Class.forName("com.mysql.cj.jdbc.Driver");
    conn = DriverManager.getConnection(url, user, pw);
    stmt = conn.createStatement();
    rs = stmt.executeQuery("select * from MEMBER");
    while (rs.next()) {
        // 한 행을 자바 객체로 변환
    }
} catch (SQLException e) {
    // 예외 처리
} finally {
    if (rs != null) try { rs.close(); } catch (SQLException ignored) {}
    if (stmt != null) try { stmt.close(); } catch (SQLException ignored) {}
    if (conn != null) try { conn.close(); } catch (SQLException ignored) {}
}
```

문제는 다음과 같다.

- 핵심 SQL은 한 줄인데 그 주변 코드가 너무 많다
- try-catch-finally가 매번 반복된다
- 자원 해제를 깜빡하면 커넥션이 누수된다
- 트랜잭션 처리도 직접 `conn.setAutoCommit(false)`, `conn.commit()`, `conn.rollback()`을 호출해야 한다

10주차 강의자료의 첫 슬라이드도 정확히 이 점을 지적한다.

## 3. 스프링 JDBC가 무엇을 줄여 주는가

스프링은 `JdbcTemplate` 클래스를 제공한다.

```java
List<Member> results = jdbcTemplate.query(
    "select * from MEMBER where EMAIL = ?",
    (rs, rowNum) -> new Member(rs.getString("EMAIL"), ...),
    email
);
```

핵심 차이:

- Connection 획득/해제 → JdbcTemplate이 처리
- Statement 생성/해제 → JdbcTemplate이 처리
- 예외 변환 → SQLException(체크 예외)을 DataAccessException(런타임 예외)으로 변환
- 트랜잭션 처리 → `@Transactional` 어노테이션 한 줄

즉 개발자는 SQL과 결과 매핑(`RowMapper`)에만 집중하면 된다.

현재 프로젝트의 `MemberRepository.selectAll`은 이 구조 그대로다.

```java
public List<Member> selectAll(String sql) {
    return jdbcTemplate.query(sql,
            (rs, rowNum) -> memRowMapper.createMember(
                    rs.getLong("ID"), rs.getString("EMAIL"),
                    rs.getString("PASSWORD"), rs.getString("NAME"),
                    rs.getTimestamp("REGDATE").toLocalDateTime()));
}
```

## 4. DataSource 자동 설정

`DataSource`는 DB 연결을 관리하는 인터페이스다.
스프링 부트는 `application.properties`의 `spring.datasource.*` 속성만 있으면 `DataSource` 빈을 자동으로 등록한다.

강의자료의 예시는 MySQL이다.

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/springdb?characterEncoding=UTF-8&serverTimezone=UTC&allowPublicKeyRetrieval=true&useSSL=false
spring.datasource.username=root
spring.datasource.password=root1234
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
```

현재 학습 프로젝트는 **실행 자체가 가능하도록 H2 임베디드 DB를 MySQL 호환 모드로 사용**한다.
즉 MySQL 서버를 따로 설치하지 않아도 같은 흐름을 그대로 학습할 수 있다.

```properties
spring.datasource.url=jdbc:h2:mem:springdb;MODE=MySQL;...
spring.datasource.username=sa
spring.datasource.password=
spring.datasource.driver-class-name=org.h2.Driver
```

스프링 부트는 이 설정을 보고 자동으로 `DataSource` 빈과 `JdbcTemplate` 빈을 만들어 준다.
필요하다면 `@Bean public DataSource dataSource()`로 수동 등록도 가능하지만,
자동 설정은 기본적으로 하나의 DB만 지원한다는 점을 기억해야 한다.

## 5. DB 관련 객체들의 역할

강의자료에서 4가지 객체 역할을 정리한다.

| 객체 | 역할 |
|---|---|
| Domain / Entity | DB 테이블과 1:1로 매핑되는 자바 객체 (`Member`) |
| DTO | 계층 간 데이터 전송 객체 |
| Repository | DB와의 입출력 (저장, 조회, 수정, 삭제) (`MemberRepository`) |
| Service | 비즈니스 로직, 트랜잭션 관리 (`MemberService`) |

현재 프로젝트의 패키지 구조는 이 역할 분리를 그대로 따른다.

```text
Lect_B/week10/
  Member.java              ← Domain (테이블 매핑)
  MemberRowMapper.java     ← ResultSet → Member 변환
  MemberRepository.java    ← @Repository, JdbcTemplate 사용
  MemberService.java       ← @Service, 비즈니스 로직
  DBServiceController.java ← @Controller, 요청 매핑
```

## 6. JdbcTemplate 만들기

`JdbcTemplate`은 `DataSource`를 받아서 만든다.
강의자료의 두 가지 방식 모두 현재 프로젝트에 적용 가능하다.

**방식 1: 직접 생성**

```java
public class MemberDao {
    private JdbcTemplate jdbcTemplate;

    public MemberDao(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }
}
```

**방식 2: 자동 주입 (Spring Boot에서 권장)**

```java
public class MemberDao {
    private final JdbcTemplate jdbcTemplate;

    // @Autowired 생략 가능(Spring 4.3+ 단일 생성자)
    public MemberDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
}
```

현재 프로젝트의 `MemberRepository`는 방식 2를 따른다.
스프링 부트가 `JdbcTemplate` 빈을 자동 등록하기 때문이다.

## 7. 함수형 인터페이스와 람다식

`JdbcTemplate`은 여러 함수형 인터페이스를 받는다.

| 인터페이스 | 추상 메서드 | 용도 |
|---|---|---|
| `RowMapper<T>` | `T mapRow(ResultSet rs, int rowNum)` | ResultSet 한 행 → 객체 변환 |
| `PreparedStatementCreator` | `PreparedStatement createPreparedStatement(Connection con)` | PreparedStatement 직접 생성 |
| `BatchPreparedStatementSetter` | `setValues(...)`, `getBatchSize()` | 배치 처리 시 파라미터 설정 |

이런 인터페이스는 추상 메서드가 1개이므로 **람다식**으로 짧게 작성할 수 있다.

```java
// 익명 클래스 방식
List<Member> results = jdbcTemplate.query(sql, new RowMapper<Member>() {
    @Override
    public Member mapRow(ResultSet rs, int rowNum) throws SQLException {
        // ... Member 객체 변환
    }
});

// 람다식 방식
List<Member> results = jdbcTemplate.query(sql,
    (rs, rowNum) -> {
        // ... Member 객체 변환
    });
```

현재 `MemberRowMapper`는 두 가지를 함께 보여 준다.

- `mapRow(ResultSet, int)` 오버라이드 → 인터페이스 구현 방식
- `rowMapper` 람다식 변수 → 람다 표현식 방식

학습 의도: 둘 다 같은 동작이며, 차이는 작성 형태뿐이라는 점을 보여 주는 것이다.

## 8. SELECT 쿼리 실행

`JdbcTemplate`은 SELECT용으로 `query()`, `queryForList()`, `queryForObject()`를 제공한다.

| 메서드 | 반환 | 용도 |
|---|---|---|
| `query(sql, rowMapper)` | `List<T>` | 행을 자바 객체 리스트로 변환 |
| `query(sql, rowMapper, args)` | `List<T>` | 파라미터 매핑 + 객체 리스트 |
| `queryForList(sql, args)` | `List<Map<String, Object>>` | 컬럼명 → 값으로 매핑된 Map 리스트 |
| `queryForObject(sql, Class<T>, args)` | `T` | 단일 값/객체 (예: count, 단일 row) |
| `queryForObject(sql, rowMapper, args)` | `T` | 단일 row를 커스텀 객체로 |

현재 프로젝트의 매핑은 다음과 같다.

| 강의자료 예제 | 현재 프로젝트 메서드 | 라우트 |
|---|---|---|
| `query()` + RowMapper 람다 | `MemberRepository.selectAll` | `/week10/simpleQuery` |
| `query()` + 파라미터 매핑 | `MemberRepository.selectAllUsingParameter` | `/week10/parameterQuery` |
| `queryForList()` | `MemberRepository.selectListMap` | `/week10/listQuery` |
| `queryForObject()` (count) | `MemberRepository.updateMember`에서 사용 | `/week10/updateQuery` |

### 8-1. query() + RowMapper

가장 자주 쓰는 패턴이다.

```java
List<Member> selectAll(String sql) {
    return jdbcTemplate.query(sql, (rs, rowNum) -> ...);
}
```

람다식 `(rs, rowNum) -> ...`은 `RowMapper<Member>`를 구현한 것과 같다.

### 8-2. 파라미터 매핑 query()

`?` 자리에 값을 채워 넣을 때 사용한다.

```java
List<Member> selectAllUsingParameter(String sql, Object[] args) {
    return jdbcTemplate.query(sql, args, memRowMapper.rowMapper);
}
```

`args` 배열의 순서대로 `?`가 바인딩된다.

### 8-3. queryForList() + List<Map>

컬럼명 → 값을 Map으로 자동 매핑한다.
강의자료에서는 이 결과를 다시 `Map<String, Member>`로 변환하는 예시를 보여 준다.

현재 프로젝트의 `MemberService.getMembersUsingMap`이 그 변환 과정을 보여 준다.

```java
List<Map<String, Object>> rows = memberRepos.selectListMap(sql, "virus1@virus.net");
// rows = [{ID=1, NAME=stdVirus, PASSWORD=2345, REGDATE=...}, ...]

List<Map<String, Member>> result = new ArrayList<>();
for (Map<String, Object> row : rows) {
    Member member = new Member(null, (String) row.get("PASSWORD"), ...);
    Map<String, Member> map = new HashMap<>();
    map.put(String.valueOf(row.get("ID")), member);
    result.add(map);
}
```

### 8-4. queryForObject() (단일 값)

쿼리 결과가 1행인 경우에만 사용한다.

```java
int count = jdbcTemplate.queryForObject(
    "select count(*) from MEMBER where EMAIL = ?",
    (rs, rowNum) -> rs.getInt(1),
    args[2]);
```

- `Class<T>` 형식: 단일 기본형 (`Integer.class`, `String.class` 등)
- `RowMapper<T>` 형식: 단일 객체

`queryForObject`는 0행이나 2행 이상이면 예외를 던진다.

## 9. INSERT/UPDATE/DELETE 쿼리 실행

변경 쿼리는 `update()` 메서드를 쓴다.

```java
int rows = jdbcTemplate.update(
    "update MEMBER set NAME = ?, PASSWORD = ? where EMAIL = ?",
    name, password, email);
```

반환값은 변경된 행의 수다.

현재 프로젝트의 `MemberRepository.updateMember`는 이 패턴 + `queryForObject(count)` 조합을 보여 준다.

## 10. PreparedStatement와 일괄 처리

### 10-1. PreparedStatement 직접 사용

`update()`, `query()`에 파라미터를 그대로 전달해도 내부적으로는 `PreparedStatement`가 만들어진다.

하지만 다음 같은 경우에는 `PreparedStatementCreator`로 직접 만들어야 한다.

- NULL 타입을 명시해야 할 때
- 대용량 바인딩, 옵션(`RETURN_GENERATED_KEYS`) 지정
- 자동 생성 키(PK) 반환 (11번 참고)

```java
public PreparedStatementCreator createPreparedStatement(Member member, String sql, String[] args) {
    return con -> {
        PreparedStatement pstmt = con.prepareStatement(sql, args);
        pstmt.setString(1, member.getEmail());
        pstmt.setString(2, member.getPassword());
        // ...
        return pstmt;
    };
}
```

### 10-2. batchUpdate (일괄 처리)

대량 INSERT/UPDATE는 한 번에 묶어서 보내는 편이 빠르다.
`batchUpdate`는 두 가지 인자 방식을 지원한다.

**방식 A: `Object[]` 리스트**

```java
List<Object[]> memberData = Arrays.asList(
    new Object[]{"john@example.com", "2456", "John", LocalDateTime.now()},
    new Object[]{"jane@example.com", "34567", "Jane", LocalDateTime.now()},
    ...
);
jdbcTemplate.batchUpdate(sql, memberData);
```

**방식 B: `BatchPreparedStatementSetter`**

```java
jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
    @Override
    public void setValues(PreparedStatement ps, int i) throws SQLException {
        Member m = memberData.get(i);
        ps.setString(1, m.getEmail());
        // ...
    }
    @Override
    public int getBatchSize() {
        return memberData.size();
    }
});
```

방식 B는 객체 → 파라미터 매핑을 직접 제어할 수 있어 유연하다.

## 11. 자동 생성 키(KeyHolder)

`MEMBER` 테이블의 `ID`는 `AUTO_INCREMENT`로 자동 생성된다.
보통 `update()`는 변경된 행 수만 반환하므로, 자동 생성된 키를 알 수 없다.

`KeyHolder`를 쓰면 받을 수 있다.

```java
KeyHolder keyHolder = new GeneratedKeyHolder();
PreparedStatementCreator pstmtObj = createPreparedStatement(member, sql, new String[]{"ID"});
jdbcTemplate.update(pstmtObj, keyHolder);
long generatedId = keyHolder.getKey().longValue();
```

핵심:

- `new String[]{"ID"}`: 반환받을 컬럼 이름을 지정
- `keyHolder.getKey()`: `Number` 타입이며 `longValue()`/`intValue()`로 원하는 타입 추출

현재 프로젝트의 `MemberRepository.insertMember`가 이 패턴을 그대로 보여 준다.

## 12. @Transactional과 롤백

여러 SQL을 하나의 단위로 묶어야 할 때가 있다.
예: 회원 정보 insert → 이메일 발송 → 둘 중 하나라도 실패하면 둘 다 취소.

순수 JDBC라면 `conn.setAutoCommit(false)` → `conn.commit()` / `conn.rollback()`을 직접 호출해야 한다.

스프링은 한 줄로 해결한다.

```java
@Transactional
public void register(Member member) {
    insertMember(...);
    sendEmail(...);
}
```

기본 동작:

- 메서드가 정상 종료되면 자동 commit
- `RuntimeException`이 발생하면 자동 rollback
- 체크 예외는 기본적으로 롤백 대상이 아님 → `rollbackFor`로 추가 지정

```java
@Transactional(rollbackFor = SQLSyntaxErrorException.class)
public void changePassword(Member member, String newPasswd) {
    insertMember(...);
    updateMember(잘못된 SQL); // 예외 발생
}
```

현재 프로젝트의 `MemberRepository.changePassword`가 정확히 이 예시다.

- 일부러 `EMAI`라는 오타(원본 강의자료의 학습용 오타)
- 첫 번째 INSERT는 성공
- 두 번째 UPDATE는 SQL 구문 오류
- 트랜잭션이 롤백되므로 INSERT도 취소

### @Transactional은 어떻게 동작하는가

`@Transactional`은 마법이 아니다.
스프링 AOP 프록시가 메서드 실행 전후에 트랜잭션을 시작/커밋/롤백한다.

즉 7주차에서 배운 AOP 흐름과 동일하다.

```text
Controller
  -> AOP Proxy
      -> 트랜잭션 시작
      -> Service 메서드 실행
      -> 정상 종료 → 커밋
         예외 발생 → 롤백
  -> Controller
```

따라서 다음과 같은 제약도 7주차 AOP 제약과 같다.

- 스프링 빈이 아닌 객체에는 적용되지 않음
- 같은 클래스 내부에서 `@Transactional` 메서드를 직접 호출하면 프록시를 거치지 않음
- `private` 메서드에는 적용되지 않음

## 13. 현재 프로젝트에서 구현한 실습 구조

원본 강의 코드는 MySQL 서버를 가정하지만,
현재 학습 프로젝트는 **H2 임베디드 DB(MySQL 호환 모드)**를 사용해 별도 DB 설치 없이 실행 가능하도록 했다.

### 13-1. Ex1 실습

URL: `/week10/usingDataSource`

확인할 것:

- `WebApplicationContext.getBean("dataSource")`로 자동 등록된 `DataSource`를 직접 사용
- Connection/Statement/예외 처리/자원 해제를 **개발자가 직접 작성**해야 함
- 이 코드와 이후 `JdbcTemplate` 코드의 양을 비교

### 13-2. Ex2 실습

URL: `/week10/simpleQuery`

확인할 것:

- `JdbcTemplate.query()` + `RowMapper` 람다식
- 5개의 `virus*` 레코드 또는 시드 데이터 출력

### 13-3. Ex3 실습

URL: `/week10/parameterQuery`

확인할 것:

- `?`로 마킹된 파라미터에 `args` 배열을 매핑
- `email`과 `name`이 동시에 일치하는 한 행만 조회

### 13-4. Ex4 실습

URL: `/week10/listQuery`

확인할 것:

- `queryForList()`가 `List<Map<String, Object>>`를 반환
- 컬럼명 키를 가진 Map을 `Map<String, Member>`로 변환

### 13-5. Ex5 실습

URL: `/week10/updateQuery`

확인할 것:

- `update()` 호출 전 `queryForObject("select count(*)...")`로 영향 받을 행 수 확인
- UPDATE 후 다시 조회한 결과 확인

### 13-6. Ex6 실습

URL: `/week10/batchUpdateArray`, `/week10/batchUpdateSetter`

확인할 것:

- 같은 SQL을 두 가지 방식의 batchUpdate로 실행
- `Object[][]` 방식과 `BatchPreparedStatementSetter` 방식의 코드 차이

### 13-7. Ex7 실습

URL: `/week10/keyHolder`

확인할 것:

- INSERT 직후 자동 생성된 `ID` 값을 콘솔에 출력
- `RETURN_GENERATED_KEYS` 옵션을 `PreparedStatementCreator`가 어떻게 적용하는지 코드로 확인

### 13-8. Ex8 실습

URL: `/week10/transactionUpdate`

확인할 것:

- `changePassword`의 첫 INSERT는 성공
- 두 번째 UPDATE에서 SQL 오류 발생
- `@Transactional` 덕분에 INSERT도 롤백되어 최종 행 수는 변하지 않음

## 14. 자주 헷갈리는 질문

### Q1. JdbcTemplate은 ORM인가?

아니다.

JdbcTemplate은 SQL을 직접 작성하는 방식이다.
ORM(JPA/Hibernate)은 객체와 테이블 매핑을 자동으로 처리하지만,
JdbcTemplate은 RowMapper를 직접 작성해 매핑한다.

### Q2. `queryForObject`로 0행이 반환되면?

`EmptyResultDataAccessException`이 발생한다.
"한 행"이 보장되는 경우(예: count 쿼리)에만 써야 한다.

### Q3. `update()`의 반환값은 무엇인가?

변경된 행의 개수다.
영향 받은 행이 없으면 0이 반환된다.

### Q4. `@Transactional`이 안 먹는 경우는?

- 같은 클래스 내부에서 직접 호출 (프록시 우회)
- `private` 메서드에 붙음
- 빈으로 등록되지 않은 객체
- 체크 예외 발생 시 (기본 정책상 롤백 대상이 아님 → `rollbackFor`로 추가)

### Q5. `KeyHolder`를 안 쓰고 자동 생성 키를 얻을 수 있는가?

`update()`만으로는 받을 수 없다.
`PreparedStatementCreator`에 `RETURN_GENERATED_KEYS` 옵션을 주고 `KeyHolder`로 받아야 한다.

### Q6. H2와 MySQL의 SQL 차이는?

대부분 같지만 `now()`, `auto_increment`, 함수 이름 등 일부 다를 수 있다.
현재 프로젝트는 H2를 MySQL 호환 모드(`MODE=MySQL`)로 띄우고, 표준 함수(`CURRENT_TIMESTAMP`)를 사용해 양쪽에서 동일하게 동작하도록 했다.

## 15. 시험 대비 핵심 정리

- 순수 JDBC API는 SQL과 무관한 반복 코드(Connection/Statement/예외 처리/자원 해제)를 많이 만든다.
- `JdbcTemplate`은 이 반복을 제거하고 `RowMapper`만 작성하면 된다.
- `DataSource`는 `application.properties`의 `spring.datasource.*` 속성으로 자동 등록된다.
- DB 객체는 Domain(Entity), DTO, Repository, Service로 역할을 분리한다.
- `RowMapper`, `PreparedStatementCreator`, `BatchPreparedStatementSetter`는 함수형 인터페이스이며 람다식으로 짧게 쓸 수 있다.
- `JdbcTemplate.query()`는 `List<T>`, `queryForList()`는 `List<Map<String, Object>>`, `queryForObject()`는 단일 값/객체를 반환한다.
- `update()`는 INSERT/UPDATE/DELETE를 실행하고 변경된 행 수를 반환한다.
- `batchUpdate()`는 대량 처리에 사용하며 `Object[][]` 방식과 `BatchPreparedStatementSetter` 방식이 있다.
- `KeyHolder`는 `PreparedStatementCreator`와 함께 사용해 자동 생성 키(PK)를 받는다.
- `@Transactional`은 메서드 단위로 트랜잭션을 관리하며, 기본적으로 `RuntimeException` 발생 시 롤백한다.
- `rollbackFor`는 추가 예외 타입을 롤백 대상으로 지정한다.
- `@Transactional`은 스프링 AOP 프록시 기반이므로 7주차 AOP 제약(같은 클래스 내부 호출, private 등)이 동일하게 적용된다.
