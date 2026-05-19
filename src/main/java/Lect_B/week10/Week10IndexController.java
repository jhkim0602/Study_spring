package Lect_B.week10;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class Week10IndexController {

	@GetMapping({ "/week10", "/week10/" })
	public String index() {
		return "week10/index";
	}
}
