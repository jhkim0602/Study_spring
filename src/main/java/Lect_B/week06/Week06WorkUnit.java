package Lect_B.week06;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component("week06WorkUnit")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class Week06WorkUnit {

	private final String unitId = UUID.randomUUID().toString().substring(0, 8);
	private final LocalDateTime createdAt = LocalDateTime.now();

	public String getUnitId() {
		return unitId;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	@Override
	public String toString() {
		return "Week06WorkUnit{id=%s, createdAt=%s}".formatted(unitId, createdAt);
	}
}
