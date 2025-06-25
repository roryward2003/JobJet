package job.jet.controller;

import job.jet.domain.Profile;
import job.jet.dto.ProfileDto;
import job.jet.service.AuthenticationService;
import job.jet.service.JwtService;
import job.jet.response.LoginResponse;

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
        return ResponseEntity.status(HttpStatus.CREATED).body(registeredUser);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticate(@RequestBody ProfileDto loginUserDto) {
        LoginResponse loginResponse = new LoginResponse();
        Profile authenticatedUser;
        
        try {
            authenticatedUser = authenticationService.authenticate(loginUserDto);
        } catch(Exception e) {
            loginResponse.setErrorMsg("The username or password is incorrect");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(loginResponse);
        }

        if(!authenticatedUser.isVerified()) {
            loginResponse.setErrorMsg("Please verify your email.");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(loginResponse);
        }
        String jwtToken = jwtService.generateToken(authenticatedUser);
        loginResponse.setToken(jwtToken);
        loginResponse.setExpiresIn(jwtService.getExpirationTime());

        return ResponseEntity.ok(loginResponse);
    }
}