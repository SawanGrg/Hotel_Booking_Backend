package com.fyp.hotel.controller.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.fyp.hotel.dto.LoginRequestDto;
import com.fyp.hotel.dto.LoginResponseDto;
import com.fyp.hotel.model.User;
import com.fyp.hotel.serviceImpl.user.UserServiceImplementation;
import com.fyp.hotel.util.JwtUtils;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/v1")
public class LoginController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final UserServiceImplementation userServiceImplementation;

    @Autowired
    public LoginController(AuthenticationManager authenticationManager, JwtUtils jwtUtils, UserServiceImplementation userServiceImplementation) {
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.userServiceImplementation = userServiceImplementation;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> authenticate(
            @Validated //validated is used to validate the request body of the request we validate based on the annotations given in the dto class
            @RequestBody
            LoginRequestDto request) {

        doAuthenticate(request.getUsername(), request.getPassword());
        final User user = (User) userServiceImplementation.loadUserByUsername(request.getUsername());

        if (user == null) {
            LoginResponseDto loginResponseDto = new LoginResponseDto();
            loginResponseDto.setUsername("");
            loginResponseDto.setToken("Invalid credentials");
            loginResponseDto.setRoleName("");

            return ResponseEntity.badRequest().body(loginResponseDto);

        } else {

            System.out.println("came in login controller  step 4 : " + user.getUsername() + " " + user.getPassword() + " " + user.getRoles().iterator().next().getRoleName() + " " + user.getUserStatus());

            if (user.getUserStatus().equals("PENDING") || user.getUserStatus().equals("UNVERIFIED")) {
                if (user.getRoles().iterator().next().getRoleName().equals("ROLE_USER")) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new LoginResponseDto("", "user is not verified", ""));
                } else if (user.getRoles().iterator().next().getRoleName().equals("ROLE_VENDOR")) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new LoginResponseDto("", "vendor is not verified", ""));
                }
            }

                String jwtToken = jwtUtils.generateToken(user);

                LoginResponseDto loginResponseDto = new LoginResponseDto();
                loginResponseDto.setUsername(user.getUsername());
                loginResponseDto.setToken(jwtUtils.generateToken(user));
                loginResponseDto.setRoleName(user.getRoles().iterator().next().getRoleName());

                return ResponseEntity.ok().body(loginResponseDto);


        }
    }

    //this method checks whether the user is authenticated or not
    //
    private void doAuthenticate(String username, String password){
        try{

            System.out.println("came in login controller  step 5 : " );

            //token is created with the help of username and password
            //UsernamePasswordAuthenticationToken is useful when we want to authenticate a user with a username and password.
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, password);
            System.out.println(" from username password authentication token:------------------> " + token);
            
            //authenticate method makes sure that the user is authenticated or not
            authenticationManager.authenticate(token);
            //if the user is authenticated then it will return true
            //else it will throw an exception
        }
        catch(BadCredentialsException e){
            throw new BadCredentialsException ("Invalid credentials, please check your username and password");
        }
    }
    
}
