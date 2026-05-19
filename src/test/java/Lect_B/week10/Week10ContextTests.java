package Lect_B.week10;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;

import com.example.lect8.Lect8Application;

@SpringBootTest(classes = Lect8Application.class)
@Sql(scripts = { "classpath:sql/week10-schema.sql", "classpath:sql/week10-data.sql" },
		executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
class Week10ContextTests {

	@Autowired
	private MemberService memberService;

	@Autowired
	private MemberRepository memberRepository;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Test
	void week10BeansAreLoaded() {
		assertThat(memberService).isNotNull();
		assertThat(memberRepository).isNotNull();
		assertThat(jdbcTemplate).isNotNull();
	}

	@Test
	void selectAllReturnsSeedRecords() {
		List<Member> members = memberService.getMembers();
		assertThat(members).extracting(Member::getEmail).contains("seed1@virus.net");
	}

	@Test
	void simpleQueryAndBatchInsertWork() {
		List<Member> members = memberService.getBatchUpdateArray();
		assertThat(members).extracting(Member::getEmail)
				.contains("john@example.com", "jane@example.com", "mike@example.com");
	}

	@Test
	void keyHolderReturnsGeneratedId() {
		List<Member> members = memberService.getKeyHolder();
		assertThat(members).extracting(Member::getEmail).contains("virus1@virus.com");
		assertThat(members).filteredOn(m -> "virus1@virus.com".equals(m.getEmail()))
				.extracting(Member::getId).doesNotContainNull();
	}

	@Test
	void transactionRollsBackOnSqlError() {
		int before = memberService.getMembers().size();
		List<Member> after = memberService.transactionProcess();
		assertThat(after).hasSize(before);
		assertThat(after).extracting(Member::getEmail).doesNotContain("홍길동@virus.com");
	}

	@Test
	void parameterQueryFiltersByEmailAndName() {
		jdbcTemplate.update("insert into MEMBER(EMAIL, PASSWORD, NAME, REGDATE) values (?,?,?, CURRENT_TIMESTAMP)",
				"virus3@virus.net", "1234", "std3");
		List<Member> result = memberService.getMembersUsingParameter();
		assertThat(result).hasSize(1);
		assertThat(result.get(0).getName()).isEqualTo("std3");
	}

	@Test
	void listMapQueryReturnsMap() {
		jdbcTemplate.update("insert into MEMBER(EMAIL, PASSWORD, NAME, REGDATE) values (?,?,?, CURRENT_TIMESTAMP)",
				"virus1@virus.net", "1234", "std1");
		List<Map<String, Member>> result = memberService.getMembersUsingMap();
		assertThat(result).isNotEmpty();
	}
}
