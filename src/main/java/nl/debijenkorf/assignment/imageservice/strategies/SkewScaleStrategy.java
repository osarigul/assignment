package nl.debijenkorf.assignment.imageservice.strategies;

import java.awt.Image;
import java.awt.image.BufferedImage;

import org.springframework.stereotype.Component;

@Component("SKEWScaleStrategy")
public class SkewScaleStrategy implements ScaleStrategy {
	/**
	 * Scales the image by the Skew strategy
	 * @param originalImage original image
	 * @param targetWidth target width
	 * @param targetHeight target height
	 * @return scaled image
	 */
	@Override
	public Image getScaledImage(final BufferedImage originalImage, final int targetWidth, final int targetHeight) {
		return originalImage.getScaledInstance(targetWidth, targetHeight, Image.SCALE_SMOOTH);
	}
}
