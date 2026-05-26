package Lect_B.week13;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;

import com.example.lect8.Lect8Application;

@SpringBootTest(classes = Lect8Application.class)
class Week13ContextTests {

	@Autowired
	private BasicProcessController2 controller;

	@Autowired
	private MvcConfig2 mvcConfig;

	@Test
	void week13BeansAreLoaded() {
		assertThat(controller).isNotNull();
		assertThat(mvcConfig).isNotNull();
	}

	@Test
	void registerRequestFormPasswordMatch() {
		RegisterRequestForm match = new RegisterRequestForm("a@b.com", "name", "pw1234!", "pw1234!");
		assertThat(match.isPasswordEqualToConfirmPassword()).isTrue();

		RegisterRequestForm mismatch = new RegisterRequestForm("a@b.com", "name", "pw1234!", "other");
		assertThat(mismatch.isPasswordEqualToConfirmPassword()).isFalse();
	}

	@Test
	void registerRequestValidatorSupportsTargetType() {
		RegisterRequestValidator validator = new RegisterRequestValidator();
		assertThat(validator.supports(RegisterRequestForm.class)).isTrue();
		assertThat(validator.supports(String.class)).isFalse();
	}

	@Test
	void registerRequestValidatorRejectsInvalidEmail() {
		RegisterRequestValidator validator = new RegisterRequestValidator();
		RegisterRequestForm form = new RegisterRequestForm();
		form.setEmail("no-at-sign");
		form.setName("aname");
		form.setPassword("Abcd1234!");
		form.setConfirmPassword("Abcd1234!");

		Errors errors = new BeanPropertyBindingResult(form, "registerRequest");
		validator.validate(form, errors);

		assertThat(errors.getFieldError("email")).isNotNull();
		assertThat(errors.getFieldError("email").getCode()).isEqualTo("email.invalid");
	}

	@Test
	void registerRequestValidatorRejectsWeakPassword() {
		RegisterRequestValidator validator = new RegisterRequestValidator();
		RegisterRequestForm form = new RegisterRequestForm();
		form.setEmail("a@b.com");
		form.setName("aname");
		form.setPassword("short");
		form.setConfirmPassword("short");

		Errors errors = new BeanPropertyBindingResult(form, "registerRequest");
		validator.validate(form, errors);

		assertThat(errors.getFieldError("password")).isNotNull();
		assertThat(errors.getFieldError("password").getCode()).isEqualTo("password.bad");
	}

	@Test
	void registerRequestValidatorRejectsConfirmPasswordMismatch() {
		RegisterRequestValidator validator = new RegisterRequestValidator();
		RegisterRequestForm form = new RegisterRequestForm();
		form.setEmail("a@b.com");
		form.setName("aname");
		form.setPassword("Abcd1234!");
		form.setConfirmPassword("Different1!");

		Errors errors = new BeanPropertyBindingResult(form, "registerRequest");
		validator.validate(form, errors);

		assertThat(errors.getFieldError("confirmPassword")).isNotNull();
		assertThat(errors.getFieldError("confirmPassword").getCode()).isEqualTo("confirmPassword.nomatch");
	}

	@Test
	void registerRequestValidatorAcceptsValidForm() {
		RegisterRequestValidator validator = new RegisterRequestValidator();
		RegisterRequestForm form = new RegisterRequestForm("a@b.com", "aname", "Abcd1234!", "Abcd1234!");

		Errors errors = new BeanPropertyBindingResult(form, "registerRequest");
		validator.validate(form, errors);

		assertThat(errors.hasErrors()).isFalse();
	}

	@Test
	void formatCommandSettersAndGettersWork() {
		FormatCommand cmd = new FormatCommand();
		cmd.setNumber(1234.56);
		cmd.setPrice(9999.99);
		cmd.setPercentage(0.156);

		assertThat(cmd.getNumber()).isEqualTo(1234.56);
		assertThat(cmd.getPrice()).isEqualTo(9999.99);
		assertThat(cmd.getPercentage()).isEqualTo(0.156);
	}
}
