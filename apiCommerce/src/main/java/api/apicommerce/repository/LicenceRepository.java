package api.apicommerce.repository;

import api.apicommerce.entity.LicenseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface LicenceRepository extends JpaRepository<LicenseEntity, Long> {

    @Query(value = "select licence from LicenseEntity licence " +
            "inner join StatusLicenseEntity as status on status.idStatusLicense = licence.statusLicenseEntity.idStatusLicense " +
            "inner join CommerceEntity as commerce on licence.commerceEntity.idCommerce = commerce.idCommerce " +
            "where licence.licenseCode = :licenceCode " +
            "and commerce.idCommerce = :idCommerce " +
            "and cast(licence.expireDate as DATE)  > :currentDate " +
            "and status.idStatusLicense = 1 ")
    LicenseEntity getLicenceByIdCommerceBeforeExpireDateAndStatusValid(@Param("licenceCode") String licenceCode,
                                                         @Param("idCommerce") Long idCommerce,
                                                         @Param("currentDate")LocalDate currentDate);
}
