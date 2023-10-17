package com.fyp.hotel.controller.user;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fyp.hotel.dto.LoginRequestDto;
import com.fyp.hotel.dto.LoginResponseDto;
import com.fyp.hotel.model.User;
import com.fyp.hotel.serviceImpl.user.UserServiceImplementation;
import com.fyp.hotel.util.JwtUtils;

import lombok.RequiredArgsConstructor;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*", maxAge = 3600) 
@RequiredArgsConstructor
@RequestMapping("/login")
public class LoginController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final UserServiceImplementation userServiceImplementation;
    
    @PostMapping()
    public ResponseEntity<LoginResponseDto> authenticate(@RequestBody LoginRequestDto request) {

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
            System.out.println("token:------------------> " + token);
            
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
