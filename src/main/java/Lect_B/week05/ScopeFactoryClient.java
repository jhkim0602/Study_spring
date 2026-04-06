package Lect_B.week05;

import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class ScopeFactoryClient {

	private final ObjectFactory<WorkUnit> workUnitFactory;

	public ScopeFactoryClient(@Qualifier("workUnit") ObjectFactory<WorkUnit> workUnitFactory) {
		this.workUnitFactory = workUnitFactory;
	}

	public WorkUnit createWorkUnit() {
		return workUnitFactory.getObject();
	}
}
