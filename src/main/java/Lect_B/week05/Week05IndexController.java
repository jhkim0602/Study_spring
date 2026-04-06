package Lect_B.week05;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class Week05IndexController {

	@GetMapping({"/week05", "/week05/"})
	public String index() {
		return "week05/index";
	}
}
