package nl.debijenkorf.assignment.imageservice.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import nl.debijenkorf.assignment.imageservice.beans.ImageType;
import nl.debijenkorf.assignment.imageservice.beans.ImageTypeBuilder;
import nl.debijenkorf.assignment.imageservice.services.ImageService;

@SpringBootTest
@AutoConfigureMockMvc
class ImageControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	ImageService imageService;

	@MockBean
	private ImageTypeBuilder imageTypeBuilder;

	@Test
	public void testServeImage_404() throws Exception {
		this.mockMvc.perform(get("/image/show/thumbnail/some-seo-text/?reference=123.jpg"))
				.andDo(print())
				.andExpect(status().is4xxClientError());
	}

	@Test
	public void testServeImage_200() throws Exception {
		final ImageType imageType = mock(ImageType.class);
		when(imageType.getType()).thenReturn(ImageType.Type.JPG);
		when(imageTypeBuilder.getImageType(anyString())).thenReturn(imageType);
		when(imageService.retrieveImage(imageType, "123.jpg")).thenReturn(new byte[1]);

		this.mockMvc.perform(get("/image/show/thumbnail/some-seo-text/?reference=123.jpg"))
				.andDo(print())
				.andExpect(content().contentType(MediaType.IMAGE_JPEG))
				.andExpect(status().isOk());
	}

	@Test
	public void testFlushImages() throws Exception {
		final ImageType imageType = mock(ImageType.class);
		when(imageTypeBuilder.getImageType(anyString())).thenReturn(imageType);

		this.mockMvc.perform(delete("/image/flush/thumbnail/?reference=123.jpg"))
				.andDo(print())
				.andExpect(status().isOk());

	}
}