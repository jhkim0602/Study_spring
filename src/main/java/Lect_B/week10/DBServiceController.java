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

	@Autowired
	private WebApplicationContext context = null;

	@Autowired
	private MemberService memberService;

	// application.properties 파일의 설정 정보를 이용해 DataSource 타입 객체를 생성
	// DataSource 타입 객체와 JDBC API를 이용한 DB 연결 및 SQL 실행
	@ResponseBody
	@GetMapping("/usingDataSource")
	public String usedDataSource() {
		DataSource dataSource = (DataSource) context.getBean("dataSource");

		Statement stmt = null;
		Connection conn = null;
		String sql;

		try {
			conn = dataSource.getConnection();
			stmt = conn.createStatement();

			sql = "create table if not exists MEMBER("
					+ "ID int auto_increment primary key, "
					+ "EMAIL varchar(100), "
					+ "PASSWORD varchar(100), "
					+ "NAME varchar(100), "
					+ "REGDATE timestamp)";
			stmt.executeUpdate(sql);

			for (int i = 1; i <= 5; i++) {
				sql = "insert into MEMBER(EMAIL, PASSWORD, NAME, REGDATE) "
						+ " values('virus" + i + "@virus.net' , '1234', 'std" + i + "' , now())";
				stmt.executeUpdate(sql);
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			if (stmt != null)
				try {
					stmt.close();
				} catch (SQLException e) {
				}
			if (conn != null)
				try {
					conn.close();
				} catch (SQLException e) {
				}
		}

		return "DB 연결이 정상적으로 처리되었습니다.";
	}

	@GetMapping("/simpleQuery")
	public ModelAndView simpleQuery(ModelAndView mav) {
		mav.addObject("memberList", memberService.getMembers());
		mav.setViewName("week10/membersView");
		return mav;
	}

	@GetMapping("/parameterQuery")
	public ModelAndView parameterQuery(ModelAndView mav) {
		mav.addObject("memberList", memberService.getMembersUsingParameter());
		mav.setViewName("week10/membersView");
		return mav;
	}

	@GetMapping("/listQuery")
	public ModelAndView listQuery(ModelAndView mav) {
		mav.addObject("memberList", memberService.getMembersUsingMap());
		mav.setViewName("week10/memberListView");
		return mav;
	}

	@GetMapping("/updateQuery")
	public ModelAndView updateQuery(ModelAndView mav) {
		mav.addObject("memberList", memberService.getUpdateMembers());
		mav.setViewName("week10/membersView");
		return mav;
	}

	// Ex6
	@GetMapping("/batchUpdateArray")
	public ModelAndView batchUpdateArray(ModelAndView mav) {
		mav.addObject("members", memberService.getBatchUpdateArray());
		mav.addObject("memberList", memberService.getMembers());
		mav.setViewName("week10/membersView");
		return mav;
	}

	// Ex6
	@GetMapping("/batchUpdateSetter")
	public ModelAndView batchUpdateSetter(ModelAndView mav) {
		mav.addObject("members", memberService.getBatchUpdateSetter());
		mav.addObject("memberList", memberService.getMembers());
		mav.setViewName("week10/membersView");
		return mav;
	}

	// Ex7
	@GetMapping("/keyHolder")
	public ModelAndView keyHolder(ModelAndView mav) {
		mav.addObject("members", memberService.getKeyHolder());
		mav.addObject("memberList", memberService.getMembers());
		mav.setViewName("week10/membersView");
		return mav;
	}

	// Ex8
	@GetMapping("/transactionUpdate")
	public ModelAndView transactionUpdate(ModelAndView mav) {
		mav.addObject("members", memberService.transactionProcess());
		mav.addObject("memberList", memberService.getMembers());
		mav.setViewName("week10/membersView");
		return mav;
	}
}
