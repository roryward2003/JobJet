package job.jet.service;

import job.jet.domain.Profile;
import job.jet.repo.ProfileRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;

@Service
public class ProfileService {
    private final ProfileRepository profileRepository;
    private final PasswordEncoder passwordEncoder; 


    @Autowired
    public ProfileService(ProfileRepository profileRepository, PasswordEncoder passwordEncoder) {
        this.profileRepository = profileRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void setKeywords(Profile profile, String keywords)
    {
        profile.setKeywords(keywords);
        profileRepository.save(profile);
    }

    public void setLocation(Profile profile, String location)
    {
        profile.setLocation(location);
        profileRepository.save(profile);
    }

    public void setJobType(Profile profile, String jobType)
    {
        profile.setJobType(jobType);
        profileRepository.save(profile);
    }

    public void setPassword(Profile profile, String password)
    {
        profile.setPassword(passwordEncoder.encode(password));
        profileRepository.save(profile);
    }

    public void setVerified(Profile profile, boolean verified){
        profile.setVerified(verified);
        profileRepository.save(profile);
    }

    public void addJob(String username, String id)
    {
        Profile profile = profileRepository.findByUsername(username);
        if(profile != null)
        {
            profile.addJob(id);
            profileRepository.save(profile);
        }
    }

    public void removeJob(String username, String id)
    {
        Profile profile = profileRepository.findByUsername(username);
        if(profile != null)
        {
            profile.removeJob(id);
            profileRepository.save(profile);
        }
    }

    public void rejectJob(String username, String id) {
        Profile profile = profileRepository.findByUsername(username);
        if(profile != null) {
            profile.rejectJob(id);
            profileRepository.save(profile);
        }
    }

    public void unRejectJob(String username, String id) {
        Profile profile = profileRepository.findByUsername(username);
        if(profile != null) {
            profile.unRejectJob(id);
            profileRepository.save(profile);
        }
    }

    public void unRejectAll(String username) {
        Profile profile = profileRepository.findByUsername(username);
        if(profile != null) {
            profile.unRejectAll();
            profileRepository.save(profile);
        }
    }

    public Profile createProfile(String username, String password, String name, String keywords,
    String location, String jobType) {

        String encryptedPassword = passwordEncoder.encode(password);
        Profile profile = new Profile();
        profile.setUsername(username);
        profile.setPassword(encryptedPassword);
        profile.setName(name);
        profile.setKeywords(keywords);
        profile.setLocation(location);
        profile.setJobType(jobType);
        return profileRepository.save(profile);
    }
    
    public boolean validatePassword(String username, String passwordAttempt){
        Profile profile = getProfileByUsername(username);
        return passwordEncoder.matches(passwordAttempt, profile.getPassword());
    }

    public void deleteProfile(String id) {
        profileRepository.deleteById(id);
    }
    public void deleteProfileByUsername(String username) {
        profileRepository.deleteByUsername(username);
    }
    public Profile getProfileByUsername(String username) {
        return profileRepository.findByUsername(username);
    }
    public List<Profile> getAllProfiles() {
        return profileRepository.findAll();
    }
}