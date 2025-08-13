package api.authentication.repository;

import api.authentication.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    UserEntity findByIdUser(Long idUser);

    UserEntity findByUserName(String username);
}
