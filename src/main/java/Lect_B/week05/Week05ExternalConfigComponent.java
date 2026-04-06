package Lect_B.week05;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Component
@ConfigurationProperties(prefix = "week05.mail")
@Getter
@Setter
public class Week05ExternalConfigComponent {

	@Value("${week05.server.port}")
	private int serverPort;

	@Value("${week05.server.address}")
	private String serverAddress;

	private String host;
	private int port;
	private int timeoutSeconds;
	private Credentials credentials = new Credentials();
	private List<String> defaultRecipients = new ArrayList<>();

	@Getter
	@Setter
	public static class Credentials {
		private String username;
		private String password;
	}

	public String summary() {
		return "server=%s:%d, smtp=%s:%d, user=%s".formatted(
				serverAddress, serverPort, host, port, credentials.getUsername());
	}
}
