package api.authentication.repository;

import api.authentication.entity.ClientEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends JpaRepository<ClientEntity, Long> {

    @Query(value = "SELECT client FROM ClientEntity client " +
            "inner join CommerceEntity as commerce on commerce.idCommerce = client.commerceEntity.idCommerce " +
            "inner join UserEntity as user on user.idUser = client.userEntity.idUser " +
            "WHERE user.idUser = :idUser ")
    ClientEntity findByIdUser(@Param("idUser")final Long idUser);
}
