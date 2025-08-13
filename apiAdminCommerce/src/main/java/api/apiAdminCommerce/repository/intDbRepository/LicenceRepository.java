package api.apiAdminCommerce.repository.intDbRepository;

import api.apiAdminCommerce.entity.LicenseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LicenceRepository extends JpaRepository<LicenseEntity, Long> {

    @Query(value = "select license from LicenseEntity as license " +
            "inner join CommerceEntity as commerce on commerce.idCommerce = license.commerceEntity.idCommerce " +
            "where commerce.idCommerce = :idCommerce")
    LicenseEntity getLicenseEntityByCommerceEntity_IdCommerce(@Param("idCommerce") final Long idCommerce);

    List<LicenseEntity>findAll();


    /*@Query(value = "select licence from LicenseEntity licence where " +
            "licence.commerceEntity.idCommerce = :idCommerce and cast(licence.expireDate as DATE)  > :currentDate " +
            "and licence.statusLicenseEntity.idStatusLicense = 1 ")
    LicenseEntity getLicenceByIdCommerceBeforeExpireDateAndStatusValid(
                                                                       @Param("idCommerce") Long idCommerce,
                                                                       @Param("currentDate") LocalDate currentDate);*/
}
