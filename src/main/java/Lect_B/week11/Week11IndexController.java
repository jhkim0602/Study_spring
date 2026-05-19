package Lect_B.week11;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class Week11IndexController {

	@GetMapping({ "/week11", "/week11/" })
	public String index() {
		return "week11/index";
	}
}
