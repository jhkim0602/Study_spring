package Lect_B.week03;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.context.support.XmlWebApplicationContext;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ContextController {

	private XmlWebApplicationContext xmlContext;
	private AnnotationConfigWebApplicationContext configContext;
	private ModelAndView mav;

	public ContextController() {
		super();
		xmlContext = new XmlWebApplicationContext();
		xmlContext.setConfigLocation("classpath:static/xml/Ex1.xml");
		xmlContext.refresh();
		configContext = new AnnotationConfigWebApplicationContext();
		configContext.register(AppConfig.class);
		configContext.refresh();
		// 생성자에서 View 이름을 지정하지 않는 경우, 나중에 setViewName()으로 지정한다.
		mav = new ModelAndView();
	}

	@GetMapping("/createBean")
	public ModelAndView beanTest() {
		SmsSender xmlSms = (SmsSender) xmlContext.getBean("xmlSms");
		SmsSender configSms = (SmsSender) configContext.getBean("configSms");

		// 어떤 View 페이지로 이동할지 지정한다.
		mav.setViewName("week03/beanView");
		// View 페이지에서 사용할 모델 객체를 전달한다.
		mav.addObject("xmlSms", xmlSms);
		// View 페이지에서 사용할 모델 객체를 전달한다.
		mav.addObject("configSms", configSms);
		// 객체 자체뿐 아니라 단순 문자열 데이터도 함께 전달할 수 있다.
		mav.addObject("xmlSmsType", xmlSms.getClass().getName());
		// 객체 자체뿐 아니라 단순 문자열 데이터도 함께 전달할 수 있다.
		mav.addObject("configSmsType", configSms.getClass().getName());
		return mav;
	}
}
