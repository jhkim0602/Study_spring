package Lect_B.week13;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class Week13IndexController {

	@GetMapping({ "/week13", "/week13/" })
	public String index() {
		return "week13/index";
	}
}
