package com.travelbnb.repository;

import com.travelbnb.Entity.Country;
import com.travelbnb.Entity.Location;
import com.travelbnb.Entity.Property;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PropertyRepository extends JpaRepository<Property, Long> {

    @Query("Select p from Property p where p.name=:name and p.location=:location and p.country=:country")
    Optional<Property> findByNameAndLocationAndCountry(String name, Location location, Country country);

//    @Query("select p from Property p JOIN Location l on p.location=l.id JOIN country c on p.country=c.id WHERE l.name=:locationName or c.name=:locationName")
//    List<Property> searchProperty(@Param("locationName") String locationName);
    @Query("SELECT p FROM Property p WHERE p.location.name = :locationName OR p.country.name = :locationName")
    List<Property> searchProperty(@Param("locationName") String locationName);

}