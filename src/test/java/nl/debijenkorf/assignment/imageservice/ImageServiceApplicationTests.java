package nl.debijenkorf.assignment.imageservice;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import nl.debijenkorf.assignment.imageservice.controllers.ImageController;

@SpringBootTest
class ImageServiceApplicationTests {

	@Autowired
	private ImageController imageController;

	@Test
	void contextLoads() {
		assertThat(imageController).isNotNull();
	}

}
