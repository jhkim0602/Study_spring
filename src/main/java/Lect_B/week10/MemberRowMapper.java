package Lect_B.week10;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
public class MemberRowMapper implements RowMapper<Member> {

	public Member createMember(Long id, String email, String password,
			String name, LocalDateTime regDateTime) {
		Member member = new Member(email, password, name, regDateTime);
		member.setId(id);
		return member;
	}

	@Override
	public Member mapRow(ResultSet rs, int rowNum) throws SQLException {
		return createMember(
				rs.getLong("ID"), rs.getString("EMAIL"),
				rs.getString("PASSWORD"), rs.getString("NAME"),
				rs.getTimestamp("REGDATE").toLocalDateTime()
		);
	}

	// 람다식 형태(변수로 사용)
	public RowMapper<Member> rowMapper = (rs, rowNum) ->
			createMember(
					rs.getLong("ID"), rs.getString("EMAIL"),
					rs.getString("PASSWORD"),
					rs.getString("NAME"),
					rs.getTimestamp("REGDATE").toLocalDateTime()
			);
}
