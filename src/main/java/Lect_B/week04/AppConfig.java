package Lect_B.week04;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration // 빈 설정 클래스로 사용하기 위한 어노테이션
public class AppConfig {
	// 메서드를 빈으로 등록
	// 빈의 이름은 메서드 이름으로 설정
	// 생성된 빈의 타입은 메서드의 반환 타입
	@Bean 
	public SmsSender configSms() {
		return new SmsSender();
	}
	
	@Bean
	public List<String> unit() {
		List<String> list = new ArrayList<>();
		
		list.add("문자열 1");
		list.add("문자열 2");
		
		return list;
	}
}
