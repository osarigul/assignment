package nl.debijenkorf.assignment.imageservice.utils;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class S3FilePathResolverTest {

	private S3FilePathResolver s3FilePathResolver = new S3FilePathResolver();

	@ParameterizedTest
	@CsvSource({",,''",
			",abcdefghij.png,''",
			"xyz,,''",
			"xyz,abcd.png,xyz/abcd.png",
			"xyz,abcdefgh.png,xyz/abcd/abcdefgh.png",
			"xyz,abcdefghij.png,xyz/abcd/efgh/abcdefghij.png",
			"xyz,abcdefghij,xyz/abcd/efgh/abcdefghij",
			"xyz,abc/de/fgh/ij.png,xyz/abc_/de_f/abc_de_fgh_ij.png",
	})
	public void testGetS3Url(final String typeName, final String fileName, final String expected) {
		final String result = s3FilePathResolver.getS3Url(typeName, fileName);
		assertEquals(expected, result);
	}
}