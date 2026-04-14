package Lect_B.week07;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.lect8.Lect8Application;

@SpringBootTest(classes = Lect8Application.class)
class Week07ContextTests {

	@Autowired
	private Week07AopController controller;

	@Autowired
	private Week07AopService aopService;

	@Autowired
	private Week07XmlAopService xmlAopService;

	@Autowired
	private AopEventLog eventLog;

	@Test
	void week07BeansAreLoadedAndServiceIsProxied() {
		assertThat(controller).isNotNull();
		assertThat(aopService).isNotNull();
		assertThat(AopUtils.isAopProxy(aopService)).isTrue();
	}

	@Test
	void xmlAopServiceIsLoadedAndProxied() {
		assertThat(xmlAopService).isNotNull();
		assertThat(AopUtils.isAopProxy(xmlAopService)).isTrue();
	}

	@Test
	void beforeAdviceAllowsAdminRole() {
		eventLog.reset();

		String result = aopService.performSensitiveOperation("student", "ADMIN", "target", 3);
		List<String> events = eventLog.snapshot();

		assertThat(result).contains("민감한 작업 실행 완료");
		assertThat(events).anyMatch(event -> event.contains("[Before] 인증 성공"));
	}

	@Test
	void beforeAdviceRejectsNonAdminRole() {
		eventLog.reset();

		assertThatThrownBy(() -> aopService.performSensitiveOperation("student", "USER", "target", 3))
				.isInstanceOf(SecurityException.class)
				.hasMessageContaining("사용자가 인증되지 않았습니다");

		assertThat(eventLog.snapshot()).anyMatch(event -> event.contains("[Before] 인증 실패"));
	}

	@Test
	void afterAdvicesRecordSuccessAndFailure() {
		eventLog.reset();

		String result = aopService.placeOrder(100, 50);
		List<String> successEvents = eventLog.snapshot();

		assertThat(result).contains("[조건일치]");
		assertThat(successEvents).anyMatch(event -> event.contains("[AfterReturning]"));
		assertThat(successEvents).anyMatch(event -> event.contains("[After]"));

		eventLog.reset();

		assertThatThrownBy(() -> aopService.placeOrder(20, 50))
				.isInstanceOf(SecurityException.class)
				.hasMessageContaining("[조건불일치]");

		List<String> failureEvents = eventLog.snapshot();
		assertThat(failureEvents).anyMatch(event -> event.contains("[AfterThrowing]"));
		assertThat(failureEvents).anyMatch(event -> event.contains("[After]"));
	}

	@Test
	void aroundAdviceCanChangeArgumentsAndReturnValue() {
		eventLog.reset();

		String result = aopService.check("student", "admin");
		List<String> events = eventLog.snapshot();

		assertThat(result).contains("[Around 적용 결과]");
		assertThat(result).contains("접근 권한이 있습니다");
		assertThat(events).anyMatch(event -> event.contains("role 인자를 대문자로 변경"));
	}

	@Test
	void annotationPointcutRecordsAnnotatedMethod() {
		eventLog.reset();

		String result = aopService.annotationTarget("sample");
		List<String> events = eventLog.snapshot();

		assertThat(result).contains("@TraceAop 대상 메서드 실행");
		assertThat(events).anyMatch(event -> event.contains("[@annotation] @TraceAop 메서드 실행 전"));
		assertThat(events).anyMatch(event -> event.contains("[@annotation] @TraceAop 메서드 실행 후"));
	}

	@Test
	void xmlAopAdvicesRecordSuccessAndFailure() {
		eventLog.reset();

		String result = xmlAopService.processXmlOrder(100, 50);
		List<String> successEvents = eventLog.snapshot();

		assertThat(result).contains("[XML 조건일치]");
		assertThat(successEvents).anyMatch(event -> event.contains("[XML Before]"));
		assertThat(successEvents).anyMatch(event -> event.contains("[XML AfterReturning]"));
		assertThat(successEvents).anyMatch(event -> event.contains("[XML After]"));

		eventLog.reset();

		assertThatThrownBy(() -> xmlAopService.processXmlOrder(20, 50))
				.isInstanceOf(SecurityException.class)
				.hasMessageContaining("[XML 조건불일치]");

		List<String> failureEvents = eventLog.snapshot();
		assertThat(failureEvents).anyMatch(event -> event.contains("[XML Before]"));
		assertThat(failureEvents).anyMatch(event -> event.contains("[XML AfterThrowing]"));
		assertThat(failureEvents).anyMatch(event -> event.contains("[XML After]"));
	}

	@Test
	void xmlAroundAdviceCanChangeArgumentsAndReturnValue() {
		eventLog.reset();

		String result = xmlAopService.checkXmlPermission("student", "admin");
		List<String> events = eventLog.snapshot();

		assertThat(result).contains("[XML Around 적용 결과]");
		assertThat(result).contains("접근 권한이 있습니다");
		assertThat(events).anyMatch(event -> event.contains("[XML Around] role 인자를 대문자로 변경"));
	}
}
