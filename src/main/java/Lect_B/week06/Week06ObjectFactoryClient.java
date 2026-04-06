package Lect_B.week06;

import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class Week06ObjectFactoryClient {

	private final ObjectFactory<Week06WorkUnit> workUnitFactory;

	public Week06ObjectFactoryClient(
			@Qualifier("week06WorkUnit") ObjectFactory<Week06WorkUnit> workUnitFactory) {
		this.workUnitFactory = workUnitFactory;
	}

	public Week06WorkUnit createWorkUnit() {
		return workUnitFactory.getObject();
	}
}
