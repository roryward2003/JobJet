package ie.tcd.scss.groupProject.dto;

public class ProfileDto {
    private String username;
    private String password;
    private String name;
    private String keywords;
    private String location;
    private String jobType;

    public ProfileDto(String username, String password, String name,  String keywords, String location, String jobType) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.keywords = keywords;
        this.location = location;
        this.jobType = jobType;
    }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getKeywords() { return keywords; }
    public void setKeywords(String keywords) { this.keywords = keywords; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    public String getJobType() { return jobType; }
    public void setJobType(String jobType) { this.jobType = jobType; }
}