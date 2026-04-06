package Lect_B.week05;

import java.util.List;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class Week05PracticeController {

	private final WebApplicationContext context;
	private final AnimalService animalService;
	private final ScopeSingletonClient scopeSingletonClient;
	private final ScopeFactoryClient scopeFactoryClient;
	private final RequestTrace requestTrace;
	private final SessionTrace sessionTrace;
	private final Week05AwareComponent awareComponent;
	private final Week05ExternalConfigComponent externalConfig;

	@GetMapping("/week05/autoDI")
	public ModelAndView autoDi() {
		AnimalAutoDI service = context.getBean(AnimalAutoDI.class);

		ModelAndView mav = new ModelAndView("week05/autoDIView");
		mav.addObject("obj", service);
		mav.addObject("sendResult",
				service.getSms().send("010-0505-1001", "필드/생성자/Setter 주입 확인"));
		return mav;
	}

	@GetMapping("/week05/commonDI")
	public ModelAndView commonDi() {
		CommonService service = context.getBean(CommonService.class);
		service.setDefaultValue(30);
		service.setPeriodTime(20);

		ModelAndView mav = new ModelAndView("week05/commonDIView");
		mav.addObject("obj", service);
		mav.addObject("summary", service.summarize());
		return mav;
	}

	@GetMapping("/week05/lombokXml")
	public ModelAndView lombokXml() {
		LombokService service = context.getBean("xmlLombokService", LombokService.class);
		WorkUnit extraUnit1 = context.getBean("xmlUnit", WorkUnit.class);
		WorkUnit extraUnit2 = context.getBean("xmlUnit", WorkUnit.class);

		ModelAndView mav = new ModelAndView("week05/lombokXmlView");
		mav.addObject("obj", service);
		mav.addObject("runResult", service.run());
		mav.addObject("extraUnit1", extraUnit1);
		mav.addObject("extraUnit2", extraUnit2);
		return mav;
	}

	@GetMapping("/week05/list")
	public ModelAndView useList() {
		ModelAndView mav = new ModelAndView("week05/listDIView");
		mav.addObject("annotationAnimals", animalService.getAnoAnimals());
		mav.addObject("xmlAnimals", animalService.getXmlAnimals());
		return mav;
	}

	@GetMapping("/week05/map")
	public ModelAndView useMap() {
		ModelAndView mav = new ModelAndView("week05/mapDIView");
		mav.addObject("annotationAnimalsMap", animalService.getAnoAnimalsMap());
		mav.addObject("xmlAnimalsMap", animalService.getXmlAnimalMap());
		return mav;
	}

	@GetMapping("/week05/scope")
	public ModelAndView scope(HttpSession session) {
		SmsSender singleton1 = context.getBean("smsSender", SmsSender.class);
		SmsSender singleton2 = context.getBean("smsSender", SmsSender.class);
		WorkUnit prototype1 = context.getBean("workUnit", WorkUnit.class);
		WorkUnit prototype2 = context.getBean("workUnit", WorkUnit.class);
		WorkUnit factoryUnit1 = scopeFactoryClient.createWorkUnit();
		WorkUnit factoryUnit2 = scopeFactoryClient.createWorkUnit();

		ModelAndView mav = new ModelAndView("week05/scopeView");
		mav.addObject("singleton1", singleton1);
		mav.addObject("singleton2", singleton2);
		mav.addObject("singletonSame", singleton1 == singleton2);
		mav.addObject("prototype1", prototype1);
		mav.addObject("prototype2", prototype2);
		mav.addObject("prototypeSame", prototype1 == prototype2);
		mav.addObject("directUnit1", scopeSingletonClient.currentUnitId());
		mav.addObject("directUnit2", scopeSingletonClient.currentUnitId());
		mav.addObject("factoryUnit1", factoryUnit1);
		mav.addObject("factoryUnit2", factoryUnit2);
		mav.addObject("requestTrace", requestTrace);
		mav.addObject("sessionTrace", sessionTrace);
		mav.addObject("sessionAccessCount", sessionTrace.markAccess());
		mav.addObject("sessionId", session.getId());
		return mav;
	}

	@GetMapping("/week05/lifecycle")
	public ModelAndView lifecycle() {
		AnnotationConfigApplicationContext localContext =
				new AnnotationConfigApplicationContext(Week05LifecycleConfig.class);
		Week05LifecycleBean bean = localContext.getBean(Week05LifecycleBean.class);
		List<String> initEvents = bean.snapshotEvents();
		localContext.close();
		List<String> fullEvents = bean.snapshotEvents();

		ModelAndView mav = new ModelAndView("week05/lifecycleView");
		mav.addObject("initEvents", initEvents);
		mav.addObject("fullEvents", fullEvents);
		return mav;
	}

	@GetMapping("/week05/aware")
	public ModelAndView aware() {
		ModelAndView mav = new ModelAndView("week05/awareView");
		mav.addObject("aware", awareComponent);
		mav.addObject("lookupSms", awareComponent.lookupSmsSender());
		return mav;
	}

	@GetMapping("/week05/properties")
	public ModelAndView properties() {
		ModelAndView mav = new ModelAndView("week05/propertiesView");
		mav.addObject("config", externalConfig);
		mav.addObject("summary", externalConfig.summary());
		return mav;
	}
}
