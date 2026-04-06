package Lect_B.week05;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import lombok.Getter;

@Component
@Getter
public class AnimalAutoDI {

	// 동일 타입 빈이 여러 개일 때 필드명이 cat 이라서 이름 기준으로 Cat 빈이 연결된다.
	@Autowired
	private Animal cat;

	private final SmsSender sms;
	private Animal dog;

	public AnimalAutoDI(@Qualifier("xmlSms") SmsSender sms) {
		this.sms = sms;
	}

	@Autowired
	public void setDog(@Qualifier("dog") Animal dog) {
		this.dog = dog;
	}
}
