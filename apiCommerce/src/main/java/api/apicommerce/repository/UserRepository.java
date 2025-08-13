package api.apicommerce.repository;

import api.apicommerce.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository  extends JpaRepository<UserEntity, Long> {

    UserEntity findByIdUser(Long idUser);
}
