package nl.debijenkorf.assignment.imageservice.strategies;

import java.awt.Image;
import java.awt.image.BufferedImage;

import org.springframework.stereotype.Component;

@Component("CROPScaleStrategy")
public class CropScaleStrategy implements ScaleStrategy {
	/**
	 * Scales the image by the Crop strategy
	 * @param originalImage original image
	 * @param targetWidth target width
	 * @param targetHeight target height
	 * @return scaled image
	 */
	@Override
	public Image getScaledImage(final BufferedImage originalImage, final int targetWidth, final int targetHeight) {
		final int originalWidth = originalImage.getWidth();
		final int originalHeight = originalImage.getHeight();
		final double originalRatio = (double)originalWidth/originalHeight;
		final double targetRatio = (double)targetWidth/targetHeight;
		int width = 0;
		int height = 0;

		if (originalRatio > targetRatio) {
			width = (int)(targetHeight * (double)originalWidth / originalHeight);
			height = targetHeight;
		} else {
			width = targetWidth;
			height = (int)(targetWidth * (double)originalHeight / originalWidth);
		}

		return originalImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);
	}
}
