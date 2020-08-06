package nl.debijenkorf.assignment.imageservice.services;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;

import nl.debijenkorf.assignment.imageservice.beans.ImageType;
import nl.debijenkorf.assignment.imageservice.exceptions.AwsS3Exception;

@Service
public class AwsS3ServiceImpl implements AwsS3Service {

	private static final Logger LOGGER = LoggerFactory.getLogger(AwsS3ServiceImpl.class);

	private String bucketName;

	@Autowired
	private AmazonS3 amazonS3;

	@Override
	public byte[] retrieveFile(final String fileName) throws IOException {
		byte[] fileContent = null;
		try (final S3Object s3Object = amazonS3.getObject(bucketName, fileName);
				final S3ObjectInputStream s3ObjectInputStream = s3Object.getObjectContent();
		) {
			fileContent = IOUtils.toByteArray(s3ObjectInputStream);
		} catch (IOException ex) {
			LOGGER.error("Cannot retrieve file ["+fileName+"] in bucket ["+bucketName+"] from Aws", ex);
			throw new AwsS3Exception(ex);
		}
		return fileContent;
	}

	@Override
	public boolean doesFileExist(final String fileName) {
		if (!StringUtils.isEmpty(fileName) && !StringUtils.isEmpty(bucketName)) {
			return amazonS3.doesObjectExist(bucketName, fileName);
		}
		return false;
	}

	@Override
	@Async
	public void uploadFile(final String fileName, final byte[] data, ImageType.Type imageType) {
		InputStream inputStream = new ByteArrayInputStream(data);
		final ObjectMetadata metadata = new ObjectMetadata();
		metadata.setContentType(imageType.equals(ImageType.Type.JPG) ? MediaType.IMAGE_JPEG_VALUE : MediaType.IMAGE_PNG_VALUE);
		try {
			amazonS3.putObject(bucketName, fileName, inputStream, metadata);
		} catch(Exception e) {
			LOGGER.error("Cannot upload file ["+fileName+"] in bucket ["+bucketName+"] to Aws", e);
		}
	}

	@Override
	public void deleteFile(final String fileName) {
		if (!StringUtils.isEmpty(fileName) && !StringUtils.isEmpty(bucketName)) {
			try {
				amazonS3.deleteObject(bucketName, fileName);
			} catch (Exception e) {
				LOGGER.warn("Error occurred while deleting file ["+fileName+"] in bucket ["+bucketName+"]", e);
			}
		}
	}

	@Value("${aws-s3-endpoint}")
	public void setBucketName(final String bucketName) {
		this.bucketName = bucketName;
	}
}
