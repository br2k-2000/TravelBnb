package com.travelbnb.Controller;

import com.travelbnb.Service.RoomService;
import com.travelbnb.payload.RoomDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/room")
public class RoomController {

    private RoomService roomService;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @PostMapping("/addRooms")
    public ResponseEntity<RoomDto> addRooms(@RequestParam long propertyId,
                                            @RequestBody RoomDto roomDto){
        RoomDto dto = roomService.addRooms(propertyId, roomDto);
        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }

    @PutMapping("/{roomId}")
    public ResponseEntity<RoomDto> updateRooms( @PathVariable long roomId,
                                                @RequestParam long propertyId,
                                                @RequestBody RoomDto dto){
        RoomDto dto1 = roomService.updateRooms(roomId, propertyId, dto);
        return new ResponseEntity<>(dto1,HttpStatus.OK);
    }

}
