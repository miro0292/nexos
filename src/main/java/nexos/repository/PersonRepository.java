package nexos.repository;


import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import nexos.model.Person;

@Repository
public interface PersonRepository extends MongoRepository<Person, Long>{

}
