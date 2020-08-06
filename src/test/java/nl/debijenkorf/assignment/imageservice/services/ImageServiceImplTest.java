package nl.debijenkorf.assignment.imageservice.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.boot.test.context.SpringBootTest;

import nl.debijenkorf.assignment.imageservice.beans.ImageType;
import nl.debijenkorf.assignment.imageservice.utils.S3FilePathResolver;

@SpringBootTest
class ImageServiceImplTest {

	@Mock
	private S3FilePathResolver s3FilePathResolver;

	@Mock
	private AwsS3Service awsS3Service;

	@Mock
	private ImageResizeService imageResizeService;

	@InjectMocks
	@Spy
	private ImageServiceImpl imageService;

	private ImageType imageType;

	final private String originalFileName = "originalFileName";
	final private String optimizedFilePath = "optimizedFilePath";
	final private String originalFilePath = "originalFilePath";

	@BeforeEach
	public void setUp() {
		imageType = new ImageType(200,200,75, ImageType.ScaleType.FILL, "#ff0000", ImageType.Type.JPG, "thumbnail");
		when(s3FilePathResolver.getS3Url("thumbnail",originalFileName)).thenReturn(optimizedFilePath);
	}

	@Test
	public void testOptimizedFileExistsInS3() throws Exception {
		final byte[] image = new byte[0];
		when(awsS3Service.doesFileExist(optimizedFilePath)).thenReturn(true);
		when(awsS3Service.retrieveFile(optimizedFilePath)).thenReturn(image);

		assertEquals(image, imageService.retrieveImage(imageType, originalFileName));
	}

	@Test
	public void testOnlyOriginalFileExistsInS3() throws Exception {
		final byte[] image = new byte[0];
		final byte[] optimizedImage = new byte[1];
		when(s3FilePathResolver.getS3Url(eq(null),eq(originalFileName))).thenReturn(originalFilePath);
		when(awsS3Service.doesFileExist(originalFilePath)).thenReturn(true);
		when(awsS3Service.retrieveFile(originalFilePath)).thenReturn(image);
		doReturn(optimizedImage).when(imageService).resizeAndOptimizeOriginalImage(image, imageType);

		assertEquals(optimizedImage, imageService.retrieveImage(imageType, originalFileName));
	}

	@Test
	public void testNoFileExistsInS3() throws Exception {
		final byte[] originalImage = new byte[0];
		final byte[] optimizedImage = new byte[1];
		doReturn(originalImage).when(imageService).downloadOriginalImageFile(originalFileName);
		doReturn(optimizedImage).when(imageService).resizeAndOptimizeOriginalImage(originalImage, imageType);
		imageService.retrieveImage(imageType, originalFileName);

		assertEquals(optimizedImage, imageService.retrieveImage(imageType, originalFileName));
	}
}