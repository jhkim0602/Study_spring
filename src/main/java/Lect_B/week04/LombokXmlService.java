package Lect_B.week04;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class LombokXmlService {

	private final SmsSender smsSender;
	private final long periodTime;

	public String run() {
		return smsSender.send("010-3030-2026", "XML + Lombok 생성자 주입, periodTime=" + periodTime);
	}
}
