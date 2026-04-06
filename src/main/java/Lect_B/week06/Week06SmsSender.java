package Lect_B.week06;

import java.util.UUID;

public class Week06SmsSender {

	private final String label;
	private final String instanceId;

	public Week06SmsSender(String label) {
		this.label = label;
		this.instanceId = UUID.randomUUID().toString().substring(0, 8);
	}

	public String getLabel() {
		return label;
	}

	public String getInstanceId() {
		return instanceId;
	}

	@Override
	public String toString() {
		return label + "(" + instanceId + ")";
	}
}
