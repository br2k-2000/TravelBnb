package com.travelbnb.repository;

import com.travelbnb.Entity.Property;
import com.travelbnb.Entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RoomRepository extends JpaRepository<Room, Long> {

    List<Room> findByPropertyIdAndStatus(Long propertyId,boolean status);

    Optional<Room> findByPropertyAndRoomNumber(Property property, Integer roomNumber);

    void findByStatus(boolean b);
}