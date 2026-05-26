package Lect_B.week13;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

// 13주.Ex2 : Validator 인터페이스 구현체. 커맨드 객체의 필드 단위 검증.
public class RegisterRequestValidator implements Validator {

	// 최소 8자, 대소문자/숫자/특수문자 1개 이상씩 포함
	private static final String regExp =
			"^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";

	private final Pattern pattern;

	public RegisterRequestValidator() {
		this.pattern = Pattern.compile(regExp);
	}

	@Override
	public boolean supports(Class<?> clazz) {
		// isAssignableFrom : 검증 대상 타입이 RegisterRequestForm 인지 확인
		return RegisterRequestForm.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		System.out.println("-------RegisterRequestValidator.validate-----------");
		RegisterRequestForm regReq = (RegisterRequestForm) target;

		if (regReq.getEmail() == null || !regReq.getEmail().contains("@")) {
			errors.rejectValue("email", "email.invalid", "Invalid email format.");
		}

		String password = regReq.getPassword();
		if (password == null) {
			errors.rejectValue("password", "password.bad");
		} else {
			Matcher matcher = pattern.matcher(password);
			if (!matcher.matches()) {
				errors.rejectValue("password", "password.bad");
			}
		}

		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "name.empty", "Name is required.");
		ValidationUtils.rejectIfEmpty(errors, "confirmPassword", "confirmPassword.required");

		if (password != null && !password.isEmpty() && !regReq.isPasswordEqualToConfirmPassword()) {
			errors.rejectValue("confirmPassword", "confirmPassword.nomatch");
		}
	}
}
