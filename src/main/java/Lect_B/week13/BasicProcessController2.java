package Lect_B.week13;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.NumberFormat;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/week13")
public class BasicProcessController2 {

	// 13주.Ex1 : <spring:message> + <form:form> 데모용 폼 화면 (검증 없음)
	// 13주.Ex2 : 수동 Validator 사용 폼
	// 13주.Ex3 : @Valid 어노테이션 검증 폼
	// 13주.Ex4 : 글로벌 Validator(@Validated) 검증 폼
	@GetMapping({ "/message", "/validation", "/register", "/globalValidator" })
	public String membershipForm(jakarta.servlet.http.HttpServletRequest request, Model model) {
		String uri = request.getRequestURI();
		String formAction;
		if (uri.endsWith("/validation")) {
			formAction = "/week13/membershipValidate";   // Ex2
		} else if (uri.endsWith("/register")) {
			formAction = "/week13/userSubmit";           // Ex3
		} else if (uri.endsWith("/globalValidator")) {
			formAction = "/week13/globalValidatorSubmit"; // Ex4
		} else {
			formAction = "/week13/membershipSubmit";     // Ex1
		}
		model.addAttribute("registerRequest", new RegisterRequestForm());
		model.addAttribute("formAction", formAction);
		return "week13/registerForm";
	}

	// 13주.Ex1 : 단순 폼 제출 - 입력값을 그대로 welcome 화면에 표시
	@PostMapping("/membershipSubmit")
	public String membershipSubmit(RegisterRequestForm regReq, Model model) {
		model.addAttribute("registerRequest", regReq);
		return "week13/welcome";
	}

	// 13주.Ex2 : Validator 구현체를 컨트롤러 내부에서 직접 호출
	@PostMapping("/membershipValidate")
	public String membershipValidate(
			@ModelAttribute("registerRequest") RegisterRequestForm regReq,
			BindingResult result,
			Model model) {
		model.addAttribute("registerRequest", regReq);
		model.addAttribute("formAction", "/week13/membershipValidate");
		new RegisterRequestValidator().validate(regReq, result);
		if (result.hasErrors()) {
			return "week13/registerForm";
		}
		return "week13/welcome";
	}

	// 13주.Ex3 : JSR-380 어노테이션 검증 (@Valid)
	@PostMapping("/userSubmit")
	public String userSubmit(
			@Valid @ModelAttribute("registerRequest") RegisterRequestForm registerRequest,
			BindingResult result,
			Model model) {
		System.out.println("@Valid 오류 수 : " + result.getErrorCount());
		model.addAttribute("formAction", "/week13/userSubmit");
		if (result.hasErrors()) {
			return "week13/registerForm";
		}
		return "week13/welcome";
	}

	// 13주.Ex4 : 글로벌 Validator (MvcConfig2.getValidator()) 가 자동 적용
	// @Validated 어노테이션 + Errors 파라미터 조합
	@PostMapping("/globalValidatorSubmit")
	public String globalValidatorSubmit(
			@Validated @ModelAttribute("registerRequest") RegisterRequestForm regReq,
			Errors errors,
			Model model) {
		model.addAttribute("registerRequest", regReq);
		if (errors.hasErrors()) {
			return "week13/errorMessage";
		}
		return "week13/welcome";
	}

	// 13주.Ex5 : @DateTimeFormat, @NumberFormat 으로 요청 파라미터의 타입 변환
	@GetMapping("/convert")
	public String sampleFormat(
			@RequestParam("number") @NumberFormat(style = NumberFormat.Style.NUMBER) Double number,
			@RequestParam("price") @NumberFormat(style = NumberFormat.Style.CURRENCY) Double price,
			@RequestParam("percentage") @NumberFormat(style = NumberFormat.Style.PERCENT) Double percentage,
			@RequestParam("dateTime") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime dateTime,
			@ModelAttribute FormatCommand format,
			Model model) {

		model.addAttribute("Number", number);
		model.addAttribute("Price", price);
		model.addAttribute("Percentage", percentage);

		// JSTL <fmt:formatDate> 는 java.util.Date 타입을 요구하므로 Date 로 변환
		Date date = Date.from(dateTime.atZone(ZoneId.systemDefault()).toInstant());
		model.addAttribute("dateTime", date);

		format.setDate(date);
		model.addAttribute("obj", format);
		return "week13/formatResult";
	}
}
