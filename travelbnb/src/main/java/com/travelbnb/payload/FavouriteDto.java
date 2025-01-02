package com.travelbnb.payload;

import com.travelbnb.Entity.AppUser;
import com.travelbnb.Entity.Property;
import lombok.Data;

@Data
public class FavouriteDto {
    private Long id;
    private Boolean status;
    private Property property;
    private AppUser appUser;
}
