package Lect_B.week06;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class Week06IndexController {

	@GetMapping({"/week06", "/week06/"})
	public String index() {
		return "week06/index";
	}
}
