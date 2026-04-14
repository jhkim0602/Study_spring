package Lect_B.week07;

import org.springframework.stereotype.Service;

@Service
public class Week07AopService {

	public String performSensitiveOperation(String userId, String role, String message, int count) {
		return "민감한 작업 실행 완료: userId=" + userId + ", role=" + role
				+ ", message=" + message + ", count=" + count;
	}

	public String placeOrder(double orderValue, double minimumValue) {
		if (orderValue < minimumValue) {
			throw new SecurityException(orderValue + " < " + minimumValue + " [조건불일치]");
		}

		return orderValue + " >= " + minimumValue + " [조건일치]";
	}

	public String check(String userId, String role) {
		if (!"ADMIN".equals(role)) {
			return userId + " 사용자는 접근 권한이 없습니다. role=" + role;
		}

		return userId + " 사용자는 접근 권한이 있습니다. role=" + role;
	}

	@TraceAop
	public String annotationTarget(String label) {
		return "@TraceAop 대상 메서드 실행: " + label;
	}
}
