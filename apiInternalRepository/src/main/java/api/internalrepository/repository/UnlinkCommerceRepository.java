package api.internalrepository.repository;

import api.internalrepository.entity.UnlinkCommerceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UnlinkCommerceRepository extends JpaRepository<UnlinkCommerceEntity, Long> {

    @Query(value = "select unlink from UnlinkCommerceEntity as unlink " +
            "inner join CommerceEntity as com on com.idCommerce = unlink.commerceEntity.idCommerce " +
            "where com.idCommerce = :idCommerce and unlink.status is true ")
    UnlinkCommerceEntity findUnlinkCommerceByIdCommerceAndStatusIsTrue(
            @Param("idCommerce") final Long idCommerce);

}
