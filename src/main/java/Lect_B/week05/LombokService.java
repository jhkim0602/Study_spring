package Lect_B.week05;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@Getter
@Setter
public class LombokService {

	private final SmsSender sms;
	private final long periodTime;
	private String msg;
	private WorkUnit unit;

	public String run() {
		String unitId = unit != null ? unit.getUnitId() : "none";
		return sms.send("010-0505-1002",
				msg + " / unit=" + unitId + " / periodTime=" + periodTime);
	}
}
