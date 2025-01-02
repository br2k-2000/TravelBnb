package com.travelbnb.Controller;

import com.travelbnb.Service.ImageService;
import com.travelbnb.payload.ImageDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/image")
public class ImageController {

    private ImageService imageService;

    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @PostMapping(path = "/upload/file/{bucketName}/property/{propertyId}",consumes = MediaType.MULTIPART_FORM_DATA_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ImageDto> uploadImage(
            @RequestParam MultipartFile file,
            @PathVariable long propertyId,
            @PathVariable String bucketName
            //   @AuthenticationPrincipal AppUser user

    ){
        ImageDto savedImageEntity = imageService.uploadImage(file, propertyId, bucketName);
        return new ResponseEntity<>(savedImageEntity, HttpStatus.CREATED);
    }
}
