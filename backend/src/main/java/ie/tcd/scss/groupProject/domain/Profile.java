package ie.tcd.scss.groupProject.domain;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.Getter;
import lombok.Setter;

@Getter
public class Profile implements UserDetails {
    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    @Setter
    @Column(length = 200, unique = true)
    private String username;

    // Hashed password
    @Setter
    @Column(nullable = false)
    private String password;

    @Setter
    @Getter
    @Column(nullable = false)
    private boolean verified;

    @Setter
    @Getter
    private List<String> savedJobs;
    
    @Setter
    @Getter
    private List<String> rejectedJobs;

    @Setter
    @Getter
    private String name;
    
    @Setter
    @Getter
    private String keywords;
    
    @Setter
    @Getter
    private String location;

    @Setter
    @Getter
    private String jobType;

    public Profile(String username, String password, String name, String keywords, String location, String jobType) {
        this.username = username;
        this.password = password;
        this.verified = false;
        this.savedJobs = new ArrayList<>();
        this.rejectedJobs = new ArrayList<>();
        this.name = name;
        this.keywords = keywords;
        this.location = location;
        this.jobType = jobType;
    }

    public Profile(){
        this.savedJobs = new ArrayList<String>();
        this.rejectedJobs = new ArrayList<String>();
    }

    @Override
    public String toString() { return "Profile{"+"id="+id+", username="+username;}
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Profile profile = (Profile) o;
        return Objects.equals(id,profile.id) && Objects.equals(username,profile.username);
    }

    public void addJob(String jobId) {
        savedJobs.add(jobId);
    }

    public void removeJob(String jobId) {
        savedJobs.remove(jobId);
    }

    public void rejectJob(String jobId) {
        rejectedJobs.add(jobId);
    }

    public void unRejectJob(String jobId) {
        rejectedJobs.remove(jobId);
    }

    public void unRejectAll() {
        rejectedJobs = new ArrayList<String>();
    }

    @Override
    public int hashCode() { return Objects.hash(id,username); }

    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }
}