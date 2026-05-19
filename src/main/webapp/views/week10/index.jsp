<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Week10 Index</title>
</head>
<body>
<h1>10주차 DB 연동 실습</h1>
<p>application.properties로 자동 등록된 DataSource와 JdbcTemplate으로 MEMBER 테이블에 SELECT/INSERT/UPDATE/배치/키 자동 생성/트랜잭션을 적용한다.</p>
<p>학습용으로 내장 H2 DB를 MySQL 호환 모드로 사용한다. (스키마: <code>src/main/resources/sql/week10-schema.sql</code>)</p>

<ul>
	<li><a href="/week10/usingDataSource">Ex1. DataSource로 직접 INSERT (virus1~5)</a></li>
	<li><a href="/week10/simpleQuery">Ex2. JdbcTemplate.query + RowMapper 람다</a></li>
	<li><a href="/week10/parameterQuery">Ex3. 파라미터 매핑 query()</a></li>
	<li><a href="/week10/listQuery">Ex4. queryForList -> List&lt;Map&gt;</a></li>
	<li><a href="/week10/updateQuery">Ex5. update + queryForObject(count)</a></li>
	<li><a href="/week10/batchUpdateArray">Ex6-1. batchUpdate(Object[][])</a></li>
	<li><a href="/week10/batchUpdateSetter">Ex6-2. batchUpdate(BatchPreparedStatementSetter)</a></li>
	<li><a href="/week10/keyHolder">Ex7. PreparedStatementCreator + KeyHolder</a></li>
	<li><a href="/week10/transactionUpdate">Ex8. @Transactional 롤백 확인</a></li>
</ul>

<p><a href="/h2-console">H2 콘솔 열기</a> (JDBC URL: jdbc:h2:mem:springdb, user: sa)</p>
<p><a href="/">루트 인덱스로 이동</a></p>
</body>
</html>
