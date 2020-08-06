package nl.debijenkorf.assignment.imageservice.strategies;

import java.awt.Image;
import java.awt.image.BufferedImage;

public interface ScaleStrategy {
	Image getScaledImage(BufferedImage originalImage, int targetWidth, int targetHeight);
}
