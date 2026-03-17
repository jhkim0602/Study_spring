package Lect_B.week03;

public class SmsSender {

	private final String senderName;

	public SmsSender() {
		this("기본 SMS 발신기");
	}

	public SmsSender(String senderName) {
		this.senderName = senderName;
	}

	public String getSenderName() {
		return senderName;
	}

	public String send(String phoneNumber, String message) {
		return senderName + " -> [" + phoneNumber + "] " + message;
	}
}
