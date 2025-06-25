package job.jet.dto;

public class CareerDto {
    private String keywords;
    private String location;

    public CareerDto(String keywords, String location) {
        this.keywords = keywords;
        this.location = location;
    }

    public String getKeywords() { return keywords; }
    public void setKeywords(String keywords) { this.keywords = keywords; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
}
