package Lect_B.week05;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

@Configuration
@ImportResource({
	"classpath:xml/week05-ex1.xml",
	"classpath:xml/week05-ex2.xml",
	"classpath:xml/week05-ex3.xml",
	"classpath:xml/week05-ex4.xml"
})
public class Week05XmlImportConfig {

}
