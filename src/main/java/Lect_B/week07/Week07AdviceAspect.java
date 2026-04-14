package Lect_B.week07;

import java.util.Arrays;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class Week07AdviceAspect {

	private final AopEventLog eventLog;

	public Week07AdviceAspect(AopEventLog eventLog) {
		this.eventLog = eventLog;
	}

	@Pointcut("execution(* Lect_B.week07.Week07AopService.placeOrder(..))")
	private void orderOperation() {
	}

	@Before("execution(* Lect_B.week07.Week07AopService.performSensitiveOperation(..))")
	public void authenticate(JoinPoint joinPoint) {
		Object[] args = joinPoint.getArgs();
		String methodName = joinPoint.getSignature().getName();
		String role = args.length > 1 ? String.valueOf(args[1]) : "";

		eventLog.add("[Before] 메서드 실행 전 인증 검사: " + methodName);
		eventLog.add("[Before] 전달 인자: " + Arrays.toString(args));

		if (!"ADMIN".equalsIgnoreCase(role)) {
			eventLog.add("[Before] 인증 실패: ADMIN 권한이 필요함");
			throw new SecurityException("사용자가 인증되지 않았습니다. role=" + role);
		}

		eventLog.add("[Before] 인증 성공: 대상 메서드 실행 허용");
	}

	@AfterReturning(pointcut = "orderOperation()", returning = "result")
	public void logAfterReturning(Object result) {
		eventLog.add("[AfterReturning] 메서드가 정상적으로 종료됨");
		eventLog.add("[AfterReturning] 반환값: " + result);
	}

	@AfterThrowing(pointcut = "orderOperation()", throwing = "ex")
	public void exceptionProcess(Throwable ex) {
		eventLog.add("[AfterThrowing] 예외 발생: " + ex.getMessage());
	}

	@After("orderOperation()")
	public void logAfter() {
		eventLog.add("[After] 정상/예외 여부와 관계없이 실행 후 처리");
	}

	@Around("execution(* Lect_B.week07.Week07AopService.check(..))")
	public Object checkPermission(ProceedingJoinPoint joinPoint) throws Throwable {
		long startTime = System.currentTimeMillis();
		Object[] args = joinPoint.getArgs();

		eventLog.add("[Around] check 메서드 실행 전");
		eventLog.add("[Around] 원본 인자: " + Arrays.toString(args));

		if (args.length > 1 && args[1] instanceof String role) {
			args[1] = role.toUpperCase();
			eventLog.add("[Around] role 인자를 대문자로 변경: " + args[1]);
		}

		Object result = joinPoint.proceed(args);

		long endTime = System.currentTimeMillis();
		eventLog.add("[Around] 대상 메서드 실행 시간: " + (endTime - startTime) + "ms");

		return "[Around 적용 결과] " + result;
	}

	@Around("@annotation(Lect_B.week07.TraceAop)")
	public Object traceAnnotatedMethod(ProceedingJoinPoint joinPoint) throws Throwable {
		eventLog.add("[@annotation] @TraceAop 메서드 실행 전: " + joinPoint.getSignature().getName());
		Object result = joinPoint.proceed();
		eventLog.add("[@annotation] @TraceAop 메서드 실행 후: " + result);
		return result;
	}
}
