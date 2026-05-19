package Lect_B.week11;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Locale;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;

import com.example.lect8.Lect8Application;

@SpringBootTest(classes = Lect8Application.class)
class Week11ContextTests {

	@Autowired
	private BasicProcessController1 controller;

	@Autowired
	private MessageSource messageSource;

	@Test
	void week11BeansAreLoaded() {
		assertThat(controller).isNotNull();
		assertThat(messageSource).isNotNull();
	}

	@Test
	void messageSourceResolvesKoreanGreeting() {
		String greeting = messageSource.getMessage("greeting", null, Locale.KOREAN);
		assertThat(greeting).contains("안녕하세요");
	}

	@Test
	void messageSourceResolvesEnglishGreeting() {
		String greeting = messageSource.getMessage("greeting", null, Locale.ENGLISH);
		assertThat(greeting).isEqualTo("Hello");
	}

	@Test
	void initCommandSetsPlaceholders() {
		RegisterRequest request = controller.initCommand(null);
		assertThat(request.getName()).contains("한글");
		assertThat(request.getEmail()).contains("이메일");
		assertThat(request.getPassword()).contains("영문자");
		assertThat(request.getConfirmPassword()).contains("암호");
	}

	@Test
	void registerRequestPasswordMatch() {
		RegisterRequest match = new RegisterRequest("a@b.com", "name", "pw", "pw");
		assertThat(match.isPasswordEqualToConfirmPassword()).isTrue();

		RegisterRequest mismatch = new RegisterRequest("a@b.com", "name", "pw", "different");
		assertThat(mismatch.isPasswordEqualToConfirmPassword()).isFalse();
	}

	@Test
	void questionIsChoiceFlagReflectsOptions() {
		Question textQ = new Question("자유 응답");
		assertThat(textQ.isChoice()).isFalse();

		Question choiceQ = new Question("선택형", java.util.List.of("A", "B"));
		assertThat(choiceQ.isChoice()).isTrue();
	}
}
