package Lect_B.report1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ReportController {

	@Autowired
	private WebApplicationContext context;

	@GetMapping("/report1")
	public ModelAndView reportBean(ModelAndView mav) {
		StdManager mng = (StdManager) context.getBean("stdManager");
		mng.printResult();
		mav.addObject("stdList", mng.getStdList());
		mav.setViewName("report1/stdInfoView");

		return mav;
	}
}
