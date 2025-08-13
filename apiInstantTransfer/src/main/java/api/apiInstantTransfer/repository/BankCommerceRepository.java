package api.apiInstantTransfer.repository;

import api.apiInstantTransfer.entity.BankCommerceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.OffsetDateTime;

public interface BankCommerceRepository extends JpaRepository<BankCommerceEntity, Long> {

    BankCommerceEntity findByCommerceEntity_IdCommerceAndStatusTrue(Long idCommerce);

    @Query(value = "select bankCommerce from BankCommerceEntity bankCommerce " +
            "inner join CommerceEntity as commerce on commerce.idCommerce = bankCommerce.commerceEntity.idCommerce " +
            "inner join LicenseEntity licence on licence.commerceEntity.idCommerce = commerce.idCommerce " +
            "where cast(licence.expireDate as DATE)  > :currentDate and " +
            "commerce.idCommerce = :idCommerce and bankCommerce.status = true " +
            "and bankCommerce.bankEntity.idBank = 5")
    BankCommerceEntity getBankCommerceInformationByIdCommerceAndLicenseValid(@Param("idCommerce") Long idCommerce,
                                                                             @Param("currentDate")LocalDate currentDate);


    @Modifying
    @Query(value = "update BankCommerceEntity bankCommerce set bankCommerce.token = :token, " +
            "bankCommerce.tokenExpireDate = :expireDate where bankCommerce.idBankCommerce = :idBankCommerce")
    void updateBankCommerceToken(@Param("token") String token,
                                 @Param("idBankCommerce") Long idBankCommerce,
                                 @Param("expireDate")OffsetDateTime expireDate);


}
