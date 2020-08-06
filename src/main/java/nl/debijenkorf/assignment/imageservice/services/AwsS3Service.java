package nl.debijenkorf.assignment.imageservice.services;

import java.io.IOException;

import nl.debijenkorf.assignment.imageservice.beans.ImageType;

public interface AwsS3Service {

	/**
	 * Retrieves the file from an S3 bucket
	 * @param fileName path of the file object
	 * @return file object in byte array
	 * @throws IOException
	 */
	byte[] retrieveFile(String fileName) throws IOException;

	/**
	 * Checks if the given object exists in S3
	 * @param fileName path of the file object
	 * @return true if object exists
	 */
	boolean doesFileExist(String fileName);

	/**
	 * Saves the file to an S3 bucket
	 * @param fileName path of the file object
	 * @param data file object in byte array
	 * @param imageType
	 */
	void uploadFile(String fileName, byte[] data, ImageType.Type imageType);

	/**
	 * Removes the given file from an S3 bucket
	 * @param fileName path of the file object
	 */
	void deleteFile(String fileName);
}
