package nl.debijenkorf.assignment.imageservice.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;

@SpringBootTest
class AwsS3ServiceImplTest {

	@Mock
	private AmazonS3 amazonS3;

	@InjectMocks
	AwsS3ServiceImpl awsS3Service;

	private String bucketName;

	private String fileName;

	@BeforeEach
	public void setUp() {
		fileName = "/file/name.jpg";
		bucketName = "test-bucket";
		awsS3Service.setBucketName(bucketName);
	}

	@Test
	void testDoesFileExist() {
		when(amazonS3.doesObjectExist(bucketName, fileName)).thenReturn(true);

		assertTrue(awsS3Service.doesFileExist(fileName));
	}

	@Test
	void testRetrieveFile() throws Exception {
		final S3Object s3Object = mock(S3Object.class);
		final S3ObjectInputStream s3ObjectInputStream = mock(S3ObjectInputStream.class);
		when(amazonS3.getObject(bucketName, fileName)).thenReturn(s3Object);
		when(s3Object.getObjectContent()).thenReturn(s3ObjectInputStream);
		when(s3ObjectInputStream.read(any())).thenReturn(-1);

		assertEquals(0, awsS3Service.retrieveFile(fileName).length);
	}

}