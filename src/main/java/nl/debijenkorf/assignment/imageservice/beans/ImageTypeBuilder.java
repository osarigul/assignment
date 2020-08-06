package nl.debijenkorf.assignment.imageservice.beans;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;

import nl.debijenkorf.assignment.imageservice.exceptions.ConfigurationException;

public class ImageTypeBuilder {

	@Autowired
	private Environment env;

	public ImageType getImageType(final String typeName) {
		final int height = getIntProperty(typeName + ".height");
		final int width = getIntProperty(typeName + ".width");
		final int quality = getIntProperty(typeName + ".quality");
		final ImageType.ScaleType scaleType = getScaleTypeProperty(typeName + ".scale-type");
		final String fillColor = getHexProperty(typeName + ".fill-color");
		final ImageType.Type type = getTypeProperty(typeName + ".type");

		return new ImageType(height, width, quality, scaleType, fillColor, type, typeName);
	}


	public int getIntProperty(final String name) {
		String value = env.getProperty(name);
		if (!StringUtils.isEmpty(value)) {
			int result;
			try {
				result = Integer.parseInt(value);
			} catch (NumberFormatException nfe) {
				throw new ConfigurationException("Invalid " + name + " property", nfe);
			}
			if (result < 1) {
				throw new ConfigurationException("Invalid " + name + " property value: " + result);
			}
			return result;
		} else {
			throw new ConfigurationException("Cannot find " + name + " property");
		}
	}

	public ImageType.ScaleType getScaleTypeProperty(final String name) {
		String value = env.getProperty(name);
		if (!StringUtils.isEmpty(value)) {
			return ImageType.ScaleType.valueOf(value.trim().toUpperCase());
		} else {
			throw new ConfigurationException("Cannot find " + name + " property");
		}
	}

	public String getHexProperty(final String name) {
		String value = env.getProperty(name);
		if (!StringUtils.isEmpty(value) && value.matches("#?[0-9A-Fa-f]{6}")) {
			return value.startsWith("#") ? value : "#".concat(value);
		}
		return null;
	}

	public ImageType.Type getTypeProperty(final String name) {
		String value = env.getProperty(name);
		if (!StringUtils.isEmpty(value)) {
			return ImageType.Type.valueOf(value.trim().toUpperCase());
		} else {
			throw new ConfigurationException("Cannot find " + name + " property");
		}
	}
}
