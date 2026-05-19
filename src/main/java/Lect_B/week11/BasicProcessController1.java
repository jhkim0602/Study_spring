package Lect_B.week11;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("regist")
public class BasicProcessController1 {

	// 11주.Ex1 : 경로 변수와 PathVariable
	@GetMapping("/step/{id}")
	public String detail(@PathVariable("id") Long stepId) {
		String viewName = "registerStep" + stepId;
		return "week11/" + viewName;
	}

	// 11주.Ex1 : POST step2, RedirectAttributes로 flash 메시지 전달
	@PostMapping("/step2")
	public String handleStep2(
			@RequestParam("view") String view,
			@RequestParam(value = "agree", defaultValue = "false") Boolean agree,
			RedirectAttributes redirectAttributes) {

		if (!agree) {
			redirectAttributes.addFlashAttribute("message", "약관 동의를 해주세요.");
			return "redirect:/regist/step/1";
		}
		return "week11/" + view;
	}

	// 11주.Ex3 실습부터는 주석으로 처리할 것 (강의자료 흐름 보존용)
	@PostMapping("/step3")
	public String handleStep3(
			@RequestParam("view") String view,
			@RequestParam("name") String name,
			@RequestParam("email") String email,
			@RequestParam("password") String password,
			@RequestParam("confirmPassword") String confirmPassword,
			Model model) {
		System.out.println("handleStep3");
		RegisterRequest registerRequest = new RegisterRequest(email, name,
				password, confirmPassword);
		model.addAttribute("registerRequest", registerRequest);
		return "week11/" + view;
	}

	/*
	// 11주.Ex3, Ex4 - 커맨드 객체 자동 바인딩
	@PostMapping("/step3")
	public String handleStep3(RegisterRequest registerRequest, Model model,
			@RequestParam("view") String view) {
		return "week11/" + view;
	}
	*/

	// 11주.Ex4 : @ModelAttribute - 컨트롤러 진입 전 커맨드 객체 초기화
	@ModelAttribute("registerRequest")
	public RegisterRequest initCommand(HttpServletRequest request) {
		RegisterRequest cm = new RegisterRequest();
		cm.setName("한글 이름을 입력해 주세요");
		cm.setEmail("규격에 맞춰서 이메일을 입력해 주세요");
		cm.setPassword("영문자 및 특수 문자 포함 최소 4문자 입력");
		cm.setConfirmPassword("암호를 다시 한번 입력해 주세요");
		return cm;
	}

	@GetMapping("/initCommand")
	public String initForm() {
		return "week11/registerStep2";
	}

	// 11주.Ex5 : 인덱스를 가진 커맨드 객체 (List + 중첩 객체)
	@GetMapping("/survey")
	public String form(Model model) {
		Question q1 = new Question("당신의 역할은 무엇입니까?",
				Arrays.asList("서버", "프론트", "풀스택"));
		Question q2 = new Question("많이 사용하는 개발도구는 무엇입니까?",
				Arrays.asList("이클립스", "인텔리J", "서브라임"));
		Question q3 = new Question("하고 싶은 말을 적어주세요.");

		List<Question> questions = Arrays.asList(q1, q2, q3);
		model.addAttribute("questions", questions);
		return "week11/surveyForm";
	}

	@PostMapping("/survey")
	public String submit(@ModelAttribute("ansData") AnsweredData data) {
		return "week11/surveySubmitted";
	}

	// 11주.Ex6 : MessageSource를 이용한 메시지 다국화
	@Autowired
	private MessageSource messageSource;

	@GetMapping("/message")
	public String message(Locale locale, Model model) {
		String greetingMessage = messageSource.getMessage("greeting", null, locale);
		String farewellMessage = messageSource.getMessage("farewell", null, locale);

		model.addAttribute("greeting", greetingMessage);
		model.addAttribute("farewell", farewellMessage);
		return "week11/greeting";
	}
}
