package nl.debijenkorf.assignment.imageservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

import nl.debijenkorf.assignment.imageservice.beans.ImageTypeBuilder;
import nl.debijenkorf.assignment.imageservice.utils.S3FilePathResolver;

@Configuration
public class Config {

	@Value("${aws-accesskey}")
	private String accesKey;

	@Value("${aws-secretkey}")
	private String secretKey;

	@Bean
	public AmazonS3 getAwsS3Client() {
		final BasicAWSCredentials basicAWSCredentials = new BasicAWSCredentials(accesKey, secretKey);
		final AWSStaticCredentialsProvider awsStaticCredentialsProvider = new AWSStaticCredentialsProvider(basicAWSCredentials);
		return AmazonS3ClientBuilder //
				.standard() //
				.withCredentials(awsStaticCredentialsProvider)
				.build();
	}

	@Bean
	public ImageTypeBuilder imageTypeBuilder() {
		return new ImageTypeBuilder();
	}

	@Bean
	public S3FilePathResolver s3FilePathResolver() {
		return new S3FilePathResolver();
	}
}
