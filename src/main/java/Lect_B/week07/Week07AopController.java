package Lect_B.week07;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class Week07AopController {

	private final Week07AopService aopService;
	private final Week07XmlAopService xmlAopService;
	private final AopEventLog eventLog;

	public Week07AopController(Week07AopService aopService, Week07XmlAopService xmlAopService,
			AopEventLog eventLog) {
		this.aopService = aopService;
		this.xmlAopService = xmlAopService;
		this.eventLog = eventLog;
	}

	@GetMapping("/week07/before")
	public ModelAndView before(
			@RequestParam(defaultValue = "student") String userId,
			@RequestParam(defaultValue = "ADMIN") String role,
			@RequestParam(defaultValue = "타겟 메서드 호출") String message,
			@RequestParam(defaultValue = "3") int count) {
		eventLog.reset();

		ModelAndView mav = new ModelAndView("week07/beforeView");
		try {
			String result = aopService.performSensitiveOperation(userId, role, message, count);
			mav.addObject("result", result);
			mav.addObject("success", true);
		} catch (RuntimeException ex) {
			mav.addObject("error", ex.getMessage());
			mav.addObject("success", false);
		}

		mav.addObject("userId", userId);
		mav.addObject("role", role);
		mav.addObject("message", message);
		mav.addObject("count", count);
		mav.addObject("events", eventLog.snapshot());
		return mav;
	}

	@GetMapping("/week07/after")
	public ModelAndView after(
			@RequestParam(defaultValue = "100") double orderValue,
			@RequestParam(defaultValue = "50") double minimumValue) {
		eventLog.reset();

		ModelAndView mav = new ModelAndView("week07/afterView");
		try {
			String result = aopService.placeOrder(orderValue, minimumValue);
			mav.addObject("result", result);
			mav.addObject("success", true);
		} catch (RuntimeException ex) {
			mav.addObject("error", ex.getMessage());
			mav.addObject("success", false);
		}

		mav.addObject("orderValue", orderValue);
		mav.addObject("minimumValue", minimumValue);
		mav.addObject("events", eventLog.snapshot());
		return mav;
	}

	@GetMapping("/week07/around")
	public ModelAndView around(
			@RequestParam(defaultValue = "student") String userId,
			@RequestParam(defaultValue = "admin") String role) {
		eventLog.reset();

		String result = aopService.check(userId, role);

		ModelAndView mav = new ModelAndView("week07/aroundView");
		mav.addObject("userId", userId);
		mav.addObject("role", role);
		mav.addObject("result", result);
		mav.addObject("events", eventLog.snapshot());
		return mav;
	}

	@GetMapping("/week07/pointcut")
	public ModelAndView pointcut(@RequestParam(defaultValue = "annotation pointcut") String label) {
		eventLog.reset();

		String result = aopService.annotationTarget(label);

		ModelAndView mav = new ModelAndView("week07/pointcutView");
		mav.addObject("label", label);
		mav.addObject("result", result);
		mav.addObject("events", eventLog.snapshot());
		return mav;
	}

	@GetMapping("/week07/xml")
	public ModelAndView xmlAop(
			@RequestParam(defaultValue = "student") String userId,
			@RequestParam(defaultValue = "admin") String role,
			@RequestParam(defaultValue = "100") double orderValue,
			@RequestParam(defaultValue = "50") double minimumValue) {
		eventLog.reset();

		ModelAndView mav = new ModelAndView("week07/xmlView");
		try {
			String orderResult = xmlAopService.processXmlOrder(orderValue, minimumValue);
			mav.addObject("orderResult", orderResult);
			mav.addObject("orderSuccess", true);
		} catch (RuntimeException ex) {
			mav.addObject("orderError", ex.getMessage());
			mav.addObject("orderSuccess", false);
		}

		String permissionResult = xmlAopService.checkXmlPermission(userId, role);

		mav.addObject("userId", userId);
		mav.addObject("role", role);
		mav.addObject("orderValue", orderValue);
		mav.addObject("minimumValue", minimumValue);
		mav.addObject("permissionResult", permissionResult);
		mav.addObject("events", eventLog.snapshot());
		return mav;
	}
}
