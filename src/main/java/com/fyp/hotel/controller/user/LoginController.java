package com.fyp.hotel.controller.user;

import org.springframework.beans.factory.annotation.Autowired;
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

        System.out.println("came in login controller  step 1 : " + request);
        doAuthenticate(request.getUsername(), request.getPassword());
        System.out.println("came in login controller  step 2 : " + request);

        final User user = (User) userServiceImplementation.loadUserByUsername(request.getUsername());
        System.out.println("came in login controller  step 4 : " + request);

        if (user == null) {
            LoginResponseDto loginResponseDto = new LoginResponseDto();
            loginResponseDto.setUsername("");
            loginResponseDto.setToken("Invalid credentials");
            loginResponseDto.setRoleName("");

            return ResponseEntity.badRequest().body(loginResponseDto);
            
        } else {

            // The generateToken() method is used to generate the JWT token.
            final String jwtToken = jwtUtils.generateToken(user);
            
            LoginResponseDto loginResponseDto = new LoginResponseDto();
            loginResponseDto.setUsername(user.getUsername());
            loginResponseDto.setToken(jwtToken);
            loginResponseDto.setRoleName(user.getRoles().iterator().next().getRoleName());
            
            return ResponseEntity.ok().body(loginResponseDto);
                                    
        }
    }

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
