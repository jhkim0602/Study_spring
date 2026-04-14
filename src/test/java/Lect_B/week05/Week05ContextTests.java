package Lect_B.week05;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.lect8.Lect8Application;

@SpringBootTest(classes = Lect8Application.class)
class Week05ContextTests {

	@Autowired
	private Week05PracticeController controller;

	@Autowired
	private AnimalService animalService;

	@Autowired
	private Week05ExternalConfigComponent externalConfig;

	@Autowired
	private ScopeFactoryClient scopeFactoryClient;

	@Test
	void week05BeansAreLoaded() {
		assertThat(controller).isNotNull();
		assertThat(animalService.getAnoAnimals()).hasSize(2);
		assertThat(animalService.getXmlAnimals()).hasSize(2);
		assertThat(externalConfig.getHost()).isEqualTo("smtp.gmail.com");
		assertThat(scopeFactoryClient.createWorkUnit().getUnitId()).isNotBlank();
	}
}
