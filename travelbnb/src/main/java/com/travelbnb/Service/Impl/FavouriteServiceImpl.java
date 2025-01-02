package com.travelbnb.Service.Impl;

import com.travelbnb.Entity.AppUser;
import com.travelbnb.Entity.Favourites;
import com.travelbnb.Entity.Property;
import com.travelbnb.Exception.ResourceNotFoundException;
import com.travelbnb.Service.FavouriteService;
import com.travelbnb.payload.FavouriteDto;
import com.travelbnb.repository.FavouritesRepository;
import com.travelbnb.repository.PropertyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FavouriteServiceImpl implements FavouriteService {
   @Autowired
   private PropertyRepository propertyRepository;
   @Autowired
   private FavouritesRepository favouritesRepository;

    @Override
    public FavouriteDto addFavourite(AppUser user, long propertyId, FavouriteDto favouriteDto) {

        Property property = propertyRepository.findById(propertyId).orElseThrow(
                ()->new ResourceNotFoundException("Property not found with id: "+propertyId)
        );
        favouriteDto.setProperty(property);

        favouriteDto.setAppUser(user);
        Favourites favourite = dtoToEntity(favouriteDto);

        Optional<Favourites> existFavourite = favouritesRepository.findByUserAndProperty(favourite.getAppUser(), favourite.getProperty());
        if (existFavourite.isPresent()){
            throw new ResourceNotFoundException("Favourite Already exist with property: "+favourite.getProperty().getName());
        }
        Favourites saved = favouritesRepository.save(favourite);
        FavouriteDto dto = entityToDto(saved);
        return dto;
    }

    @Override
    public List<FavouriteDto> getFavouriteByUser(AppUser appUser) {
        List<Favourites> favouriteByUser = favouritesRepository.findFavouriteByUser(appUser);
        List<FavouriteDto> collect = favouriteByUser.stream().map(f -> entityToDto(f)).collect(Collectors.toList());
        return collect;

    }

    @Override
    public FavouriteDto updateFavorite(AppUser user, long favId, long propertyId, FavouriteDto favouriteDto) {
        Favourites favourite=null;
        favourite = favouritesRepository.findById(favId).orElseThrow(
                ()->new ResourceNotFoundException("Favourite not found with id:" +favId)
        );

        Property property = propertyRepository.findById(propertyId).orElseThrow(
                ()->new ResourceNotFoundException("Property not found with id: "+propertyId)
        );
        favouriteDto.setProperty(property);
        favouriteDto.setAppUser(user);
        favourite = dtoToEntity(favouriteDto);
        favourite.setId(favId);

        Favourites save = favouritesRepository.save(favourite);
        FavouriteDto dto = entityToDto(save);
        return dto;
    }
    //dto to entity

    Favourites dtoToEntity(FavouriteDto favouriteDto){
        Favourites entity=new Favourites();
        entity.setStatus(favouriteDto.getStatus());
        entity.setProperty(favouriteDto.getProperty());
        entity.setAppUser(favouriteDto.getAppUser());
        return entity;
    }
    //entity to dto
    FavouriteDto entityToDto(Favourites favourite){
        FavouriteDto dto=new FavouriteDto();
        dto.setId(favourite.getId());
        dto.setStatus(favourite.getStatus());
        dto.setProperty(favourite.getProperty());
        dto.setAppUser(favourite.getAppUser());
        return dto;
    }
}
