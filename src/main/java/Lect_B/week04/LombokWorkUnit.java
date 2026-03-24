package Lect_B.week04;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Component
@Getter
@RequiredArgsConstructor
public class LombokWorkUnit {

	private final SmsSender configSms;
	private final WorkUnit week04WorkUnit;

	@Value("${message.greeting}")
	private String msg;

	public String sendPracticeMessage() {
		return configSms.send("010-4040-2026", msg);
	}
}
