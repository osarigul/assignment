package nl.debijenkorf.assignment.imageservice.exceptions;

public class ConfigurationException extends RuntimeException {
	public ConfigurationException(final String message, final Throwable throwable) {
		super(message, throwable);
	}

	public ConfigurationException(final String message) {
		super(message);
	}
}
