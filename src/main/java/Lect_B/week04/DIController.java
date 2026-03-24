package Lect_B.week04;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class DIController {

	@Autowired
	private WebApplicationContext context;

	@Autowired
	private LombokWorkUnit lombokWorkUnit;

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
		mav.addObject("obj1Type", sms.getClass().getName());
		mav.addObject("obj2Type", unit.getClass().getName());
		mav.addObject("sendResult", sms.send("010-1234-5678", "configDI 실습 실행"));
		mav.setViewName("week04/configDIView");
		return mav;
	}

	@GetMapping({"/xmlDI", "/week04/xmlDI"})
	public ModelAndView xmlDi() {
		try (ClassPathXmlApplicationContext xmlContext =
				new ClassPathXmlApplicationContext("xml/week04-beans.xml")) {
			SmsSender xmlSms = xmlContext.getBean("week04XmlSms", SmsSender.class);
			SmsSender configSms = context.getBean("configSms", SmsSender.class);

			ModelAndView mav = new ModelAndView("week04/beanView");
			mav.addObject("xmlBeanName", "week04XmlSms");
			mav.addObject("configBeanName", "configSms");
			mav.addObject("xmlSms", xmlSms);
			mav.addObject("configSms", configSms);
			mav.addObject("xmlSmsType", xmlSms.getClass().getName());
			mav.addObject("configSmsType", configSms.getClass().getName());
			mav.addObject("xmlMessage", xmlSms.send("010-1111-2222", "XML 방식으로 생성한 Bean"));
			mav.addObject("configMessage", configSms.send("010-3333-4444", "Java Config 방식으로 생성한 Bean"));
			return mav;
		}
	}

	@GetMapping({"/lombokDI", "/week04/lombokDI"})
	public ModelAndView lombokDi(ModelAndView mav) {
		try (ClassPathXmlApplicationContext xmlContext =
				new ClassPathXmlApplicationContext("xml/week04-beans.xml")) {
			LombokXmlService xmlLombokService =
					xmlContext.getBean("xmlLombokService", LombokXmlService.class);

			mav.addObject("obj", lombokWorkUnit);
			mav.addObject("sendResult", lombokWorkUnit.sendPracticeMessage());
			mav.addObject("xmlObj", xmlLombokService);
			mav.addObject("xmlSendResult", xmlLombokService.run());
			mav.setViewName("week04/lombokDIView");
			return mav;
		}
	}

	@GetMapping({"/contextDI", "/week04/contextDI"})
	public ModelAndView contextDi() {
		HardWorkUnit work = context.getBean("hardWorkUnit", HardWorkUnit.class);

		ModelAndView mav = new ModelAndView("week04/context");
		mav.addObject("contextType", context.getClass().getName());
		mav.addObject("workUnitType", work.getWorkUnit().getClass().getName());
		mav.addObject("workUnitName", work.getWorkUnit().getUnitName());
		mav.addObject("senderType", work.getSms().getClass().getName());
		mav.addObject("senderName", work.getSms().getSenderName());
		mav.addObject("greeting", work.getMsg());
		mav.addObject("resultMessage", work.sendPracticeMessage());
		return mav;
	}

	@GetMapping({"/modelAndViewNew", "/week04/modelAndViewNew"})
	public ModelAndView modelAndViewNew() {
		ModelAndView mav = new ModelAndView();
		SmsSender sms = context.getBean("configSms", SmsSender.class);

		mav.addObject("title", "new 연산자로 직접 생성한 ModelAndView");
		mav.addObject("createType", "controller method inside new ModelAndView()");
		mav.addObject("senderName", sms.getSenderName());
		mav.addObject("message", sms.send("010-5555-1111", "new 연산자로 생성한 ModelAndView 예제"));
		mav.setViewName("week04/modelAndViewView");
		return mav;
	}

	@GetMapping({"/modelAndViewParam", "/week04/modelAndViewParam"})
	public ModelAndView modelAndViewParam(ModelAndView mav) {
		SmsSender sms = context.getBean("configSms", SmsSender.class);

		mav.addObject("title", "매개변수로 주입받은 ModelAndView");
		mav.addObject("createType", "injected by Spring as controller method parameter");
		mav.addObject("senderName", sms.getSenderName());
		mav.addObject("message", sms.send("010-5555-2222", "매개변수로 주입받은 ModelAndView 예제"));
		mav.setViewName("week04/modelAndViewView");
		return mav;
	}
}
