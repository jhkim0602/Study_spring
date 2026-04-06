package Lect_B.week05;

import org.springframework.stereotype.Component;

@Component
public class ScopeSingletonClient {

	private final WorkUnit workUnit;

	public ScopeSingletonClient(WorkUnit workUnit) {
		this.workUnit = workUnit;
	}

	public WorkUnit getWorkUnit() {
		return workUnit;
	}

	public String currentUnitId() {
		return workUnit.getUnitId();
	}
}
