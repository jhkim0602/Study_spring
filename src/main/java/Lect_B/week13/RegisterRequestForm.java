package Lect_B.week13;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequestForm {

	// 13주.Ex3 : JSR-380(Bean Validation) 어노테이션 기반 검증
	@Email(message = "이메일이 null이거나 양식이 일치하지 않습니다.")
	private String email;

	@Size(min = 2, max = 50, message = "이름은 반드시 2~50문자로 구성합니다.")
	private String name;

	@Size(min = 5, max = 20, message = "암호는 반드시 5~20문자로 구성됩니다.")
	@Pattern(
			regexp = "^(?=.*[!@#$%^&*(),.?\":{}|<>])[A-Za-z\\d!@#$%^&*(),.?\":{}|<>]{5,20}$",
			message = "암호는 반드시 특수문자 및 숫자를 포함합니다.")
	private String password;

	private String confirmPassword;

	public boolean isPasswordEqualToConfirmPassword() {
		return password != null && password.equals(confirmPassword);
	}
}
