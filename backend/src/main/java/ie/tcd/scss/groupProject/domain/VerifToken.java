package ie.tcd.scss.groupProject.domain;

import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class VerifToken {

    @Id
    private String id;              // ID should be taken from profile
    private String emailAddress;
    private String token;           // Token generated using java's UUID
    private long expirationTime;    // probably a week? Two weeks?

    public VerifToken() { }

    public VerifToken(String id, String emailAddress) {
        this.id = id;
        this.emailAddress = emailAddress;
        this.token = UUID.randomUUID().toString();
        this.expirationTime = 200000000;
    }

    public String getId(){ return this.id; }
    public void setId(String id){ this.id = id; }

    public String getToken(){ return this.token; }
    public void setToken(String token){ this.token = token; }

    public String getEmailAddress(){ return this.emailAddress; }
    public void setEmailAddress(String emailAddress){ this.emailAddress = emailAddress; }

    public long getExpirationTime(){ return this.expirationTime; }
    public void setExpirationTime(long expirationTime){ this.expirationTime =  expirationTime; }
}



