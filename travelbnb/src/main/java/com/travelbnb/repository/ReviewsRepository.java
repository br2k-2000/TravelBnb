package com.travelbnb.repository;

import com.travelbnb.Entity.AppUser;
import com.travelbnb.Entity.Property;
import com.travelbnb.Entity.Reviews;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ReviewsRepository extends JpaRepository<Reviews, Long> {

    @Query("Select r from Reviews r where r.appUser=:user and r.property=:property")
    Reviews findReviewsByUser(@Param("user") AppUser appUser, @Param("property") Property property);


    @Query("Select r from Reviews r where r.appUser=:user")
    List<Reviews> reviewsByUser(@Param("user") AppUser appUser);

    @Query("Select r from Reviews r where r.appUser=:user and r.property=:property")
    Optional<Reviews> findReviewsByUserAndProperty(@Param("user") AppUser appUser, @Param("property") Property property);

}