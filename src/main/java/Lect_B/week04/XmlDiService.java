package Lect_B.week04;

public class XmlDiService {

	private final SmsSender constructorSms;
	private final long constructorPeriodTime;
	private WorkUnit workUnit;
	private String msg;

	public XmlDiService(SmsSender constructorSms, long constructorPeriodTime) {
		this.constructorSms = constructorSms;
		this.constructorPeriodTime = constructorPeriodTime;
	}

	public SmsSender getConstructorSms() {
		return constructorSms;
	}

	public long getConstructorPeriodTime() {
		return constructorPeriodTime;
	}

	public WorkUnit getWorkUnit() {
		return workUnit;
	}

	public void setWorkUnit(WorkUnit workUnit) {
		this.workUnit = workUnit;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
}
