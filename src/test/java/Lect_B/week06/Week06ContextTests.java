package Lect_B.week06;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.context.WebApplicationContext;

import com.example.lect8.Lect8Application;

@SpringBootTest(classes = Lect8Application.class)
class Week06ContextTests {

	@Autowired
	private BeanScopeController controller;

	@Autowired
	private ExternalConfigComponent externalConfigComponent;

	@Autowired
	private Week06ObjectFactoryClient objectFactoryClient;

	@Autowired
	private WebApplicationContext context;

	@Test
	void week06BeansAreLoaded() {
		assertThat(controller).isNotNull();
		assertThat(externalConfigComponent.getServerPort()).isEqualTo("8080");
		assertThat(externalConfigComponent.getUrl()).contains("lect_b_week06");
		assertThat(objectFactoryClient.createWorkUnit().getUnitId()).isNotBlank();
		assertThat(context.containsBean("week06ScopeBean0")).isTrue();
		assertThat(context.containsBean("week06ScopeBean3")).isTrue();
	}
}
