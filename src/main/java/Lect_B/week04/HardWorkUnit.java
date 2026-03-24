package Lect_B.week04;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class HardWorkUnit {

	private final SmsSender autoSms;
	private final WorkUnit workUnit;
	private String msg;

	@Autowired
	public HardWorkUnit(@Qualifier("configSms") SmsSender autoSms,
			@Qualifier("week04WorkUnit") WorkUnit workUnit) {
		this.autoSms = autoSms;
		this.workUnit = workUnit;
	}

	public SmsSender getAutoSms() {
		return autoSms;
	}

	public SmsSender getSms() {
		return autoSms;
	}

	public WorkUnit getWorkUnit() {
		return workUnit;
	}

	@Value("${message.greeting}")
	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getMsg() {
		return msg;
	}

	public String sendPracticeMessage() {
		return autoSms.send("010-2026-0404", msg);
	}
}
