package Lect_B.week06;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.web.context.annotation.RequestScope;
import org.springframework.web.context.annotation.SessionScope;

@Configuration
public class BeanScopeConfig {

	@Bean(name = "week06ScopeBean0")
	public Week06SmsSender singletonBean() {
		return new Week06SmsSender("week06 singleton");
	}

	@Bean(name = "week06ScopeBean1")
	@Scope("prototype")
	public Week06SmsSender prototypeBean() {
		return new Week06SmsSender("week06 prototype");
	}

	@Bean(name = "week06ScopeBean2")
	@RequestScope
	public Week06SmsSender requestBean() {
		return new Week06SmsSender("week06 request");
	}

	@Bean(name = "week06ScopeBean3")
	@SessionScope
	public Week06SmsSender sessionBean() {
		return new Week06SmsSender("week06 session");
	}
}
