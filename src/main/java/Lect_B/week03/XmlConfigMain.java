package Lect_B.week03;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class XmlConfigMain {

	public static void main(String[] args) {
		ClassPathXmlApplicationContext context =
				new ClassPathXmlApplicationContext("xml/beans.xml");

		SmsSender smsSender = context.getBean("smsSender", SmsSender.class);

		System.out.println("가져온 빈 이름: smsSender");
		System.out.println("가져온 빈 타입: " + smsSender.getClass().getName());
		System.out.println("빈 내부 값: " + smsSender.getSenderName());
		System.out.println(smsSender.send("010-9876-5432", "XML 기반 스프링 컨테이너 실습 중"));

		context.close();
	}
}
