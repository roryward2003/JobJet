package ie.tcd.scss.groupProject.service;

import ie.tcd.scss.groupProject.dto.ProfileDto;
import ie.tcd.scss.groupProject.domain.Profile;
import ie.tcd.scss.groupProject.domain.VerifToken;
import ie.tcd.scss.groupProject.repo.ProfileRepository;
import ie.tcd.scss.groupProject.repo.VerifTokenRepository;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

//
// Service for automatic auth token evaluation for authentication purposes
//

@Service
public class AuthenticationService {
    private final ProfileRepository profileRepository;
    private final VerifTokenRepository verifTokenRepository;
    
    private final PasswordEncoder passwordEncoder;
    
    private final AuthenticationManager authenticationManager;

    private final EmailService emailService;

    public AuthenticationService(
        ProfileRepository profileRepository,
        VerifTokenRepository verifTokenRepository,
        AuthenticationManager authenticationManager,
        PasswordEncoder passwordEncoder,
        EmailService emailService
    ) {
        this.authenticationManager = authenticationManager;
        this.profileRepository = profileRepository;
        this.verifTokenRepository = verifTokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }

    public Profile signup(ProfileDto input) {
        if(profileRepository.existsByUsername(input.getUsername())) {
            return new Profile();
        }
        Profile profile = new Profile(
            input.getUsername(),
            passwordEncoder.encode(input.getPassword()),
            input.getName(),
            input.getKeywords(),
            input.getLocation(),
            input.getJobType()
        );

        VerifToken token = new VerifToken(
            profile.getId(),
            profile.getUsername()
        );
        
        emailService.sendVerificationEmail(token.getEmailAddress(), token.getToken());
        verifTokenRepository.save(token);
        return profileRepository.save(profile);
    }

    public Profile authenticate(ProfileDto input) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        input.getUsername(),
                        input.getPassword()
                )
        );

        try {
            return profileRepository.findByUsername(input.getUsername());
        } catch (Exception e) {
            throw e;
        }
    }
}