package Lect_B.week10;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.SQLSyntaxErrorException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

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

	private final JdbcTemplate jdbcTemplate;

	public MemberRepository(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Autowired
	private MemberRowMapper memRowMapper;

	public List<Member> selectAll(String sql) {
		return jdbcTemplate.query(sql,
				(rs, rowNum) -> memRowMapper.createMember(
						rs.getLong("ID"), rs.getString("EMAIL"),
						rs.getString("PASSWORD"), rs.getString("NAME"),
						rs.getTimestamp("REGDATE").toLocalDateTime()));
	}

	public List<Member> selectAllUsingParameter(String sql, Object[] args) {
		return jdbcTemplate.query(sql, args, memRowMapper.rowMapper);
	}

	public List<Map<String, Object>> selectListMap(String sql, String email) {
		return jdbcTemplate.queryForList(sql, email);
	}

	public int updateMember(String sql, Object[] args) {
		int count = jdbcTemplate.queryForObject(
				"select count(*) from MEMBER where EMAIL = ?",
				(rs, rowNum) -> rs.getInt(1), args[2]);
		System.out.println("조건 만족 레코드 수 : " + count);
		return jdbcTemplate.update(sql, args);
	}

	public int[] batchInsertMembers(String sql, List<Object[]> memberData) {
		return jdbcTemplate.batchUpdate(sql, memberData);
	}

	public int[] batchInsertMembersSetter(String sql, List<Member> memberData) {
		return jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
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
		PreparedStatementCreator pstmtObj = createPreparedStatement(member, sql, new String[] { "ID" });
		jdbcTemplate.update(pstmtObj, keyHolder);
		Number keyValue = keyHolder.getKey();
		return keyValue.longValue();
	}

	@Transactional(rollbackFor = SQLSyntaxErrorException.class)
	public void changePassword(Member member, String newPasswd) {
		String sql = "insert into MEMBER (EMAIL, PASSWORD, NAME, REGDATE) " +
				"values (?, ?, ?, ?)";
		insertMember(member, sql);

		sql = "update MEMBER set NAME = ?, PASSWORD = ? where EMAI = ?";
		Object[] args1 = { member.getName(), newPasswd, member.getEmail() };
		updateMember(sql, args1);
	}
}
