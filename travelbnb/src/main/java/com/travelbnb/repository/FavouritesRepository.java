package com.travelbnb.repository;

import com.travelbnb.Entity.AppUser;
import com.travelbnb.Entity.Favourites;
import com.travelbnb.Entity.Property;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FavouritesRepository extends JpaRepository<Favourites, Long> {

//    @Query("SELECT f FROM Favourite f WHERE f.appUser = :user AND f.property = :property")
//    Optional<Favourites> findByUserAndProperty(@Param("user") AppUser appUser, @Param("property") Property property);

    @Query("SELECT f FROM Favourites f WHERE f.appUser = :appUser AND f.property = :property")
    Optional<Favourites> findByUserAndProperty(@Param("appUser") AppUser appUser, @Param("property") Property property);

    @Query("Select f from Favourites f where f.appUser=:user")
    List<Favourites> findFavouriteByUser(@Param("user") AppUser appUser);

    @Modifying
    @Transactional
    void deleteByProperty(Property property);
}