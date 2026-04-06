package Lect_B.week05;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component("workUnit")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class WorkUnit {

	private final String unitId = UUID.randomUUID().toString().substring(0, 8);
	private final LocalDateTime createdAt = LocalDateTime.now();

	public String getUnitId() {
		return unitId;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public String getDescription() {
		return "WorkUnit{id=%s, createdAt=%s}".formatted(unitId, createdAt);
	}

	@Override
	public String toString() {
		return getDescription();
	}
}
