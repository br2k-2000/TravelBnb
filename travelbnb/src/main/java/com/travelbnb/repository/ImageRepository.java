package com.travelbnb.repository;

import com.travelbnb.Entity.Image;
import com.travelbnb.Entity.Property;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

public interface ImageRepository extends JpaRepository<Image, Long> {

    @Modifying
    @Transactional
    void deleteByProperty(Property property);

}