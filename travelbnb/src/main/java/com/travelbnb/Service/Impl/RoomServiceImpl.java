package com.travelbnb.Service.Impl;

import com.travelbnb.Entity.Property;
import com.travelbnb.Entity.Room;
import com.travelbnb.Exception.ConcurrencyException;
import com.travelbnb.Exception.ResourceNotFoundException;
import com.travelbnb.Service.RoomService;
import com.travelbnb.payload.RoomDto;
import com.travelbnb.repository.PropertyRepository;
import com.travelbnb.repository.RoomRepository;
import jakarta.persistence.OptimisticLockException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RoomServiceImpl  implements RoomService {

    private RoomRepository roomRepository;
    private PropertyRepository propertyRepository;

    public RoomServiceImpl(RoomRepository roomRepository, PropertyRepository propertyRepository) {
        this.roomRepository = roomRepository;
        this.propertyRepository = propertyRepository;
    }

    @Override
    public RoomDto addRooms(long propertyId, RoomDto roomDto) {
        //get the property details
        Property property = propertyRepository.findById(propertyId).orElseThrow(
                ()->new ResourceNotFoundException("property not found with id: "+propertyId)
        );
        roomDto.setProperty(property);
        Room room = dtoToEntity(roomDto);

        Optional<Room> existingRoom  = roomRepository.findByPropertyAndRoomNumber(property, room.getRoomNumber());
        if (existingRoom.isPresent()){
            throw new ResourceNotFoundException("Room already exists with the same properties.");
        }
        Room save = roomRepository.save(room);
        RoomDto dto2 = entityToDto(save);
        return dto2;

    }

    @Transactional
    @Override
    public RoomDto updateRooms(long roomId, long propertyId, RoomDto dto) {
        try {
            // Fetch existing room
            Room existingRoom = roomRepository.findById(roomId).orElseThrow(
                    () -> new ResourceNotFoundException("Room not found with id: " + roomId)
            );

            // Optionally update property only if it changes
            if (propertyId != existingRoom.getProperty().getId()) {
                Property property = propertyRepository.findById(propertyId).orElseThrow(
                        () -> new ResourceNotFoundException("Property not found with id: " + propertyId)
                );
                existingRoom.setProperty(property);
            }

            // Update the existing room entity with new values from the DTO
            existingRoom.setRoomNumber(dto.getRoomNumber());
            existingRoom.setStatus(dto.isStatus());

            // No need to explicitly save the room if already managed by persistence context
            RoomDto responseDto = entityToDto(existingRoom);
            return responseDto;

        } catch (OptimisticLockException e) {
            // Custom exception for concurrency issues
            throw new ConcurrencyException("The room was updated by another transaction. Please try again.");
        }
    }


    //dto to entity
    Room dtoToEntity(RoomDto roomDto){
        Room room=new Room();
        room.setRoomNumber(roomDto.getRoomNumber());
        room.setStatus(roomDto.isStatus());
        room.setProperty(roomDto.getProperty());
        return room;

    }
    //entity to dto
    RoomDto entityToDto(Room room){
        RoomDto dto=new RoomDto();
        dto.setId(room.getId());
        dto.setRoomNumber(room.getRoomNumber());
        dto.setStatus(room.isStatus());
        dto.setProperty(room.getProperty());
        return dto;
    }
}
