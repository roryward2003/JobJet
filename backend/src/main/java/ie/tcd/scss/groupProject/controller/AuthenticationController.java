package ie.tcd.scss.groupProject.controller;

import ie.tcd.scss.groupProject.domain.Profile;
import ie.tcd.scss.groupProject.dto.ProfileDto;
import ie.tcd.scss.groupProject.service.AuthenticationService;
import ie.tcd.scss.groupProject.service.JwtService;
import ie.tcd.scss.groupProject.response.LoginResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//
// Controls login and creation, which are not restricted by auth tokens
//

@CrossOrigin
@RequestMapping("/auth")
@RestController
public class AuthenticationController {
    private final JwtService jwtService;
    
    private final AuthenticationService authenticationService;

    public AuthenticationController(JwtService jwtService, AuthenticationService authenticationService) {
        this.jwtService = jwtService;
        this.authenticationService = authenticationService;
    }

    @PostMapping("/signup")
    public ResponseEntity<Profile> register(@RequestBody ProfileDto registerUserDto) {
        Profile registeredUser = authenticationService.signup(registerUserDto);
        if(registeredUser.getUsername().isEmpty()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.ok(registeredUser);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticate(@RequestBody ProfileDto loginUserDto) {
        Profile authenticatedUser = authenticationService.authenticate(loginUserDto);
        if(!authenticatedUser.isVerified()) {
            LoginResponse loginResponse = new LoginResponse();
            loginResponse.setErrorMsg("Please verify your email.");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(loginResponse);
        }
        String jwtToken = jwtService.generateToken(authenticatedUser);
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setToken(jwtToken);
        loginResponse.setExpiresIn(jwtService.getExpirationTime());

        return ResponseEntity.ok(loginResponse);
    }
}