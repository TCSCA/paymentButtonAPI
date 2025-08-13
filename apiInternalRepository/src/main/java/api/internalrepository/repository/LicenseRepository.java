package api.internalrepository.repository;

import api.internalrepository.entity.LicenseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LicenseRepository extends JpaRepository<LicenseEntity, Long> {

    @Query(value = "select license from LicenseEntity as license " +
            "inner join StatusLicenseEntity as status on status.idStatusLicense = license.statusLicenseEntity.idStatusLicense " +
            "inner join CommerceEntity as com on com.idCommerce = license.commerceEntity.idCommerce " +
            "where com.idCommerce =:idCommerce and status.idStatusLicense = 1 ")
    LicenseEntity getLicenseEntityActiveByIdCommerce(@Param("idCommerce") final Long idCommerce);

    @Query(value = "select licence from LicenseEntity as licence where licence.statusLicenseEntity.idStatusLicense = 2")
    List<LicenseEntity> getAllActiveLicences();

    @Query(value = "select license from LicenseEntity as license " +
            "inner join StatusLicenseEntity as status on status.idStatusLicense = license.statusLicenseEntity.idStatusLicense " +
            "inner join CommerceEntity as com on com.idCommerce = license.commerceEntity.idCommerce " +
            "where com.idCommerce =:idCommerce ")
    LicenseEntity getLicenseEntityByIdCommerce(@Param("idCommerce") final Long idCommerce);

}
