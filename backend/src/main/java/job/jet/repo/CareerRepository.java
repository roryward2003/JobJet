package job.jet.repo;

import job.jet.domain.Career;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CareerRepository extends MongoRepository<Career, String> {
    Career findById(long id);
}