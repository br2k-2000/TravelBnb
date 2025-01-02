package com.travelbnb.Service;

import com.travelbnb.payload.RoomDto;

public interface RoomService {

    RoomDto addRooms(long propertyId, RoomDto roomDto);


    RoomDto updateRooms(long roomId, long propertyId, RoomDto dto);

}
