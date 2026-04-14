package Lect_B.week07;

import java.util.Arrays;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;

public class Week07XmlAdvice {

	private final AopEventLog eventLog;

	public Week07XmlAdvice(AopEventLog eventLog) {
		this.eventLog = eventLog;
	}

	public void beforeXml(JoinPoint joinPoint) {
		eventLog.add("[XML Before] 대상 메서드 실행 전: " + joinPoint.getSignature().getName());
		eventLog.add("[XML Before] 전달 인자: " + Arrays.toString(joinPoint.getArgs()));
	}

	public void afterReturningXml(JoinPoint joinPoint, Object result) {
		eventLog.add("[XML AfterReturning] 정상 종료 메서드: " + joinPoint.getSignature().getName());
		eventLog.add("[XML AfterReturning] 반환값: " + result);
	}

	public void afterThrowingXml(JoinPoint joinPoint, Throwable ex) {
		eventLog.add("[XML AfterThrowing] 예외 발생 메서드: " + joinPoint.getSignature().getName());
		eventLog.add("[XML AfterThrowing] 예외 메시지: " + ex.getMessage());
	}

	public void afterXml(JoinPoint joinPoint) {
		eventLog.add("[XML After] 정상/예외 여부와 관계없이 실행 후 처리: "
				+ joinPoint.getSignature().getName());
	}

	public Object aroundXml(ProceedingJoinPoint joinPoint) throws Throwable {
		long startTime = System.currentTimeMillis();
		Object[] args = joinPoint.getArgs();

		eventLog.add("[XML Around] checkXmlPermission 메서드 실행 전");
		eventLog.add("[XML Around] 원본 인자: " + Arrays.toString(args));

		if (args.length > 1 && args[1] instanceof String role) {
			args[1] = role.toUpperCase();
			eventLog.add("[XML Around] role 인자를 대문자로 변경: " + args[1]);
		}

		Object result = joinPoint.proceed(args);

		long endTime = System.currentTimeMillis();
		eventLog.add("[XML Around] 대상 메서드 실행 시간: " + (endTime - startTime) + "ms");

		return "[XML Around 적용 결과] " + result;
	}
}
