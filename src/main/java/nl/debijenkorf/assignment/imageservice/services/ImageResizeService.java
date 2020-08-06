package nl.debijenkorf.assignment.imageservice.services;

import java.awt.image.BufferedImage;

import nl.debijenkorf.assignment.imageservice.beans.ImageType;

public interface ImageResizeService {
	/**
	 * Resizes the given image to the sizes defined in ImageType object
	 * @param originalImage the image to be resized
	 * @param imageType parameter object
	 * @return resized image
	 */
	BufferedImage resizeImage(BufferedImage originalImage, ImageType imageType);

	/**
	 * Resizes the given image to the target sizes
	 * @param originalImage the image to be resized
	 * @param targetWidth target width
	 * @param targetHeight target height
	 * @param scaleType one of enum values of CROP, FILL, SKEW
	 * @param color Color code in hex format. The background color of the image if scale type is given as FILL
	 * @return resized image
	 */
	BufferedImage resizeImage(BufferedImage originalImage, int targetWidth, int targetHeight, ImageType.ScaleType scaleType, String color);
}
