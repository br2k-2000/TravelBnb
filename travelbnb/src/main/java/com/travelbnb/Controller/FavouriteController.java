package com.travelbnb.Controller;

import com.travelbnb.Entity.AppUser;
import com.travelbnb.Service.FavouriteService;
import com.travelbnb.payload.FavouriteDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/favourite")
public class FavouriteController {

    @Autowired
    private FavouriteService favouriteService;



    @PostMapping("/addFavourite")
    public ResponseEntity<FavouriteDto> addFavourite(
            @AuthenticationPrincipal AppUser appUser,
            @RequestParam long propertyId,
            @RequestBody FavouriteDto favouriteDto
    ){
        FavouriteDto savedStatus = favouriteService.addFavourite(appUser, propertyId, favouriteDto);
        return new ResponseEntity<>(savedStatus, HttpStatus.CREATED);
    }
    @GetMapping("/getFavouriteByUser")
    public ResponseEntity<List<FavouriteDto>> getFavouriteByUser(
            @AuthenticationPrincipal AppUser appUser
    ){
        List<FavouriteDto> favouriteByUser = favouriteService.getFavouriteByUser(appUser);
        return new ResponseEntity<>(favouriteByUser,HttpStatus.OK);
    }
    @PutMapping("/{favId}")
    public ResponseEntity<FavouriteDto> updateFavorite(
            @AuthenticationPrincipal AppUser appUser,
            @PathVariable long favId,
            @RequestParam long propertyId,
            @RequestBody FavouriteDto favouriteDto
    ){
        FavouriteDto dto = favouriteService.updateFavorite(appUser, favId, propertyId, favouriteDto);
        return new ResponseEntity<>(dto,HttpStatus.OK);
    }
}
