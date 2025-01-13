package ie.tcd.scss.groupProject.repo;

import ie.tcd.scss.groupProject.domain.VerifToken;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface VerifTokenRepository extends MongoRepository<VerifToken, String> {
    VerifToken findByToken(String token);
}
