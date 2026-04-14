package Lect_B.week07;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class Week07IndexController {

	@GetMapping({"/week07", "/week07/"})
	public String index() {
		return "week07/index";
	}
}
