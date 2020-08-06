package nl.debijenkorf.assignment.imageservice.services;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import nl.debijenkorf.assignment.imageservice.beans.ImageType;
import nl.debijenkorf.assignment.imageservice.exceptions.ConversionException;
import nl.debijenkorf.assignment.imageservice.exceptions.SourceFileException;
import nl.debijenkorf.assignment.imageservice.utils.S3FilePathResolver;

@Service
public class ImageServiceImpl implements ImageService {

	private static final Logger LOGGER = LoggerFactory.getLogger(ImageServiceImpl.class);

	@Value("${original-type-name}")
	private String originalTypeName;

	@Value("${source-root-url}")
	private String sourceRootUrl;

	@Autowired
	private S3FilePathResolver s3FilePathResolver;

	@Autowired
	private AwsS3Service awsS3Service;

	@Autowired
	private ImageResizeService imageResizeService;


	@Override
	public byte[] retrieveImage(final ImageType imageType, final String fileReference) throws IOException {
		final String optimizedImageFilePathInS3 = s3FilePathResolver.getS3Url(imageType.getName(), fileReference);

		if (StringUtils.isEmpty(optimizedImageFilePathInS3)) {
			LOGGER.error("Could not resolve S3 file path");
			throw new FileNotFoundException();
		}

		if (awsS3Service.doesFileExist(optimizedImageFilePathInS3)) {
			return awsS3Service.retrieveFile(optimizedImageFilePathInS3);
		} else {
			final String originalImageFilePathInS3 = s3FilePathResolver.getS3Url(originalTypeName, fileReference);
			byte[] originalImageData;
			if (awsS3Service.doesFileExist(originalImageFilePathInS3)) {
				originalImageData = awsS3Service.retrieveFile(originalImageFilePathInS3);

			} else {
				originalImageData = downloadOriginalImageFile(fileReference);
				storeImageInS3(originalImageData, originalImageFilePathInS3, imageType.getType());
			}
			final byte[] optimizedImage = resizeAndOptimizeOriginalImage(originalImageData, imageType);
			storeImageInS3(optimizedImage, optimizedImageFilePathInS3, imageType.getType());
			return optimizedImage;
		}

	}

	/**
	 * Downloads file from the configured url
	 * @param fileReference original file path
	 * @return file
	 */
	protected byte[] downloadOriginalImageFile(final String fileReference) {
		if (!StringUtils.isEmpty(fileReference)) {
			try {
				URL url = new URL(sourceRootUrl + fileReference);
				byte[] fileContent = IOUtils.toByteArray(url);
				return fileContent;
			} catch (IOException e) {
				LOGGER.error("Source file could not be downloaded from " + sourceRootUrl + fileReference);
				throw new SourceFileException(e);
			}
		}
		return null;
	}

	/**
	 * Uploads the image file to given path
	 * @param originalImage
	 * @param originalImageFilePathInS3
	 * @param type
	 */
	protected void storeImageInS3(final byte[] originalImage, final String originalImageFilePathInS3, final ImageType.Type type) {
		if (originalImage != null && !StringUtils.isEmpty(originalImageFilePathInS3) && type != null) {
			awsS3Service.uploadFile(originalImageFilePathInS3, originalImage, type);
		}
	}

	/**
	 * Resizes the image to the given size with imageType parameter
	 * @param originalImageData
	 * @param imageType
	 * @return resized image
	 */
	protected byte[] resizeAndOptimizeOriginalImage(final byte[] originalImageData, final ImageType imageType) {
		if (originalImageData != null && imageType != null) {
			final BufferedImage bufferedImage = convertImageDataToImage(originalImageData);
			final BufferedImage resizedImage = imageResizeService.resizeImage(bufferedImage, imageType);
			return convertImageToImageData(resizedImage, imageType.getType().name().toLowerCase());
		}
		return null;
	}

	/**
	 * Converts the image data in byte array to a BufferedImage
	 * @param imageData
	 * @return BufferedImage
	 */
	protected BufferedImage convertImageDataToImage(final byte[] imageData) {
		if (imageData != null) {
			try {
				InputStream in = new ByteArrayInputStream(imageData);
				BufferedImage bufferedImage = ImageIO.read(in);
				return bufferedImage;
			} catch (IOException e) {
				LOGGER.error("Error while converting form image data to image");
				throw new ConversionException(e);
			}
		}
		return null;
	}

	/**
	 * Converts the image to a byte array
	 * @param bufferedImage
	 * @return image data in byte array
	 */
	protected byte[] convertImageToImageData(final BufferedImage bufferedImage, final String formatName) {
		if (bufferedImage != null) {
			try {
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				ImageIO.write(bufferedImage, formatName, baos);
				byte[] bytes = baos.toByteArray();
				return bytes;
			} catch (IOException e) {
				LOGGER.error("Error while converting form image to image data");
				throw new ConversionException(e);
			}
		}
		return null;
	}

	@Override
	public void removeImage(final ImageType imageType, final String fileReference) {
		List<String> files = getFileListToBeRemoved(imageType, fileReference);
		files.stream()
				.filter(file -> !StringUtils.isEmpty(file))
				.filter(file -> awsS3Service.doesFileExist(file))
				.forEach(file -> awsS3Service.deleteFile(file));

	}

	private List<String> getFileListToBeRemoved(final ImageType imageType, final String fileReference) {
		List<String> files = new ArrayList<>();
		if (imageType.getName().equals(originalTypeName)) {
			// find all applicable files in S3 to be removed
			// NOT IMPLEMENTED!
		} else {
			files.add(s3FilePathResolver.getS3Url(imageType.getName(), fileReference));
		}
		return files;
	}
}
