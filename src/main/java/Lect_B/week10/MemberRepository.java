package Lect_B.week10;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.SQLSyntaxErrorException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class MemberRepository {

	@Autowired
	private MemberRowMapper memRowMapper = null;

	private final JdbcTemplate jdbcTemplate;

	public MemberRepository(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	public List<Member> selectAll() {
		String sql = "select * from MEMBER";
		List<Member> results = jdbcTemplate.query(sql, memRowMapper);
		return results;
	}

	public List<Member> selectAll(String sql) {
		List<Member> results = jdbcTemplate.query(sql, memRowMapper);
		return results;
	}

	public List<Member> selectAllUsingParameter(String sql, Object[] args) {
		// List<Member> results = jdbcTemplate.query(sql, new MemberRowMapper(), args);
		// List<Member> results = jdbcTemplate.query(sql, memRowMapper, args);
		List<Member> results = jdbcTemplate.query(sql, memRowMapper.rowMapper, args);
		return results;
	}

	public List<Map<String, Object>> selectListMap(String sql, String email) {
		List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql, email);
		return rows;
	}

	public void updateMember(String sql, Object[] args) {
		// email='virus1@virus.net' 인 레코드 수 획득
		int count = jdbcTemplate.queryForObject(
				"select count(*) from MEMBER where EMAIL = ?",
				(rs, rowNum) -> rs.getInt(1), args[2]);
		System.out.println("조건 만족 레코드 수 : " + count);

		int cnt = jdbcTemplate.update(sql, args);
		System.out.println("수정된 레코드 수 : " + cnt);
	}

	// Ex6
	public void batchInsertMembers(String sql, List<Object[]> memberData) {
		jdbcTemplate.batchUpdate(sql, memberData);
	}

	public void batchInsertMembersSetter(String sql, List<Member> memberData) {
		jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				Member member = memberData.get(i);
				ps.setString(1, member.getEmail());
				ps.setString(2, member.getPassword());
				ps.setString(3, member.getName());
				ps.setTimestamp(4, Timestamp.valueOf(member.getRegisterDateTime()));
			}

			@Override
			public int getBatchSize() {
				return memberData.size();
			}
		});
	}

	// Ex7
	public PreparedStatementCreator createPreparedStatement(Member member, String sql, String[] args) {
		return new PreparedStatementCreator() {

			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement pstmt = con.prepareStatement(sql, args);
				pstmt.setString(1, member.getEmail());
				pstmt.setString(2, member.getPassword());
				pstmt.setString(3, member.getName());
				pstmt.setTimestamp(4, Timestamp.valueOf(member.getRegisterDateTime()));
				return pstmt;
			}
		};
	}

	public long insertMember(Member member, String sql) {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		PreparedStatementCreator pstmtObj =
				createPreparedStatement(member, sql, new String[] {"ID"});
		jdbcTemplate.update(pstmtObj, keyHolder);

		// getKey() 메서드는 java.lang.Number 타입을 반환
		Number keyValue = keyHolder.getKey();
		// intValue(), longValue() 메서드를 이용하여 원하는 타입 값으로 변환
		return keyValue.longValue();
	}

	// Ex8
	@Transactional(rollbackFor = SQLSyntaxErrorException.class)
	public void changePassword(Member member, String newPasswd) {
		String sql = "insert into MEMBER (EMAIL, PASSWORD, NAME, REGDATE) "
				+ "values (?, ?, ?, ?)";
		insertMember(member, sql);

		sql = "update MEMBER set NAME = ?, PASSWORD = ? where EMAIL = ?";
		Object[] args1 = {member.getName(), newPasswd, member.getEmail()};
		updateMember(sql, args1);
	}
}
