package nexos.repository;


import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import nexos.model.Personalinfo;

@Repository
public interface PersonalInfoRepository extends MongoRepository<Personalinfo, Long>{
}
