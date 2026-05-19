# Week 10 Practice

## 주제

10주차 실습은 제공된 `DBServiceController`, `MemberService`, `MemberRepository`, `MemberRowMapper`, `Member` 흐름을 현재 프로젝트 구조에 맞게 재작성한 것이다.

원본 실습은 MySQL 서버를 가정한다.
현재 학습 프로젝트는 별도 DB 설치 없이 바로 실행되도록 **H2 임베디드 DB(MySQL 호환 모드)**를 사용한다.
SQL 구문과 흐름은 강의자료와 동일하므로 학습 의도가 유지된다.

## 이 실습의 목적

10주차 실습의 목표는 다음 5가지를 "눈으로 확인"하는 것이다.

- 순수 JDBC 대비 `JdbcTemplate` 코드가 얼마나 줄어드는지
- `query`, `queryForList`, `queryForObject`, `update`, `batchUpdate`의 차이
- `RowMapper`, `PreparedStatementCreator`, `BatchPreparedStatementSetter` 람다식 작성
- `KeyHolder`로 자동 생성 PK를 받는 흐름
- `@Transactional` 적용 메서드에서 SQL 오류 시 데이터가 롤백되는 결과

## 관련 파일

| 경로 | 역할 |
|---|---|
| [`src/main/java/Lect_B/week10/Member.java`](../../src/main/java/Lect_B/week10/Member.java) | 테이블 매핑 Entity |
| [`src/main/java/Lect_B/week10/MemberRowMapper.java`](../../src/main/java/Lect_B/week10/MemberRowMapper.java) | ResultSet → Member 변환, 람다식 보유 |
| [`src/main/java/Lect_B/week10/MemberRepository.java`](../../src/main/java/Lect_B/week10/MemberRepository.java) | JdbcTemplate 호출 및 @Transactional 메서드 |
| [`src/main/java/Lect_B/week10/MemberService.java`](../../src/main/java/Lect_B/week10/MemberService.java) | 비즈니스 로직, SQL 구성 |
| [`src/main/java/Lect_B/week10/DBServiceController.java`](../../src/main/java/Lect_B/week10/DBServiceController.java) | `/week10/...` 라우트 제어 |
| [`src/main/java/Lect_B/week10/Week10IndexController.java`](../../src/main/java/Lect_B/week10/Week10IndexController.java) | `/week10` 진입 |
| [`src/main/resources/application.properties`](../../src/main/resources/application.properties) | `spring.datasource.*` 설정 |
| [`src/main/resources/sql/week10-schema.sql`](../../src/main/resources/sql/week10-schema.sql) | MEMBER 테이블 스키마 |
| [`src/main/resources/sql/week10-data.sql`](../../src/main/resources/sql/week10-data.sql) | 기본 시드 데이터 |
| [`src/main/webapp/views/week10/*.jsp`](../../src/main/webapp/views/week10) | 실습 결과 화면 |
| [`src/test/java/Lect_B/week10/Week10ContextTests.java`](../../src/test/java/Lect_B/week10/Week10ContextTests.java) | JDBC 흐름 통합 테스트 |

원본 10주차 실습 파일과 현재 프로젝트의 대응 관계는 다음과 같다.

| 원본 실습 파일 | 원본 의도 | 현재 프로젝트 반영 |
|---|---|---|
| `Member.java` (`com.week10.Member`) | DB 테이블과 1:1 매핑 Entity | `Lect_B.week10.Member` |
| `MemberRowMapper.java` | RowMapper 구현 + 람다 변수 | 동일 + 패키지만 변경 |
| `MemberRepository.java` | JdbcTemplate 호출 전 영역 | `MemberRepository`, 일부 메서드는 빈으로 등록되도록 정리 |
| `MemberService.java` | 비즈니스 로직 | 동일, 결과 화면용 데이터 정리 |
| `DBServiceController.java` | Ex1~Ex8 라우트 | `/week10/...` 경로로 통일, 학습 의도 보존 |
| `memberListView.jsp` | List<Map> 결과 표시 | `views/week10/memberListView.jsp` |
| `membersView.jsp` | Member 리스트 표시 | `views/week10/membersView.jsp` |
| MySQL DataSource 설정 | `spring.datasource.url=jdbc:mysql://...` | H2 임베디드 + `MODE=MySQL` |

## 1. 왜 `week10` 전용 패키지를 따로 만들었는가

10주차는 이전 주차와 성격이 다르다.

- 2~7주차는 컨테이너 안쪽 기능(빈, DI, AOP)을 다룬다.
- 10주차는 컨테이너 밖에 있는 **DB**와의 연결을 다룬다.

그래서 다음처럼 별도 패키지로 분리했다.

```text
src/main/java/Lect_B/week10/
src/main/resources/sql/week10-schema.sql
src/main/resources/sql/week10-data.sql
src/main/webapp/views/week10/
docs/week10/
```

이렇게 나누면:

- 기존 주차 코드와 빈 이름이 섞이지 않는다
- DB 접근 관련 빈만 한 패키지에서 확인할 수 있다
- `/week10` 화면에서 실습 흐름만 따로 확인할 수 있다

## 2. 왜 MySQL 대신 H2를 쓰는가

원본 강의자료는 MySQL을 가정한다.
하지만 학습 프로젝트를 누구나 바로 실행할 수 있도록 다음 두 가지를 고려했다.

- MySQL 서버 설치/계정/포트 설정이 불필요해야 한다
- SQL 구문과 흐름은 강의자료와 동일해야 한다

H2를 `MODE=MySQL`로 띄우면 양쪽을 모두 만족한다.

```properties
spring.datasource.url=jdbc:h2:mem:springdb;MODE=MySQL;DATABASE_TO_LOWER=TRUE;CASE_INSENSITIVE_IDENTIFIERS=TRUE;DB_CLOSE_DELAY=-1
spring.datasource.username=sa
spring.datasource.password=
spring.datasource.driver-class-name=org.h2.Driver
```

추가로 `spring.sql.init.schema-locations`와 `spring.sql.init.data-locations`로 시작 시 스키마/시드를 자동 적용한다.
이 덕분에 학생은 앱을 띄우자마자 `seed1@virus.net`, `seed2@virus.net` 두 행이 있는 MEMBER 테이블을 갖게 된다.

## 3. `Member`는 무엇을 보여 주는가

Domain 객체다.

```java
public class Member {
    private Long id;
    private String email;
    private String password;
    private String name;
    private LocalDateTime registerDateTime;
}
```

특징:

- `@Getter`, `@Setter`, `@NoArgsConstructor`는 Lombok이 만들어 준다
- 모든 필드 생성자가 따로 정의된 이유: `ID`는 DB에서 자동 생성되므로 INSERT 시점에는 모름

```java
public Member(String email, String password, String name, LocalDateTime regDateTime) { ... }
```

학생은 이 패턴에서 "Entity는 보통 두 가지 생성자가 필요하다"는 점을 익혀야 한다.

## 4. `MemberRowMapper`는 왜 두 가지 형태로 작성됐는가

같은 변환 로직을 두 가지 방식으로 보여 주기 위해서다.

```java
// 1) RowMapper<Member> 인터페이스 직접 구현
@Override
public Member mapRow(ResultSet rs, int rowNum) throws SQLException {
    return createMember(...);
}

// 2) 함수형 인터페이스 + 람다식
public RowMapper<Member> rowMapper = (rs, rowNum) -> createMember(...);
```

학습 의도:

- 1번 방식은 강의자료의 "익명 클래스" 예시 흐름
- 2번 방식은 강의자료의 "람다식 → 가독성/생산성" 흐름

`MemberRepository`에서 두 방식을 모두 호출해 본다는 점도 학습 포인트다.

## 5. `MemberRepository`에서 꼭 확인할 것

### 5-1. `selectAll` (Ex2)

```java
return jdbcTemplate.query(sql, (rs, rowNum) -> memRowMapper.createMember(...));
```

람다식이 `RowMapper<Member>`로 자동 변환된다.
강의자료의 슬라이드 "익명 클래스 vs 람다식"이 이 한 줄로 압축된다.

### 5-2. `selectAllUsingParameter` (Ex3)

```java
return jdbcTemplate.query(sql, args, memRowMapper.rowMapper);
```

- `args`: `?`에 매핑할 파라미터 배열
- `memRowMapper.rowMapper`: 미리 만들어 둔 람다 RowMapper 인스턴스

### 5-3. `selectListMap` (Ex4)

```java
return jdbcTemplate.queryForList(sql, email);
```

- 반환: `List<Map<String, Object>>`
- 키는 컬럼명, 값은 컬럼 값
- `MemberService.getMembersUsingMap`에서 이걸 다시 `Map<String, Member>`로 변환

### 5-4. `updateMember` (Ex5)

```java
int count = jdbcTemplate.queryForObject(
    "select count(*) from MEMBER where EMAIL = ?",
    (rs, rowNum) -> rs.getInt(1), args[2]);
System.out.println("조건 만족 레코드 수 : " + count);

int cnt = jdbcTemplate.update(sql, args);
```

- `queryForObject`로 단일 정수를 가져오는 예
- `update`로 UPDATE 실행

콘솔에는 "조건 만족 레코드 수 : 1"이 찍힌다.

### 5-5. `batchInsertMembers` / `batchInsertMembersSetter` (Ex6)

두 가지 방식의 배치 처리.

```java
// A) Object[] 리스트
jdbcTemplate.batchUpdate(sql, memberData);

// B) BatchPreparedStatementSetter
jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
    @Override
    public void setValues(PreparedStatement ps, int i) throws SQLException {
        Member m = memberData.get(i);
        ps.setString(1, m.getEmail());
        ...
    }
    @Override
    public int getBatchSize() { return memberData.size(); }
});
```

### 5-6. `insertMember` + `createPreparedStatement` (Ex7)

```java
KeyHolder keyHolder = new GeneratedKeyHolder();
PreparedStatementCreator pstmtObj = createPreparedStatement(member, sql, new String[]{"ID"});
jdbcTemplate.update(pstmtObj, keyHolder);
return keyHolder.getKey().longValue();
```

핵심:

- `new String[]{"ID"}` → 어떤 컬럼의 자동 생성 키를 받을지 지정
- `update(pstmtObj, keyHolder)` → 호출 직후 `keyHolder.getKey()`에 생성된 값이 담긴다

### 5-7. `changePassword` (Ex8)

```java
@Transactional(rollbackFor = SQLSyntaxErrorException.class)
public void changePassword(Member member, String newPasswd) {
    String sql = "insert into MEMBER (EMAIL, PASSWORD, NAME, REGDATE) values (?, ?, ?, ?)";
    insertMember(member, sql);

    sql = "update MEMBER set NAME = ?, PASSWORD = ? where EMAI = ?"; // 의도된 오타
    updateMember(sql, args1);
}
```

- 첫 번째 INSERT는 성공
- 두 번째 UPDATE는 컬럼명 오타(`EMAI`)로 `BadSqlGrammarException` 발생
- `@Transactional` 덕분에 INSERT까지 롤백

학생이 화면에서 확인할 것: 트랜잭션 호출 전후의 행 수가 같다.

## 6. `MemberService`는 어떤 데이터를 만드는가

각 라우트가 보여 줄 데이터를 준비한다.
각 메서드의 매핑은 다음과 같다.

| 메서드 | 사용 SQL | 화면 |
|---|---|---|
| `getMembers()` | `select * from MEMBER` | 전체 회원 |
| `getMembersUsingParameter()` | `select * from MEMBER where email = ? and NAME = ?` | virus3@virus.net + std3 |
| `getMembersUsingMap()` | `select id, name, password, regdate from MEMBER where email = ?` | virus1@virus.net |
| `getUpdateMembers()` | UPDATE 후 다시 SELECT | virus1@virus.net의 갱신 결과 |
| `getBatchUpdateArray()` | INSERT 3건 (Object[][]) | john, jane, mike 포함 결과 |
| `getBatchUpdateSetter()` | INSERT 3건 (Setter) | johnSetter, janeSetter, mikeSetter 포함 결과 |
| `getKeyHolder()` | 1건 INSERT + KeyHolder | 자동 ID 생성된 홍길동 |
| `transactionProcess()` | changePassword (예외 발생) | 롤백 후 동일한 행 수 |

## 7. `DBServiceController`는 어떤 라우트를 가지는가

| 라우트 | 메서드 | 뷰 |
|---|---|---|
| `/week10` | `Week10IndexController.index` | `week10/index.jsp` |
| `/week10/usingDataSource` | Ex1 | ResponseBody 문자열 |
| `/week10/simpleQuery` | Ex2 | `week10/membersView.jsp` |
| `/week10/parameterQuery` | Ex3 | `week10/membersView.jsp` |
| `/week10/listQuery` | Ex4 | `week10/memberListView.jsp` |
| `/week10/updateQuery` | Ex5 | `week10/membersView.jsp` |
| `/week10/batchUpdateArray` | Ex6 | `week10/membersView.jsp` |
| `/week10/batchUpdateSetter` | Ex6 | `week10/membersView.jsp` |
| `/week10/keyHolder` | Ex7 | `week10/membersView.jsp` |
| `/week10/transactionUpdate` | Ex8 | `week10/membersView.jsp` |

원본 코드는 `/usingDataSource`, `/simpleQuery` 같은 짧은 경로를 썼지만,
현재 프로젝트는 주차별 일관성을 위해 `/week10/...` 접두어를 붙였다.
학습 의도는 동일하다.

## 8. 실행 결과 보는 법

순서대로 실행해 보면 다음과 같은 일이 일어난다.

1. 앱 시작 → `spring.sql.init.*` 설정으로 `week10-schema.sql`과 `week10-data.sql`이 자동 실행 → MEMBER 테이블에 `seed1`, `seed2` 2행
2. `/week10/usingDataSource` → virus1~virus5 5행 INSERT → 총 7행
3. `/week10/simpleQuery` → 7행 표시
4. `/week10/parameterQuery` → `virus3@virus.net + std3` 행이 있으면 1행 표시
5. `/week10/listQuery` → `virus1@virus.net` 행이 Map 형식으로 표시
6. `/week10/updateQuery` → virus1의 NAME이 `stdVirus`로, PASSWORD가 `2345`로 변경, 콘솔에 조건 만족 레코드 수 출력
7. `/week10/batchUpdateArray` → john, jane, mike 3행 추가
8. `/week10/batchUpdateSetter` → johnSetter, janeSetter, mikeSetter 3행 추가
9. `/week10/keyHolder` → 홍길동 1행 추가, 콘솔에 자동 생성된 키 값 출력
10. `/week10/transactionUpdate` → 트랜잭션 호출 전후 행 수 동일 (롤백 확인), 콘솔에 "Transaction rolled back" 메시지

`/h2-console` (JDBC URL: `jdbc:h2:mem:springdb`, user `sa`, password 없음)로 직접 테이블 상태를 확인할 수 있다.

## 9. 강의자료와의 차이점 정리

- 강의자료의 MySQL → 본 프로젝트의 H2 임베디드(MySQL 호환 모드)
- 강의자료의 `now()` → 표준 SQL `CURRENT_TIMESTAMP` (H2/MySQL 모두 호환)
- 라우트 경로에 `/week10/` 접두어 추가 (다른 주차와의 일관성)
- 패키지명 `com.week10` → `Lect_B.week10` (프로젝트 컨벤션)
- `Lect_B.week10`을 `Lect8Application.scanBasePackages`에 추가
- 의도된 오타(`EMAI`)는 원본 그대로 유지 (트랜잭션 롤백 학습용)

## 10. 테스트로 확인하기

`Week10ContextTests`는 다음을 자동으로 검증한다.

| 테스트 | 확인 내용 |
|---|---|
| `week10BeansAreLoaded` | `MemberService`, `MemberRepository`, `JdbcTemplate` 빈 등록 |
| `selectAllReturnsSeedRecords` | 시드 데이터 `seed1@virus.net` 존재 |
| `simpleQueryAndBatchInsertWork` | batchUpdate(Object[]) 후 john/jane/mike 존재 |
| `keyHolderReturnsGeneratedId` | KeyHolder가 자동 생성된 ID를 반환 |
| `transactionRollsBackOnSqlError` | SQL 오류 시 INSERT까지 롤백 |
| `parameterQueryFiltersByEmailAndName` | 파라미터 매핑 쿼리 동작 |
| `listMapQueryReturnsMap` | `queryForList` 결과 변환 동작 |

각 테스트는 `@Sql`로 매 메서드 시작 전 스키마/데이터를 다시 적재해 상태를 격리한다.
