package Lect_B.week06;

import org.springframework.stereotype.Component;

@Component
public class Week06DifferentScopeClient {

	private final Week06WorkUnit workUnit;

	public Week06DifferentScopeClient(Week06WorkUnit workUnit) {
		this.workUnit = workUnit;
	}

	public Week06WorkUnit getWorkUnit() {
		return workUnit;
	}
}
