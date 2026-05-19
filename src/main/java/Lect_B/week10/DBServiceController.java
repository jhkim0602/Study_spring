package Lect_B.week10;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class DBServiceController {

	// 10주.Ex1 : application.properties 설정으로 자동 등록된 DataSource 빈 사용
	@Autowired
	private WebApplicationContext context;

	@ResponseBody
	@GetMapping("/week10/usingDataSource")
	public String usedDataSource() {
		DataSource dataSource = (DataSource) context.getBean("dataSource");

		Statement stmt = null;
		Connection conn = null;
		String sql;
		try {
			conn = dataSource.getConnection();
			stmt = conn.createStatement();

			for (int i = 1; i <= 5; i++) {
				sql = "insert into MEMBER(EMAIL, PASSWORD, NAME, REGDATE) "
						+ " values('virus" + i + "@virus.net' , '1234', 'std" + i + "' , CURRENT_TIMESTAMP)";
				stmt.executeUpdate(sql);
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException ignored) {
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException ignored) {
				}
			}
		}

		return "DB 연결이 정상적으로 처리되었습니다.";
	}

	// 10주.Ex2 : JdbcTemplate.query() + RowMapper 람다식
	@Autowired
	private MemberService memberService;

	@GetMapping("/week10/simpleQuery")
	public ModelAndView simpleQuery(ModelAndView mav) {
		mav.addObject("members", memberService.getMembers());
		mav.setViewName("week10/membersView");
		return mav;
	}

	// 10주.Ex3 : 파라미터 매핑 query() 사용
	@GetMapping("/week10/parameterQuery")
	public ModelAndView parameterQuery(ModelAndView mav) {
		mav.addObject("members", memberService.getMembersUsingParameter());
		mav.setViewName("week10/membersView");
		return mav;
	}

	// 10주.Ex4 : queryForList -> List<Map<String, Object>> 변환
	@GetMapping("/week10/listQuery")
	public ModelAndView listQuery(ModelAndView mav) {
		mav.addObject("memberList", memberService.getMembersUsingMap());
		mav.setViewName("week10/memberListView");
		return mav;
	}

	// 10주.Ex5 : update 메서드 + queryForObject 로 조건 만족 수 확인
	@GetMapping("/week10/updateQuery")
	public ModelAndView updateQuery(ModelAndView mav) {
		mav.addObject("members", memberService.getUpdateMembers());
		mav.setViewName("week10/membersView");
		return mav;
	}

	// 10주.Ex6 : batchUpdate(파라미터 배열)
	@GetMapping("/week10/batchUpdateArray")
	public ModelAndView batchUpdateArray(ModelAndView mav) {
		mav.addObject("members", memberService.getBatchUpdateArray());
		mav.setViewName("week10/membersView");
		return mav;
	}

	// 10주.Ex6 : batchUpdate(BatchPreparedStatementSetter)
	@GetMapping("/week10/batchUpdateSetter")
	public ModelAndView batchUpdateSetter(ModelAndView mav) {
		mav.addObject("members", memberService.getBatchUpdateSetter());
		mav.setViewName("week10/membersView");
		return mav;
	}

	// 10주.Ex7 : PreparedStatementCreator + KeyHolder 로 자동 생성 키 획득
	@GetMapping("/week10/keyHolder")
	public ModelAndView keyHolder(ModelAndView mav) {
		mav.addObject("members", memberService.getKeyHolder());
		mav.setViewName("week10/membersView");
		return mav;
	}

	// 10주.Ex8 : @Transactional - SQL 오류 시 롤백 확인
	@GetMapping("/week10/transactionUpdate")
	public ModelAndView transactionUpdate(ModelAndView mav) {
		mav.addObject("members", memberService.transactionProcess());
		mav.setViewName("week10/membersView");
		return mav;
	}
}
