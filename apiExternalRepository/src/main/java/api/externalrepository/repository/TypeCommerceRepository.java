package api.externalrepository.repository;

import api.externalrepository.entity.TypeCommerceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TypeCommerceRepository extends JpaRepository<TypeCommerceEntity, Long> {
}
