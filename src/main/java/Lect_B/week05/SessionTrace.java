package Lect_B.week05;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

@Component
@SessionScope
public class SessionTrace {

	private final String sessionBeanId = UUID.randomUUID().toString().substring(0, 8);
	private final LocalDateTime createdAt = LocalDateTime.now();
	private int accessCount;

	public String getSessionBeanId() {
		return sessionBeanId;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public int getAccessCount() {
		return accessCount;
	}

	public int markAccess() {
		return ++accessCount;
	}

	@Override
	public String toString() {
		return "SessionTrace{id=%s, createdAt=%s, accessCount=%d}".formatted(
				sessionBeanId, createdAt, accessCount);
	}
}
