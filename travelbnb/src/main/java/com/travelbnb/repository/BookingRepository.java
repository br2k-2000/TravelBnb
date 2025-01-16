package com.travelbnb.repository;

import com.travelbnb.Entity.Booking;
import com.travelbnb.Entity.Room;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    @Modifying
    @Transactional
    void deleteByRoom(Room room);
}