package com.travelbnb.repository;

import com.travelbnb.Entity.Property;
import com.travelbnb.Entity.Room;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RoomRepository extends JpaRepository<Room, Long> {

    List<Room> findByPropertyIdAndStatus(Long propertyId,boolean status);

    Optional<Room> findByPropertyAndRoomNumber(Property property, Integer roomNumber);

    void findByStatus(boolean b);

    @Modifying
    @Transactional
    void deleteByProperty(Property property);

    List<Room> findByProperty(Property property);
}