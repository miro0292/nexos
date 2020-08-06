package nexos.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import nexos.exception.ErrorDetails;

@Repository
public interface LogRepository extends MongoRepository<ErrorDetails, Long>{

}
