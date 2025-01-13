package ie.tcd.scss.groupProject.controller;

import ie.tcd.scss.groupProject.domain.Profile;
import ie.tcd.scss.groupProject.domain.Career;
import ie.tcd.scss.groupProject.service.ProfileService;
import ie.tcd.scss.groupProject.service.CareerService;
import ie.tcd.scss.groupProject.dto.CareerDto;
import ie.tcd.scss.groupProject.dto.ProfileDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

@CrossOrigin
@RestController
@RequestMapping("/groupProject")
@Tag(name = "Group Project", description = "APIs for retrieving and modifying Profiles and Careers")
// Annotation for OpenAPI/Swagger
// See configuration in class OpenApiConfig in package config
// Access at http://localhost:8080/swagger-ui.html
public class GroupProjectController {
    // Create logger for this class
    private static final Logger logger = LoggerFactory.getLogger(GroupProjectController.class);

    private final CareerService careerService;
    private final ProfileService profileService;

    public GroupProjectController(CareerService careerService, ProfileService profileService) {
        this.careerService = careerService;
        this.profileService = profileService;
    }

    //
    // GET mapping to "/logging-example" for a log output example
    //
    @Operation(
            summary = "Example method for logging",
            description = "Demonstrates different logging levels and logging with parameters and exceptions"
    )
    @GetMapping("/logging-example")
    public void exampleMethod() {
        // Different logging levels
        logger.trace("Trace Message");  // Not printed by default
        logger.debug("Debug Message");  // Not printed by default
        logger.info("Info Message");    // Printed by default
        logger.warn("Warning Message"); // Printed by default
        logger.error("Error Message");  // Printed by default

        // Logging with parameters
        String param = "test";
        logger.info("Processing request with parameter: {}", param);

        // Logging exceptions
        try {
            throw new Exception("Test exception");
        } catch (Exception e) {
            logger.error("Error occurred", e);
        }
    }

    //
    // GET mapping to "/getID" for retrieving a user's ID based on the auth token and username.
    //
    @Operation(
        summary = "Get ID by username",
        description = "Validates auth token and returns logged in user's ID"
    )
    @GetMapping("/id")
    public ResponseEntity<String> getId() {
        Profile currentUser = getCurrentUser();
        return ResponseEntity.ok("Debug "+currentUser.getId());
    }

    //
    // POST mapping to "/jobs" for retrieving a list of jobs by keywords and location
    // @RequestBody of type CareerDto contains filter data
    //
    @Operation(
            summary = "Find Jobs by keywords and location",
            description = "Retrieves a List of jobs refined by keywords and location."
    )
    @PostMapping("/jobs")
    public ResponseEntity<List<Career>> getJobs(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Keywords and Location to refine search",
                    required = true
            )
            @RequestBody CareerDto careerDto
    ) {
        List<Career> careers = careerService.getJobs(careerDto.getKeywords(), careerDto.getLocation());
        if (careers == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(careers);
    }

    @GetMapping("/jobs/{id}")
    public ResponseEntity<Career> getJobById(
            @Parameter(
                    description = "The id of the job to retrieve",
                    required = true
            )
            @PathVariable String id)
    {
        Career career = careerService.getCareerById(id);
        if(career == null) { return ResponseEntity.notFound().build(); }
        return ResponseEntity.ok(career);
    }

    @PutMapping("users/{username}/{jobId}")
    public ResponseEntity<String> addJob(
            @Parameter(
                    description = "Username to add new job to",
                    required = true
            )
            @PathVariable String username,
            @Parameter(
                    description = "ID of new job to add to user profile",
                    required = true
            )
            @PathVariable String jobId
    )
    {
        profileService.addJob(username, jobId);
        return ResponseEntity.ok("Job added to user profile");
    }

    @DeleteMapping("users/{username}/{jobId}")
    public ResponseEntity<String> removeJob(
            @Parameter(
                    description = "Username to remove job from",
                    required = true
            )
            @PathVariable String username,
            @Parameter(
                    description = "ID of new job to remove from user profile",
                    required = true
            )
            @PathVariable String jobId
    )
    {
        profileService.removeJob(username, jobId);
        return ResponseEntity.ok("Job removed from user profile");
    }

    @PutMapping("users/reject/{username}/{jobId}")
    public ResponseEntity<String> rejectJob(
            @Parameter(
                    description = "Username to reject job for",
                    required = true
            )
            @PathVariable String username,
            @Parameter(
                    description = "ID of rejected job",
                    required = true
            )
            @PathVariable String jobId
    )
    {
        profileService.rejectJob(username, jobId);
        return ResponseEntity.ok("Job rejected for user profile");
    }

    @DeleteMapping("users/reject/{username}")
    public ResponseEntity<String> unRejectAll(
            @Parameter(
                    description = "Username to clear rejected jobs from",
                    required = true
            )
            @PathVariable String username
    )
    {
        profileService.unRejectAll(username);
        return ResponseEntity.ok("Rejected jobs cleared for user profile");
    }

    @GetMapping("user")
    @Operation(summary = "Return currently logged in user information")
    public ResponseEntity<Profile> getUser()
    {
        Profile currentUser = getCurrentUser();
        if(currentUser == null) { return ResponseEntity.notFound().build(); }
        return ResponseEntity.ok(currentUser);
    }

    @Operation(summary = "Retrieve a list of all saved jobs of the current logged in users")
    @GetMapping("/jobs")
    public ResponseEntity<List<Career>> getJobs() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Profile currentUser = (Profile) authentication.getPrincipal();
        if(currentUser == null) { return ResponseEntity.notFound().build(); }

        List<Career> savedJobs = new ArrayList<>();
        for(String jobId : currentUser.getSavedJobs()) {
            savedJobs.add(careerService.getCareerById(jobId));
        }
        return ResponseEntity.ok(savedJobs);
    }

    @Operation(summary = "Retrieve a list of all potential jobs of the current logged in user")
    @GetMapping("/users/jobs")
    public ResponseEntity<List<Career>> getPotentialJobs() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Profile currentUser = (Profile) authentication.getPrincipal();
        if(currentUser == null) { return ResponseEntity.notFound().build(); }

        List<Career> potentialJobs = careerService.getJobs(currentUser.getJobType()+" "+currentUser.getKeywords(), currentUser.getLocation());
        List<Career> rejectedJobs = new ArrayList<Career>();
        List<Career> savedJobs = new ArrayList<Career>();
        for(Career c : potentialJobs) {
            if(currentUser.getRejectedJobs().contains(c.getId()))
                rejectedJobs.add(c);
            if(currentUser.getSavedJobs().contains(c.getId()))
                savedJobs.add(c);
        }
        if (potentialJobs == null) {
            return ResponseEntity.notFound().build();
        }
        potentialJobs.removeAll(rejectedJobs);
        potentialJobs.removeAll(savedJobs);
        return ResponseEntity.ok(potentialJobs);
    }

    @Operation(summary = "Change keywords, location and jobType of logged in user")
    @PostMapping("users/params")
    public ResponseEntity<String> setParams(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Keywords and Location to refine search",
            required = true
        )
        @RequestBody ProfileDto profileDto)
    {
        logger.error("Keywords: "+profileDto.getKeywords()+", Location: "+profileDto.getLocation()+", JobType: "+profileDto.getJobType());
        String keywords = (profileDto.getKeywords().isEmpty() ? " " : profileDto.getKeywords());
        String location = (profileDto.getLocation().isEmpty() ? " " : profileDto.getLocation());
        String jobType = ((profileDto.getJobType().isEmpty() || profileDto.getJobType().equalsIgnoreCase("any")) ? " " : profileDto.getJobType());
        logger.error("Keywords: "+keywords+", Location: "+location+", JobType: "+jobType);

        profileService.setKeywords(getCurrentUser(), keywords);
        profileService.setLocation(getCurrentUser(), location);
        profileService.setJobType(getCurrentUser(), jobType);
        return ResponseEntity.ok("Profile updated");
    }
    
    @Operation(summary = "Change keywords of logged in user")
    @PutMapping("user/keywords")
    public ResponseEntity<String> setKeywords(@RequestBody String keywords)
    {
        profileService.setKeywords(getCurrentUser(), keywords);
        return ResponseEntity.ok("Keywords updated");
    }

    @Operation(summary = "Change password of logged in user")
    @PutMapping("users/password")
    public ResponseEntity<String> setPassword(@RequestBody String password)
    {
        profileService.setPassword(getCurrentUser(), password);
        return ResponseEntity.ok("Password updated");
    }

    @Operation(summary = "Change location of logged in user")
    @PutMapping("users/location")
    public ResponseEntity<String> setLocation(@RequestBody String location)
    {
        profileService.setLocation(getCurrentUser(), location);
        return ResponseEntity.ok("Location updated");
    }

    @Operation(summary = "Change job type of logged in user")
    @PutMapping("users/jobType")
    public ResponseEntity<String> setJobType(@RequestBody String jobType)
    {
        profileService.setJobType(getCurrentUser(), jobType);
        return ResponseEntity.ok("Job type updated");
    }

    // Method for convenience to get current user
    private Profile getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (Profile) authentication.getPrincipal();
    }
}