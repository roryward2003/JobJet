package job.jet.config;

import lombok.Data;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
public class JoobleAPIConfig {
    private String apiKey = System.getenv("JOOBLE_API_KEY");
    private String baseUrl = "https://jooble.org/api/";
}
