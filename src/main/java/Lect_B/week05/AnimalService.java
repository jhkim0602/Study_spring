package Lect_B.week05;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import lombok.Getter;

@Service
@Getter
public class AnimalService {

	@Autowired
	private List<Animal> anoAnimals;

	@Autowired
	@Qualifier("xmlAnimals")
	private List<Animal> xmlAnimals;

	@Autowired
	private Map<String, Animal> anoAnimalsMap;

	@Autowired
	@Qualifier("xmlAnimalsMap")
	private Map<String, Animal> xmlAnimalMap;
}
