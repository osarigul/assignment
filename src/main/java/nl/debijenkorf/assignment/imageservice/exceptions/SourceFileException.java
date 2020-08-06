package nl.debijenkorf.assignment.imageservice.exceptions;

public class SourceFileException extends RuntimeException {
	public SourceFileException(final String message, final Throwable throwable) {
		super(message, throwable);
	}

	public SourceFileException(final String message) {
		super(message);
	}

	public SourceFileException(final Throwable throwable) {
		super(throwable);
	}
}
