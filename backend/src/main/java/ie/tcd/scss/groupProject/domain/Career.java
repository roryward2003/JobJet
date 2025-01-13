package ie.tcd.scss.groupProject.domain;

import jakarta.persistence.*;
import lombok.Getter;

import java.util.Objects;

@Getter
public class Career {
    @Id
    private String id;

    @Column(length = 200)
    private String companyName;

    private String title;
    private String location;
    private String description;
    private String salary;
    private String url;


    // We can add additional info here when needed
    // Additional info will need getters, setters, etc.

    public Career(String companyName) {
        this.companyName = companyName;
    }

    public Career(){
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getCompanyName() { return this.companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }

    public String getTitle() { return this.title; }
    public void setTitle(String title) { this.title = title; }

    public String getLocation() { return this.location; }
    public void setLocation(String location) { this.location = location; }

    public String getDescription() { return this.description; }
    public void setDescription(String description) { this.description = description; }

    public String getSalary() { return this.salary; }
    public void setSalary(String salary) { this.salary = salary; }

    public String getUrl() { return this.url; }
    public void setUrl(String url) { this.url = url; }

    @Override
    public String toString() { return "Career{"+"id="+id+", Company Name="+companyName;}
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Career career = (Career) o;
        return Objects.equals(id,career.id) && Objects.equals(companyName,career.companyName);
    }

    @Override
    public int hashCode() { return Objects.hash(id,companyName); }
}