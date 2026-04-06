package Lect_B.week05;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

@Component
@RequestScope
public class RequestTrace {

	private final String requestId = UUID.randomUUID().toString().substring(0, 8);
	private final LocalDateTime createdAt = LocalDateTime.now();

	public String getRequestId() {
		return requestId;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	@Override
	public String toString() {
		return "RequestTrace{id=%s, createdAt=%s}".formatted(requestId, createdAt);
	}
}
