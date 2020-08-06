package nl.debijenkorf.assignment.imageservice.exceptions;

public class ConversionException extends RuntimeException {
	public ConversionException(final String message, final Throwable throwable) {
		super(message, throwable);
	}

	public ConversionException(final String message) {
		super(message);
	}

	public ConversionException(final Throwable throwable) {
		super(throwable);
	}
}
