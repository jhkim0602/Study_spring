package Lect_B.week03;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class JavaConfigMain {

	public static void main(String[] args) {
		AnnotationConfigApplicationContext context =
				new AnnotationConfigApplicationContext(AppConfig.class);

		SmsSender smsSender = context.getBean("configSms", SmsSender.class);

		System.out.println("가져온 빈 이름: configSms");
		System.out.println("가져온 빈 타입: " + smsSender.getClass().getName());
		System.out.println("빈 내부 값: " + smsSender.getSenderName());
		System.out.println(smsSender.send("010-1234-5678", "스프링 컨테이너 실습 중"));

		context.close();
	}
}
