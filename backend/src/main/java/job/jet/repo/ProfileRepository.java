package job.jet.repo;

import job.jet.domain.Profile;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProfileRepository extends MongoRepository<Profile, String> {
    @SuppressWarnings("null")
    List<Profile> findAll(); // returns List<Profile> instead of Iterable<Profile>
    Profile findByUsername(String username);
    boolean deleteByUsername(String username);
    boolean existsByUsername(String username);
}