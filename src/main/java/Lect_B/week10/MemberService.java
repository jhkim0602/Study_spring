package Lect_B.week10;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MemberService {

	@Autowired
	private MemberRepository memberRepos = null;

	public List<Member> getMembers() {
		return memberRepos.selectAll();
	}

	// Ex3
	public List<Member> getMembersUsingParameter() {
		String sql = "select * from MEMBER where email = ? and NAME=?"; // 간단한 SELECT 쿼리
		Object[] args = {"virus3@virus.net", "std3"};
		return memberRepos.selectAllUsingParameter(sql, args);
	}

	// Ex4
	public List<Map<String, Member>> getMembersUsingMap() {
		String sql = "select id, name, password, regdate from MEMBER where email = ?";
		List<Map<String, Object>> rows = memberRepos.selectListMap(sql, "virus1@virus.net");

		// Map<String, Object> --> Map<String, Member> 타입으로 변환
		List<Map<String, Member>> result = new ArrayList<>();
		for (Map<String, Object> row : rows) {
			Member member = new Member(
					null,
					(String) row.get("PASSWORD"),
					(String) row.get("NAME"),
					toLocalDateTime(row.get("REGDATE"))
			);

			Map<String, Member> map = new HashMap<>();
			map.put(row.get("id").toString(), member);
			result.add(map);
		}
		return result;
	}

	// Ex5
	public List<Member> getUpdateMembers() {
		String sql = "update MEMBER set NAME = ?, PASSWORD = ? where EMAIL = ?";
		Object[] args1 = {"stdVirus", "2345", "virus1@virus.net"};
		memberRepos.updateMember(sql, args1);

		sql = "select * from MEMBER where email = ?"; // 간단한 SELECT 쿼리
		Object[] args2 = {"virus1@virus.net"};

		return memberRepos.selectAllUsingParameter(sql, args2);
	}

	// Ex6
	public List<Member> getBatchUpdateArray() {
		String sql = "insert into MEMBER(EMAIL, PASSWORD, NAME, REGDATE) VALUES (?, ?, ?, ?)";

		List<Object[]> memberData = Arrays.asList(
				new Object[] {"john@example.com", "2456", "John", LocalDateTime.now()},
				new Object[] {"jane@example.com", "34567", "Jane", LocalDateTime.now()},
				new Object[] {"mike@example.com", "4567", "Mike", LocalDateTime.now()}
		);
		memberRepos.batchInsertMembers(sql, memberData);
		return getMembers();
	}

	public List<Member> getBatchUpdateSetter() {
		String sql = "insert into MEMBER(EMAIL, PASSWORD, NAME, REGDATE) VALUES (?, ?, ?, ?)";
		List<Member> memberData = Arrays.asList(
				new Member("johnSetter@example.com", "2456", "JohnSetter", LocalDateTime.now()),
				new Member("janeSetter@example.com", "34567", "JaneSetter", LocalDateTime.now()),
				new Member("mikeSetter@example.com", "4567", "MikeSetter", LocalDateTime.now())
		);
		memberRepos.batchInsertMembersSetter(sql, memberData);
		return getMembers();
	}

	// Ex7
	public List<Member> getKeyHolder() {
		String sql = "insert into MEMBER (EMAIL, PASSWORD, NAME, REGDATE) "
				+ "values (?, ?, ?, ?)";
		Member member = new Member("virus1@virus.com", "3456",
				"홍길동", LocalDateTime.now());

		long keyHolder = memberRepos.insertMember(member, sql);
		System.out.println("자동 생성된 키 값 : " + keyHolder);
		return getMembers();
	}

	// Ex8
	public List<Member> transactionProcess() {
		try {
			Member member = new Member("홍길동@virus.com", "1234",
					"홍길동", LocalDateTime.now());
			memberRepos.changePassword(member, "5678");
		} catch (Exception e) {
			System.out.println("Transaction rolled back: " + e.getMessage());
		}
		String sql = "select * from MEMBER"; // 간단한 SELECT 쿼리
		return memberRepos.selectAll(sql);
	}

	private LocalDateTime toLocalDateTime(Object value) {
		if (value instanceof LocalDateTime localDateTime) {
			return localDateTime;
		}
		if (value instanceof Timestamp timestamp) {
			return timestamp.toLocalDateTime();
		}
		return null;
	}
}
