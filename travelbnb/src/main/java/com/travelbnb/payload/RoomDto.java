package com.travelbnb.payload;

import com.travelbnb.Entity.Property;
import lombok.Data;

@Data
public class RoomDto {

    private Long id;
    private Integer roomNumber;
    private boolean status; // false means available, true means booked
    private Property property;
}
