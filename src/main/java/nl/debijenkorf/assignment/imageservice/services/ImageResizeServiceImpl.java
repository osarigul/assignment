package nl.debijenkorf.assignment.imageservice.services;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import nl.debijenkorf.assignment.imageservice.beans.ImageType;
import nl.debijenkorf.assignment.imageservice.strategies.ScaleStrategy;

@Service
public class ImageResizeServiceImpl implements ImageResizeService {

	@Autowired
	Map<String, ScaleStrategy> scaleStrategies;

	@Override
	public BufferedImage resizeImage(BufferedImage originalImage, ImageType imageType) {
		return resizeImage(originalImage, imageType.getWidth(), imageType.getHeight(), imageType.getScaleType(), imageType.getFillColor());
	}

	@Override
	public BufferedImage resizeImage(BufferedImage originalImage, int targetWidth, int targetHeight, final ImageType.ScaleType scaleType, final String color) {

		BufferedImage outputImage = createBufferedImage(targetWidth, targetHeight);
		fillBackgroundColor(outputImage, targetWidth, targetHeight, color);

		final Image resultingImage = scaleStrategies.get(scaleType.name()+ScaleStrategy.class.getSimpleName()).getScaledImage(originalImage,targetWidth, targetHeight);

		outputImage.getGraphics().drawImage(resultingImage, 0, 0, null);
		return outputImage;

	}

	protected BufferedImage createBufferedImage(final int targetWidth, final int targetHeight) {
		return new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
	}

	/**
	 * Fills the background of the image with the given color.
	 * @param image
	 * @param targetWidth width of the image
	 * @param targetHeight height of the image
	 * @param color background color in hexadecimal format
	 */
	protected void fillBackgroundColor(BufferedImage image, int targetWidth, int targetHeight, String color) {
		final Graphics2D graphics2D = image.createGraphics();
		if (!StringUtils.isEmpty(color)) {
			graphics2D.setBackground(Color.decode(color));
		}
		graphics2D.clearRect(0,0,targetWidth, targetHeight);
	}

}
