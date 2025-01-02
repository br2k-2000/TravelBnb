package com.travelbnb.Service;

import com.travelbnb.Entity.AppUser;
import com.travelbnb.Entity.Room;
import com.travelbnb.payload.BookingDto;

import java.util.List;

public interface BookingService {

    BookingDto createBooking(long propertyId, AppUser user, BookingDto bookingDto, long roomId);


    List<Room> getAvailableRooms(long propertyId);
}
