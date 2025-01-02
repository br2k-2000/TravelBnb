package com.travelbnb.Service.Impl;

import com.travelbnb.Entity.AppUser;
import com.travelbnb.Service.JWTService;
import com.travelbnb.Service.UserService;
import com.travelbnb.payload.AppUserDto;
import com.travelbnb.payload.LoginDto;
import com.travelbnb.repository.AppUserRepository;
import org.springframework.security.crypto.bcrypt.BCrypt;

import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final AppUserRepository appUserRepository;

    private JWTService jwtService;

    public UserServiceImpl(AppUserRepository appUserRepository, JWTService jwtService) {
        this.appUserRepository = appUserRepository;
        this.jwtService = jwtService;
    }

    public AppUserDto entityToDTO(AppUser appUser) {
        AppUserDto userDTO = new AppUserDto();
        userDTO.setId(appUser.getId());
        userDTO.setName(appUser.getName());
        userDTO.setUsername(appUser.getUsername());
        userDTO.setEmail(appUser.getEmail());
        userDTO.setMobile(appUser.getMobile());
        userDTO.setDate(new Date());
        return userDTO;
    }

    @Override
    public String verifyLogin(LoginDto loginDto) {
        Optional<AppUser> opUsername = appUserRepository.findByUsername(loginDto.getUsername());
        if (opUsername.isPresent()){
            AppUser appUser = opUsername.get();
            if (BCrypt.checkpw(loginDto.getPassword(),appUser.getPassword())){
              String token = jwtService.generateToken(appUser);
              return token;
            }

        }

        return null;
    }

   @Override
    public AppUser createUser(AppUser appUser) {
       appUser.setPassword(BCrypt.hashpw(appUser.getPassword(), BCrypt.gensalt(10)));
       appUserRepository.save(appUser);
        return appUser;

    }
}



