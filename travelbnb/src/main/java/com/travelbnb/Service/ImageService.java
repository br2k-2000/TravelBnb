package com.travelbnb.Service;

import com.travelbnb.payload.ImageDto;
import org.springframework.web.multipart.MultipartFile;

public interface ImageService {

    ImageDto uploadImage(MultipartFile file, long propertyId, String bucketName);
}
