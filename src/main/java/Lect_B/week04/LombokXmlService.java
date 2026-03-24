package Lect_B.week04;

import lombok.Getter;

@Getter
public class LombokXmlService {

	private final SmsSender smsSender;
	private final long periodTime;

	public LombokXmlService(SmsSender smsSender, long periodTime) {
		this.smsSender = smsSender;
		this.periodTime = periodTime;
	}

	public String run() {
		return smsSender.send("010-3030-2026", "XML + Lombok 생성자 주입, periodTime=" + periodTime);
	}
}
