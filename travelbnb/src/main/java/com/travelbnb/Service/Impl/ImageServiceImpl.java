package com.travelbnb.Service.Impl;

import com.travelbnb.Entity.Image;
import com.travelbnb.Entity.Property;
import com.travelbnb.Exception.ResourceNotFoundException;
import com.travelbnb.Service.BucketService;
import com.travelbnb.Service.ImageService;
import com.travelbnb.payload.ImageDto;
import com.travelbnb.repository.ImageRepository;
import com.travelbnb.repository.PropertyRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ImageServiceImpl implements ImageService {


    private ImageRepository imageRepository;

    private PropertyRepository propertyRepository;
    private BucketService bucketService;

    public ImageServiceImpl(ImageRepository imageRepository, PropertyRepository propertyRepository, BucketService bucketService) {
        this.imageRepository = imageRepository;
        this.propertyRepository = propertyRepository;
        this.bucketService = bucketService;
    }

    @Override
    public ImageDto uploadImage(MultipartFile file, long propertyId, String bucketName) {

        Property property = propertyRepository.findById(propertyId).orElseThrow(
                ()->new ResourceNotFoundException("Property not found with id: "+propertyId)
        );
        String imageUrl = bucketService.uploadFile(file, bucketName);

        Image image=new Image();
        image.setProperty(property);
        image.setImageUrl(imageUrl);

        Image save = imageRepository.save(image);
        ImageDto dto=new ImageDto();
        dto.setId(save.getId());
        dto.setImageUrl(save.getImageUrl());
        dto.setProperty(save.getProperty());
        return dto;

    }
}
