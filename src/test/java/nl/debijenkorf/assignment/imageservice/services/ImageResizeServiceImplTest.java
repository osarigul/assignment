package nl.debijenkorf.assignment.imageservice.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.boot.test.context.SpringBootTest;

import nl.debijenkorf.assignment.imageservice.beans.ImageType;
import nl.debijenkorf.assignment.imageservice.strategies.FillScaleStrategy;
import nl.debijenkorf.assignment.imageservice.strategies.ScaleStrategy;

@SpringBootTest
class ImageResizeServiceImplTest {

	@Mock
	Map<String, ScaleStrategy> mockedScaleStrategies;

	@InjectMocks
	@Spy
	ImageResizeServiceImpl imageResizeService;

	@Test
	void testResizeImage() {
		final ImageType imageType = new ImageType(200,200,75, ImageType.ScaleType.FILL, "#ff0000", ImageType.Type.JPG, "thumbnail");
		final BufferedImage originalImage = mock(BufferedImage.class);
		final ScaleStrategy fillScaleStrategy = mock(FillScaleStrategy.class);
		final Image resultingImage = mock(Image.class);
		final BufferedImage outputImage = mock(BufferedImage.class);

		when(mockedScaleStrategies.get("FILLScaleStrategy")).thenReturn(fillScaleStrategy);
		when(fillScaleStrategy.getScaledImage(originalImage, 200, 200)).thenReturn(resultingImage);
		doReturn(outputImage).when(imageResizeService).createBufferedImage(anyInt(), anyInt());
		when(outputImage.createGraphics()).thenReturn(mock(Graphics2D.class));
		when(outputImage.getGraphics()).thenReturn(mock(Graphics.class));

		assertEquals(outputImage, imageResizeService.resizeImage(originalImage, imageType));

	}
}