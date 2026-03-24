package Lect_B.week04;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class DIController {

	@Autowired
	private WebApplicationContext context;

	@GetMapping({"/annotationDI", "/qualiftingDI", "/week04/annotationDI", "/week04/qualiftingDI"})
	public ModelAndView annotationDI() {
		ModelAndView mav = new ModelAndView("week04/annotattionDIView");
		HardWorkUnit work = (HardWorkUnit) context.getBean("hardWorkUnit");
		mav.addObject("obj", work);

		return mav;
	}

	@GetMapping({"/configDI", "/week04/configDI"})
	public ModelAndView configDi(ModelAndView mav) {
		SmsSender sms = context.getBean("configSms", SmsSender.class);
		@SuppressWarnings("unchecked")
		List<String> unit = context.getBean("unit", List.class);

		mav.addObject("obj1", sms);
		mav.addObject("obj2", unit);
		mav.addObject("sendResult", sms.send("010-1234-5678", "configDI 실습 실행"));
		mav.setViewName("week04/configDIView");
		return mav;
	}
}
