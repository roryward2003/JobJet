package job.jet.response;

import lombok.Getter;
import lombok.Setter;

//
// This basic class is used for returning token data from login requests
//

@Getter
public class LoginResponse {

    @Setter
    private String token;

    @Setter
    @Getter
    private String errorMsg;

    @Getter
    @Setter
    private long expiresIn;

    public String getToken() {
        return token;
    }
}