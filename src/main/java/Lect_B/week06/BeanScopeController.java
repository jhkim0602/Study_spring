package Lect_B.week06;

import java.util.List;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.ModelAndView;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class BeanScopeController {

	private final WebApplicationContext context;
	private final Week06DifferentScopeClient differentScopeClient;
	private final Week06ObjectFactoryClient objectFactoryClient;
	private final AwareInterfaceImp awareInterfaceImp;
	private final ExternalConfigComponent externalConfigComponent;

	@GetMapping("/week06/scopeBean")
	public ModelAndView scopeBean() {
		Week06SmsSender[][] scopeBeanArray = new Week06SmsSender[4][2];
		boolean[] sameFlags = new boolean[4];

		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 2; j++) {
				scopeBeanArray[i][j] = context.getBean("week06ScopeBean" + i, Week06SmsSender.class);
			}
			sameFlags[i] = scopeBeanArray[i][0] == scopeBeanArray[i][1];
		}

		ModelAndView mav = new ModelAndView("week06/scopeBeanView");
		mav.addObject("scopeBeanArray", scopeBeanArray);
		mav.addObject("sameFlags", sameFlags);
		mav.addObject("scopeNames", List.of("singleton", "prototype", "request", "session"));
		return mav;
	}

	@GetMapping("/week06/useDifferentScope")
	public ModelAndView differentScope() {
		Week06WorkUnit[][] scopeBeanArray = new Week06WorkUnit[2][1];
		scopeBeanArray[0][0] = differentScopeClient.getWorkUnit();
		scopeBeanArray[1][0] = differentScopeClient.getWorkUnit();

		ModelAndView mav = new ModelAndView("week06/differentScopeView");
		mav.addObject("scopeBeanArray", scopeBeanArray);
		mav.addObject("title", "singleton 안에 직접 주입된 prototype");
		mav.addObject("sameResult", scopeBeanArray[0][0] == scopeBeanArray[1][0]);
		return mav;
	}

	@GetMapping("/week06/objectFactoryBeanTest")
	public ModelAndView objectFactoryTest() {
		Week06WorkUnit[][] scopeBeanArray = new Week06WorkUnit[2][1];
		scopeBeanArray[0][0] = objectFactoryClient.createWorkUnit();
		scopeBeanArray[1][0] = objectFactoryClient.createWorkUnit();

		ModelAndView mav = new ModelAndView("week06/differentScopeView");
		mav.addObject("scopeBeanArray", scopeBeanArray);
		mav.addObject("title", "ObjectFactory 로 필요 시점에 조회한 prototype");
		mav.addObject("sameResult", scopeBeanArray[0][0] == scopeBeanArray[1][0]);
		return mav;
	}

	@GetMapping("/week06/post-pre")
	public ModelAndView customMethod() {
		AnnotationConfigApplicationContext localContext =
				new AnnotationConfigApplicationContext(LifeCycleConfig.class);
		InitDestroyUnit cycle = localContext.getBean("myBean", InitDestroyUnit.class);
		List<String> initEvents = cycle.snapshotEvents();
		localContext.close();
		List<String> fullEvents = cycle.snapshotEvents();

		ModelAndView mav = new ModelAndView("week06/lifecycleView");
		mav.addObject("initEvents", initEvents);
		mav.addObject("fullEvents", fullEvents);
		return mav;
	}

	@GetMapping("/week06/awareInterfaceEx")
	public ModelAndView awareInterfaceEx() {
		ModelAndView mav = new ModelAndView("week06/awareInterfaceView");
		mav.addObject("beanNames", awareInterfaceImp.getSortedBeanNames());
		mav.addObject("beanName", awareInterfaceImp.getBeanName());
		mav.addObject("contextId", awareInterfaceImp.getContext().getId());
		return mav;
	}

	@GetMapping("/week06/externalConfigEx")
	public ModelAndView externalConfigEx() {
		ModelAndView mav = new ModelAndView("week06/externalConfigView");
		mav.addObject("obj", externalConfigComponent);
		return mav;
	}
}
