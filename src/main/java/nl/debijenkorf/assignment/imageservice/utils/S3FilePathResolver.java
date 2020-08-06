package nl.debijenkorf.assignment.imageservice.utils;

import org.springframework.util.StringUtils;

public class S3FilePathResolver {

	private static final String EMPTY = "";
	private static final String PATH_SEPERATOR = "/";
	private static final String DOT = ".";
	private static final String FWD_SLASH = "/";
	private static final String UNDERSCORE = "_";

	/**
	 * Creates the file path for S3 bucket.
	 * @param typeName Predefined type name
	 * @param originalFileName original image file name
	 * @return S3 directory of the image file
	 */
	public String getS3Url(final String typeName, final String originalFileName) {

		if (!StringUtils.isEmpty(typeName) && !StringUtils.isEmpty(originalFileName)) {

			String fileName = originalFileName.replaceAll(FWD_SLASH, UNDERSCORE);

			String fileExtension = getFileExtension(originalFileName);
			if (fileExtension.length() > 0) {
				fileName = fileName.substring(0, fileName.lastIndexOf(DOT));
			}

			StringBuilder sb = new StringBuilder();
			sb.append(typeName);
			sb.append(PATH_SEPERATOR);
			if (fileName.length() > 4) {
				sb.append(fileName, 0, 4);
				sb.append(PATH_SEPERATOR);
			}
			if (fileName.length() > 8) {
				sb.append(fileName, 4,8);
				sb.append(PATH_SEPERATOR);
			}
			sb.append(fileName).append(fileExtension);
			return sb.toString();
		}
		return EMPTY;
	}

	private String getFileExtension(final String fileName) {
		if (fileName.contains(DOT)) {
			return fileName.substring(fileName.lastIndexOf(DOT));
		}
		return EMPTY;
	}
}
