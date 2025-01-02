package com.travelbnb.payload;

import com.travelbnb.Entity.AppUser;
import com.travelbnb.Entity.Property;
import com.travelbnb.Entity.Room;
import lombok.Data;

@Data
public class BookingDto {

    private Long id;
    private String name;
    private String email;
    private String mobile;
    private Integer price;
    private Integer totalNights;
    private AppUser appUser;
    private Property property;
    private Room room;
}
