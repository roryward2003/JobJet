package job.jet.repo;

import job.jet.domain.VerifToken;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface VerifTokenRepository extends MongoRepository<VerifToken, String> {
    VerifToken findByToken(String token);
}
