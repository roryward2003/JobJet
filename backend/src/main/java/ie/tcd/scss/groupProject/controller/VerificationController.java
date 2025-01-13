package ie.tcd.scss.groupProject.controller;

import ie.tcd.scss.groupProject.domain.Profile;
import ie.tcd.scss.groupProject.service.VerificationService;
import ie.tcd.scss.groupProject.service.ProfileService;
import ie.tcd.scss.groupProject.domain.VerifToken;
import io.swagger.v3.oas.annotations.Parameter;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping("/auth/verif")
public class VerificationController {

    private final VerificationService verificationService;
    private final ProfileService profileService;

    public VerificationController(VerificationService verificationService, ProfileService profileService) {
        this.verificationService = verificationService;
        this.profileService = profileService;
    }

    @GetMapping("/{token}")
    public ResponseEntity<Profile> verifyProfile(
            @Parameter(description = "The token of the profile to validate", required = true) @PathVariable String token) {
        VerifToken verifToken = verificationService.getVerifToken(token);
        Profile profile = profileService.getProfileByUsername(verifToken.getEmailAddress());
        profileService.setVerified(profile, true);
        return ResponseEntity.ok(profile);
    }

}
