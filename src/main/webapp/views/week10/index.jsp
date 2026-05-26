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
<p>로컬 MySQL을 사용한다. 사전에 <code>springdb</code> 데이터베이스를 생성하고 <code>application.properties</code>의 datasource 설정(URL/계정/비밀번호)을 확인할 것.</p>

<ul>
	<li><a href="/usingDataSource">Ex1. DataSource로 직접 INSERT (virus1~5)</a></li>
	<li><a href="/simpleQuery">Ex2. JdbcTemplate.query + RowMapper 람다</a></li>
	<li><a href="/parameterQuery">Ex3. 파라미터 매핑 query()</a></li>
	<li><a href="/listQuery">Ex4. queryForList -> List&lt;Map&gt;</a></li>
	<li><a href="/updateQuery">Ex5. update + queryForObject(count)</a></li>
	<li><a href="/batchUpdateArray">Ex6-1. batchUpdate(Object[][])</a></li>
	<li><a href="/batchUpdateSetter">Ex6-2. batchUpdate(BatchPreparedStatementSetter)</a></li>
	<li><a href="/keyHolder">Ex7. PreparedStatementCreator + KeyHolder</a></li>
	<li><a href="/transactionUpdate">Ex8. @Transactional 롤백 확인</a></li>
</ul>
<p><a href="/">루트 인덱스로 이동</a></p>
</body>
</html>
