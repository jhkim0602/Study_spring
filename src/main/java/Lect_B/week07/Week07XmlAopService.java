package Lect_B.week07;

public class Week07XmlAopService {

	public String processXmlOrder(double orderValue, double minimumValue) {
		if (orderValue < minimumValue) {
			throw new SecurityException(orderValue + " < " + minimumValue + " [XML 조건불일치]");
		}

		return orderValue + " >= " + minimumValue + " [XML 조건일치]";
	}

	public String checkXmlPermission(String userId, String role) {
		if (!"ADMIN".equals(role)) {
			return userId + " 사용자는 XML AOP 접근 권한이 없습니다. role=" + role;
		}

		return userId + " 사용자는 XML AOP 접근 권한이 있습니다. role=" + role;
	}
}
