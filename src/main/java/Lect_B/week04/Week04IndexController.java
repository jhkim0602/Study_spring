package Lect_B.week04;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class Week04IndexController {

	@GetMapping({"/week04", "/week04/"})
	public String index() {
		return "week04/index";
	}
}
