package nl.debijenkorf.assignment.imageservice.exceptions;

public class AwsS3Exception extends RuntimeException {
	public AwsS3Exception(final String message, final Throwable throwable) {
		super(message, throwable);
	}

	public AwsS3Exception(final String message) {
		super(message);
	}

	public AwsS3Exception(final Throwable throwable) {
		super(throwable);
	}
}
