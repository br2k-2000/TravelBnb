package com.travelbnb.repository;

import com.travelbnb.Entity.Location;
import com.travelbnb.payload.LocationDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {

    Optional<Location> findByName(String name);

}