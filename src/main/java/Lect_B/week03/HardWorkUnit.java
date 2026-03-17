package Lect_B.week03;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class HardWorkUnit {

	@Autowired
	@Qualifier("configSms") // AppConfig 클래스의 configSms 메서드에 의해 생성된 빈
	private SmsSender autoSms;
	private WorkUnit workUnit;
	private String msg;

	@Autowired
	public HardWorkUnit(WorkUnit workUnit) {
		this.workUnit = workUnit;
	}

	public SmsSender getAutoSms() {
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
}
