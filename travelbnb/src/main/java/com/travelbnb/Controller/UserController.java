package com.travelbnb.Controller;

import com.travelbnb.Entity.AppUser;
import com.travelbnb.Service.UserService;
import com.travelbnb.payload.AppUserDto;
import com.travelbnb.payload.JWTTokenDto;
import com.travelbnb.payload.LoginDto;
import com.travelbnb.repository.AppUserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {


    private final UserService userService;
private final AppUserRepository appUserRepository;

    public UserController(UserService userService,
                          AppUserRepository appUserRepository) {
        this.userService = userService;
        this.appUserRepository = appUserRepository;
    }

    @PostMapping("/createUser")
    public ResponseEntity<?> createUser(@RequestBody AppUser appUser) {

        if(appUserRepository.existsByEmail(appUser.getEmail())){
            return new ResponseEntity<>("Email exists",HttpStatus.BAD_REQUEST);
        }
        if (appUserRepository.existsByUsername(appUser.getUsername())){
            return new ResponseEntity<>("Username exists",HttpStatus.BAD_REQUEST);
        }
        AppUser createdUser = userService.createUser(appUser);
        AppUserDto userDto = userService.entityToDTO(createdUser);
        return new ResponseEntity<>(userDto,HttpStatus.CREATED);
    }


    @PostMapping("/login")
    public ResponseEntity<?> verifyLogin(@RequestBody LoginDto loginDto){
        String token =  userService.verifyLogin(loginDto);
        if (token != null){
            JWTTokenDto jwtToken = new JWTTokenDto();
            jwtToken.setType("jwt Token");
            jwtToken.setToken(token);
            return new ResponseEntity<>(jwtToken,HttpStatus.OK);
        }else {
            return new ResponseEntity<>("invalid token",HttpStatus.OK);
        }
    }
}


