package Lect_B.week06;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Component
@ConfigurationProperties(prefix = "week06.datasource")
@Getter
@Setter
public class ExternalConfigComponent {

	@Value("${week06.server.port}")
	private String serverPort;

	@Value("${week06.server.address}")
	private String serverAddress;

	@Value("${week06.message.greeting}")
	private String greeting;

	private String url;
	private String userName;
	private String password;
}
