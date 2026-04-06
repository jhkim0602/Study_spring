package Lect_B.week05;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class ScopeSingletonClient {

	private final WorkUnit workUnit;

	public ScopeSingletonClient(@Qualifier("workUnit") WorkUnit workUnit) {
		this.workUnit = workUnit;
	}

	public WorkUnit getWorkUnit() {
		return workUnit;
	}

	public String currentUnitId() {
		return workUnit.getUnitId();
	}
}
