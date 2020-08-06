package nl.debijenkorf.assignment.imageservice.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import nl.debijenkorf.assignment.imageservice.beans.ImageType;
import nl.debijenkorf.assignment.imageservice.beans.ImageTypeBuilder;
import nl.debijenkorf.assignment.imageservice.services.ImageService;

@RestController
public class ImageController {

	@Autowired
	private ImageTypeBuilder imageTypeBuilder;

	@Autowired
	private ImageService imageService;

	@GetMapping(value = "/image/show/{type-name}/*/", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	@ResponseBody
	public ResponseEntity<byte[]> serveImage(@PathVariable("type-name") final String typeName, @RequestParam final String reference) throws Exception {

		ImageType imageType = imageTypeBuilder.getImageType(typeName);

		byte[] byteArray = imageService.retrieveImage(imageType, reference);

		if (byteArray != null && byteArray.length > 0) {
			return ResponseEntity
					.status(HttpStatus.OK)
					.contentType(imageType.getType().equals(ImageType.Type.JPG) ? MediaType.IMAGE_JPEG : MediaType.IMAGE_PNG)
					.body(byteArray);
		} else {
			return ResponseEntity
					.status(HttpStatus.NOT_FOUND)
					.build();
		}
	}

	@DeleteMapping(value = "/image/flush/{type-name}/")
	public ResponseEntity flushImages(@PathVariable("type-name") final String typeName, @RequestParam final String reference) throws Exception {

		ImageType imageType = imageTypeBuilder.getImageType(typeName);

		imageService.removeImage(imageType, reference);

		return ResponseEntity
				.status(HttpStatus.OK)
				.build();
	}


}
