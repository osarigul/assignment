package nl.debijenkorf.assignment.imageservice.services;

import java.io.IOException;

import nl.debijenkorf.assignment.imageservice.beans.ImageType;

public interface ImageService {

	/**
	 * Requested image is checked in optimized format in S3.
	 * The file is returned if it is found.
	 * If it is not found, the image in original format is checked in S3.
	 * If it is found, the image is resized to the requested format and saved in S3
	 * and returned.
	 * If original version is not found then the file is downloaded from the configured.
	 * The file is resized and saved in both original and optimized formats. The optimized
	 * file is returned.
	 *
	 * @param imageType Image Type
	 * @param fileReference Original file path
	 * @return Optimized image
	 * @throws IOException
	 */
	byte[] retrieveImage(ImageType imageType, String fileReference) throws IOException;

	/**
	 * If type of the imageType is original then all versions of the file is deleted
	 * from S3. Otherwise only the given type of file is deleted.
	 *
	 * @param imageType Image Type
	 * @param fileReference Original file path
	 */
	void removeImage(ImageType imageType, String fileReference);
}
