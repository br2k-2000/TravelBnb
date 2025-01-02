package com.travelbnb.Service;

import com.travelbnb.Entity.AppUser;
import com.travelbnb.payload.FavouriteDto;

import java.util.List;

public interface FavouriteService {

    FavouriteDto addFavourite(AppUser appUser, long propertyId, FavouriteDto favouriteDto);

    List<FavouriteDto> getFavouriteByUser(AppUser appUser);

    FavouriteDto updateFavorite(AppUser appUser, long favId, long propertyId, FavouriteDto favouriteDto);
}
