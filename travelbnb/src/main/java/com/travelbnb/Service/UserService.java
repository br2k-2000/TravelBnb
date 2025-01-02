package com.travelbnb.Service;

import com.travelbnb.Entity.AppUser;
import com.travelbnb.payload.AppUserDto;
import com.travelbnb.payload.LoginDto;


public interface UserService {

    AppUser createUser(AppUser appUser);

    String verifyLogin(LoginDto loginDto);

    AppUserDto entityToDTO(AppUser createdUser);
}
