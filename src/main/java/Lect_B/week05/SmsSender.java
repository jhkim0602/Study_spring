package Lect_B.week05;

import java.util.UUID;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Component("smsSender")
@Primary
public class SmsSender {

	private final String senderName;
	private final String instanceId;

	public SmsSender() {
		this("5주차 기본 SMS 발신기");
	}

	public SmsSender(String senderName) {
		this.senderName = senderName;
		this.instanceId = UUID.randomUUID().toString().substring(0, 8);
	}

	public String getSenderName() {
		return senderName;
	}

	public String getInstanceId() {
		return instanceId;
	}

	public String send(String phoneNumber, String message) {
		return senderName + " [" + instanceId + "] -> " + phoneNumber + " : " + message;
	}

	@Override
	public String toString() {
		return senderName + "(" + instanceId + ")";
	}
}
