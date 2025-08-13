package api.internalrepository.repository;

import api.internalrepository.entity.MessageApiEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageApiRepository extends JpaRepository<MessageApiEntity, Long> {

    MessageApiEntity findByCodeAndStatusTrue(String code);
}
