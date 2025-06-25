package job.jet.service;

import job.jet.dto.ProfileDto;
import job.jet.domain.Profile;
import job.jet.domain.VerifToken;
import job.jet.repo.ProfileRepository;
import job.jet.repo.VerifTokenRepository;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;

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
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            input.getUsername(),
                            input.getPassword()
                    )
            );
            return profileRepository.findByUsername(input.getUsername());
        } catch (InternalAuthenticationServiceException e) {
            throw new UsernameNotFoundException("User not found: " + input.getUsername());
        }
    }
}