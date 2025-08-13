package api.internalrepository.repository;

import api.internalrepository.entity.CommerceEntity;
import api.internalrepository.entity.PlanEntity;
import api.internalrepository.entity.StatusCommerceEntity;
import api.internalrepository.to.CommerceBankInformationTo;
import api.internalrepository.to.CommerceInformationTo;
import api.internalrepository.to.CommerceTo;
import org.springframework.cglib.core.Local;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;

@Repository
public interface CommerceRepository extends JpaRepository<CommerceEntity, Long> {

    @Query("select commerce from CommerceEntity as commerce " +
            "inner join StatusCommerceEntity as status on status.idStatusCommerce = commerce.statusCommerce.idStatusCommerce " +
            "where commerce.commerceDocument = :rif and status.idStatusCommerce = :idStatusCommerce")
    CommerceEntity findByCommerceDocumentAndStatusCommerce_IdStatusCommerce(@Param("rif") String rif,
                                                    @Param("idStatusCommerce") Long idStatusCommerce);

    CommerceEntity findByIdCommerce(final Long idCommerce);

    @Query("select commerce from CommerceEntity as commerce " +
            "where commerce.commerceDocument = :rif")
    CommerceEntity findByCommerceDocument(@Param("rif") String rif);

    @Query("select commerce from CommerceEntity as commerce inner join LicenseEntity licence " +
            "on licence.commerceEntity.idCommerce = commerce.idCommerce " +
            "where cast(licence.expireDate as DATE)  > :currentDate " +
            "and licence.statusLicenseEntity.idStatusLicense = 1 and commerce.commerceDocument = :rif")
    CommerceEntity getLicenceValidByCommerce(@Param("rif") String rif, @Param("currentDate") LocalDate currentDate);

    @Query("select commerce from CommerceEntity as commerce inner join LicenseEntity licence " +
            "on licence.commerceEntity.idCommerce = commerce.idCommerce " +
            "and cast(licence.activationDate as DATE) <= :currentDate " +
            "and licence.statusLicenseEntity.idStatusLicense = 1 and commerce.commerceDocument = :rif")
    CommerceEntity getLicenceActivationDate(@Param("rif") String rif, @Param("currentDate") LocalDate currentDate);


    @Query(value = "select new api.internalrepository.to.CommerceBankInformationTo(commerce.commerceName, " +
            "commerce.commerceDocument, bankCommerce.commercePhone, bankCommerce.bankAccount, bankCommerce.bankEntity.bankName) " +
            "from BankCommerceEntity bankCommerce " +
            "inner join CommerceEntity commerce on commerce.idCommerce = bankCommerce.commerceEntity.idCommerce " +
            "where commerce.commerceDocument = :rif and bankCommerce.status = true and bankCommerce.bankEntity.idBank = 5L")
    CommerceBankInformationTo getCommerceBankInformation(@Param("rif") String rif);

    @Query(value = "select commerce from CommerceEntity commerce " +
            "where cast( commerce.registerDate as DATE) BETWEEN (:startDate) AND (:endDate) " +
            "and commerce.statusCommerce.idStatusCommerce = 3")
    Page<CommerceEntity> getCommerceRejectedInformationByDate
            (Pageable pageable, @Param("startDate") Date startDate, @Param("endDate") Date endDate);

    @Query(value = "select commerce from CommerceEntity commerce " +
            "where cast( commerce.registerDate as DATE) BETWEEN (:startDate) AND (:endDate) " +
            "and commerce.statusCommerce.idStatusCommerce = 3")
    List<CommerceEntity> getCommerceRejectedInformationByDateExport
            (@Param("startDate") Date startDate, @Param("endDate") Date endDate);

    @Query(value = "select commerce from CommerceEntity commerce " +
            "where cast( commerce.registerDate as DATE) BETWEEN (:startDate) AND (:endDate) " +
            "and commerce.statusCommerce.idStatusCommerce = 2 ")
    List<CommerceEntity> getCommerceInformationByDateExport
            (Pageable pageable, @Param("startDate") Date startDate, @Param("endDate") Date endDate);

    @Query(value = "select com from CommerceEntity as com " +
            "inner join StatusCommerceEntity as status on status.idStatusCommerce = com.statusCommerce.idStatusCommerce " +
            "left join DirectionEntity as direction on com.directionEntity.idDirection = direction.idDirection " +
            "left join CityEntity as city on direction.cityEntity.idCity = city.idCity " +
            "left join MunicipalityEntity as municipality on city.municipalityEntity.idMunicipality = municipality.idMunicipality " +
            "left join StateEntity as state on municipality.stateEntity.idState = state.idState " +
            "left join CountryEntity as country on state.countryEntity.idCountry = country.idCountry " +
            "where com.commerceDocument like :rif ")
    CommerceEntity findCommerceByCommerceDocument(@Param("rif") final String rif);

    @Query(value = "select com from CommerceEntity as com where com.statusCommerce.idStatusCommerce = 2")
    List<CommerceEntity> findAllActiveCommerce();

    @Query(value = "select com from CommerceEntity as com " +
            "inner join StatusCommerceEntity as status on status.idStatusCommerce = com.statusCommerce.idStatusCommerce " +
            "left join DirectionEntity as direction on com.directionEntity.idDirection = direction.idDirection " +
            "left join CityEntity as city on direction.cityEntity.idCity = city.idCity " +
            "left join MunicipalityEntity as municipality on city.municipalityEntity.idMunicipality = municipality.idMunicipality " +
            "left join StateEntity as state on municipality.stateEntity.idState = state.idState " +
            "left join CountryEntity as country on state.countryEntity.idCountry = country.idCountry " +
            "inner join ClientEntity as client on client.commerceEntity.idCommerce = com.idCommerce " +
            "where client.userEntity.idUser =:idUser")
    CommerceEntity findCommerceByIdUser(@Param("idUser") final Long idUser);

    @Query(value = "select plan from CommerceEntity as com " +
            "inner join LicenseEntity as license on com.idCommerce = license.commerceEntity.idCommerce " +
            "inner join PlanEntity as plan on plan.idPlan = license.planEntity.idPlan " +
            "where com.idCommerce = :idCommerce")
    PlanEntity getPlanByIdCommerce(@Param("idCommerce") final Long idCommerce);

    @Query(value = "select new api.internalrepository.to.CommerceTo(com.idCommerce, com.commerceDocument, " +
            "com.commerceName, state.stateName, municipality.municipalityName, city.cityDescription, " +
            "com.contactPerson, com.registerDate, status.statusDescription, com.phoneNumberCommerce, " +
            "type, plan,com.updateDate,com.statusCommerce.idStatusCommerce) from CommerceEntity as com " +
            "inner join StatusCommerceEntity as status on status.idStatusCommerce = com.statusCommerce.idStatusCommerce " +
            "inner join TypeCommerceEntity as type on type.idTypeCommerce = com.typeCommerceEntity.idTypeCommerce " +
            "inner join LicenseEntity as license on com.idCommerce = license.commerceEntity.idCommerce " +
            "inner join PlanEntity as plan on plan.idPlan = license.planEntity.idPlan " +
            "left join DirectionEntity as direction on com.directionEntity.idDirection = direction.idDirection " +
            "left join CityEntity as city on direction.cityEntity.idCity = city.idCity " +
            "left join MunicipalityEntity as municipality on city.municipalityEntity.idMunicipality = municipality.idMunicipality " +
            "left join StateEntity as state on municipality.stateEntity.idState = state.idState " +
            "left join CountryEntity as country on state.countryEntity.idCountry = country.idCountry " +
            "where com.statusCommerce.idStatusCommerce IN (2, 6) and cast(com.registerDate as DATE) BETWEEN (:startDate) AND (:endDate) " +
            "order by com.registerDate desc")
    Page<CommerceTo> findCommerceByFilterDate(Pageable pageable,@Param("startDate") Date startDate, @Param("endDate") Date endDate);


    @Query(value = "select new api.internalrepository.to.CommerceTo(com.idCommerce, com.commerceDocument, " +
            "com.commerceName, state.stateName, municipality.municipalityName, city.cityDescription, " +
            "com.contactPerson, com.registerDate, status.statusDescription, com.phoneNumberCommerce, " +
            "type, plan,com.updateDate,com.statusCommerce.idStatusCommerce) from CommerceEntity as com " +
            "inner join StatusCommerceEntity as status on status.idStatusCommerce = com.statusCommerce.idStatusCommerce " +
            "inner join TypeCommerceEntity as type on type.idTypeCommerce = com.typeCommerceEntity.idTypeCommerce " +
            "inner join LicenseEntity as license on com.idCommerce = license.commerceEntity.idCommerce " +
            "inner join PlanEntity as plan on plan.idPlan = license.planEntity.idPlan " +
            "left join DirectionEntity as direction on com.directionEntity.idDirection = direction.idDirection " +
            "left join CityEntity as city on direction.cityEntity.idCity = city.idCity " +
            "left join MunicipalityEntity as municipality on city.municipalityEntity.idMunicipality = municipality.idMunicipality " +
            "left join StateEntity as state on municipality.stateEntity.idState = state.idState " +
            "left join CountryEntity as country on state.countryEntity.idCountry = country.idCountry " +
            "where com.statusCommerce.idStatusCommerce IN (2, 6) and cast(com.registerDate as DATE) BETWEEN (:startDate) AND (:endDate) " +
            "order by com.registerDate desc")
    List<CommerceTo> findCommerceByFilterDateExport(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

    @Query(value = "select new api.internalrepository.to.CommerceTo(com.idCommerce, com.commerceDocument, " +
            "com.commerceName, state.stateName, municipality.municipalityName, city.cityDescription, " +
            "com.contactPerson, com.registerDate, status.statusDescription, com.phoneNumberCommerce, " +
            "type, plan,com.updateDate,com.statusCommerce.idStatusCommerce) from CommerceEntity as com " +
            "inner join StatusCommerceEntity as status on status.idStatusCommerce = com.statusCommerce.idStatusCommerce " +
            "inner join TypeCommerceEntity as type on type.idTypeCommerce = com.typeCommerceEntity.idTypeCommerce " +
            "inner join LicenseEntity as license on com.idCommerce = license.commerceEntity.idCommerce " +
            "inner join PlanEntity as plan on plan.idPlan = license.planEntity.idPlan " +
            "left join DirectionEntity as direction on com.directionEntity.idDirection = direction.idDirection " +
            "left join CityEntity as city on direction.cityEntity.idCity = city.idCity " +
            "left join MunicipalityEntity as municipality on city.municipalityEntity.idMunicipality = municipality.idMunicipality " +
            "left join StateEntity as state on municipality.stateEntity.idState = state.idState " +
            "left join CountryEntity as country on state.countryEntity.idCountry = country.idCountry " +
            "where com.statusCommerce.idStatusCommerce IN (2, 6) " +
            "and cast(com.registerDate as DATE) BETWEEN (:startDate) AND (:endDate) " +
            "order by com.registerDate desc")
    List<CommerceTo> findCommerceByFilterDateFilter(@Param("startDate") LocalDate startDate,
                                                    @Param("endDate") LocalDate endDate);

    @Query(value = "select new api.internalrepository.to.CommerceTo(com.idCommerce, com.commerceDocument, " +
            "com.commerceName, state.stateName, municipality.municipalityName, city.cityDescription, " +
            "com.contactPerson, com.registerDate, status.statusDescription, com.phoneNumberCommerce, " +
            "type, plan,com.updateDate,com.statusCommerce.idStatusCommerce) from CommerceEntity as com " +
            "inner join StatusCommerceEntity as status on status.idStatusCommerce = com.statusCommerce.idStatusCommerce " +
            "inner join TypeCommerceEntity as type on type.idTypeCommerce = com.typeCommerceEntity.idTypeCommerce " +
            "inner join LicenseEntity as license on com.idCommerce = license.commerceEntity.idCommerce " +
            "inner join PlanEntity as plan on plan.idPlan = license.planEntity.idPlan " +
            "left join DirectionEntity as direction on com.directionEntity.idDirection = direction.idDirection " +
            "left join CityEntity as city on direction.cityEntity.idCity = city.idCity " +
            "left join MunicipalityEntity as municipality on city.municipalityEntity.idMunicipality = municipality.idMunicipality " +
            "left join StateEntity as state on municipality.stateEntity.idState = state.idState " +
            "left join CountryEntity as country on state.countryEntity.idCountry = country.idCountry " +
            "where lower(com.commerceDocument) like %:filter% or lower(com.commerceName) like %:filter% " +
            "or lower(state.stateName) like %:filter% or lower(com.typeCommerceEntity.typeCommerce) like %:filter% " +
            "or lower(plan.planName) like %:filter% or lower(com.statusCommerce.statusDescription) like %:filter% " +
            "and cast(com.registerDate as DATE) BETWEEN (:startDate) AND (:endDate) " +
            " order by com.registerDate desc")
    List<CommerceTo> getAllCommerceToByFilterAndDate(@Param("filter") String filter,
                                                     @Param("startDate") Date startDate,
                                                     @Param("endDate") Date endDate );

    @Query(value = "select new api.internalrepository.to.CommerceTo(com.idCommerce, com.commerceDocument, " +
            "com.commerceName, state.stateName, municipality.municipalityName, city.cityDescription, " +
            "com.contactPerson, com.registerDate, status.statusDescription, com.phoneNumberCommerce, " +
            "type, plan,com.updateDate,com.statusCommerce.idStatusCommerce) from CommerceEntity as com " +
            "inner join StatusCommerceEntity as status on status.idStatusCommerce = com.statusCommerce.idStatusCommerce " +
            "inner join TypeCommerceEntity as type on type.idTypeCommerce = com.typeCommerceEntity.idTypeCommerce " +
            "inner join LicenseEntity as license on com.idCommerce = license.commerceEntity.idCommerce " +
            "inner join PlanEntity as plan on plan.idPlan = license.planEntity.idPlan " +
            "left join DirectionEntity as direction on com.directionEntity.idDirection = direction.idDirection " +
            "left join CityEntity as city on direction.cityEntity.idCity = city.idCity " +
            "left join MunicipalityEntity as municipality on city.municipalityEntity.idMunicipality = municipality.idMunicipality " +
            "left join StateEntity as state on municipality.stateEntity.idState = state.idState " +
            "left join CountryEntity as country on state.countryEntity.idCountry = country.idCountry " +
            "where lower(com.commerceDocument) like %:filter% or lower(com.commerceName) like %:filter% " +
            "or lower(state.stateName) like %:filter% or lower(com.typeCommerceEntity.typeCommerce) like %:filter% " +
            "or lower(plan.planName) like %:filter% or lower(com.statusCommerce.statusDescription) like %:filter% " +
            "and cast(com.registerDate as DATE) BETWEEN (:startDate) AND (:endDate) " +
            " order by com.registerDate desc")
    Page<CommerceTo> getAllCommerceToByFilterAndDatePageable(Pageable pageable, @Param("filter") String filter,
                                                     @Param("startDate") Date startDate,
                                                     @Param("endDate") Date endDate );

    @Query(value = "select new api.internalrepository.to.CommerceTo(com.idCommerce, com.commerceDocument, " +
            "com.commerceName, state.stateName, municipality.municipalityName, city.cityDescription, " +
            "com.contactPerson, com.registerDate, status.statusDescription, com.phoneNumberCommerce, " +
            "type, plan,com.updateDate,com.statusCommerce.idStatusCommerce) from CommerceEntity as com " +
            "inner join StatusCommerceEntity as status on status.idStatusCommerce = com.statusCommerce.idStatusCommerce " +
            "inner join TypeCommerceEntity as type on type.idTypeCommerce = com.typeCommerceEntity.idTypeCommerce " +
            "inner join LicenseEntity as license on com.idCommerce = license.commerceEntity.idCommerce " +
            "inner join PlanEntity as plan on plan.idPlan = license.planEntity.idPlan " +
            "left join DirectionEntity as direction on com.directionEntity.idDirection = direction.idDirection " +
            "left join CityEntity as city on direction.cityEntity.idCity = city.idCity " +
            "left join MunicipalityEntity as municipality on city.municipalityEntity.idMunicipality = municipality.idMunicipality " +
            "left join StateEntity as state on municipality.stateEntity.idState = state.idState " +
            "left join CountryEntity as country on state.countryEntity.idCountry = country.idCountry " +
            "where (lower(com.commerceDocument) like %:filter% OR :filter IS NULL) " +
            "and cast(com.registerDate as DATE) BETWEEN (:startDate) AND (:endDate) " +
            " order by com.registerDate desc")
    List<CommerceTo> getAllCommerceToByRifFilter(@Param("filter") String filter,
                                                 @Param("startDate") LocalDate startDate,
                                                 @Param("endDate") LocalDate endDate);

    @Query(value = "select new api.internalrepository.to.CommerceTo(com.idCommerce, com.commerceDocument, " +
            "com.commerceName, state.stateName, municipality.municipalityName, city.cityDescription, " +
            "com.contactPerson, com.registerDate, status.statusDescription, com.phoneNumberCommerce, " +
            "type, plan,com.updateDate,com.statusCommerce.idStatusCommerce) from CommerceEntity as com " +
            "inner join StatusCommerceEntity as status on status.idStatusCommerce = com.statusCommerce.idStatusCommerce " +
            "inner join TypeCommerceEntity as type on type.idTypeCommerce = com.typeCommerceEntity.idTypeCommerce " +
            "inner join LicenseEntity as license on com.idCommerce = license.commerceEntity.idCommerce " +
            "inner join PlanEntity as plan on plan.idPlan = license.planEntity.idPlan " +
            "left join DirectionEntity as direction on com.directionEntity.idDirection = direction.idDirection " +
            "left join CityEntity as city on direction.cityEntity.idCity = city.idCity " +
            "left join MunicipalityEntity as municipality on city.municipalityEntity.idMunicipality = municipality.idMunicipality " +
            "left join StateEntity as state on municipality.stateEntity.idState = state.idState " +
            "left join CountryEntity as country on state.countryEntity.idCountry = country.idCountry " +
            "where lower(com.commerceName) like %:filter% " +
            "and cast(com.registerDate as DATE) BETWEEN (:startDate) AND (:endDate) " +
            " order by com.registerDate desc")
    List<CommerceTo> getAllCommerceToByCommerceNameFilter(@Param("filter") String filter,
                                                 @Param("startDate") LocalDate startDate,
                                                 @Param("endDate") LocalDate endDate);

    @Query(value = "select new api.internalrepository.to.CommerceTo(com.idCommerce, com.commerceDocument, " +
            "com.commerceName, state.stateName, municipality.municipalityName, city.cityDescription, " +
            "com.contactPerson, com.registerDate, status.statusDescription, com.phoneNumberCommerce, " +
            "type, plan,com.updateDate,com.statusCommerce.idStatusCommerce) from CommerceEntity as com " +
            "inner join StatusCommerceEntity as status on status.idStatusCommerce = com.statusCommerce.idStatusCommerce " +
            "inner join TypeCommerceEntity as type on type.idTypeCommerce = com.typeCommerceEntity.idTypeCommerce " +
            "inner join LicenseEntity as license on com.idCommerce = license.commerceEntity.idCommerce " +
            "inner join PlanEntity as plan on plan.idPlan = license.planEntity.idPlan " +
            "left join DirectionEntity as direction on com.directionEntity.idDirection = direction.idDirection " +
            "left join CityEntity as city on direction.cityEntity.idCity = city.idCity " +
            "left join MunicipalityEntity as municipality on city.municipalityEntity.idMunicipality = municipality.idMunicipality " +
            "left join StateEntity as state on municipality.stateEntity.idState = state.idState " +
            "left join CountryEntity as country on state.countryEntity.idCountry = country.idCountry " +
            "where com.statusCommerce.idStatusCommerce = :filter " +
            "and cast(com.registerDate as DATE) BETWEEN (:startDate) AND (:endDate) " +
            " order by com.registerDate desc")
    List<CommerceTo> getAllCommerceToByStatusCommerceFilter(@Param("filter") Long filter,
                                                          @Param("startDate") LocalDate startDate,
                                                          @Param("endDate") LocalDate endDate);

    @Query(value = "select new api.internalrepository.to.CommerceTo(com.idCommerce, com.commerceDocument, " +
            "com.commerceName, state.stateName, municipality.municipalityName, city.cityDescription, " +
            "com.contactPerson, com.registerDate, status.statusDescription, com.phoneNumberCommerce, " +
            "type, plan,com.updateDate,com.statusCommerce.idStatusCommerce) from CommerceEntity as com " +
            "inner join StatusCommerceEntity as status on status.idStatusCommerce = com.statusCommerce.idStatusCommerce " +
            "inner join TypeCommerceEntity as type on type.idTypeCommerce = com.typeCommerceEntity.idTypeCommerce " +
            "inner join LicenseEntity as license on com.idCommerce = license.commerceEntity.idCommerce " +
            "inner join PlanEntity as plan on plan.idPlan = license.planEntity.idPlan " +
            "left join DirectionEntity as direction on com.directionEntity.idDirection = direction.idDirection " +
            "left join CityEntity as city on direction.cityEntity.idCity = city.idCity " +
            "left join MunicipalityEntity as municipality on city.municipalityEntity.idMunicipality = municipality.idMunicipality " +
            "left join StateEntity as state on municipality.stateEntity.idState = state.idState " +
            "left join CountryEntity as country on state.countryEntity.idCountry = country.idCountry " +
            "where plan.idPlan = :filter " +
            "and cast(com.registerDate as DATE) BETWEEN (:startDate) AND (:endDate) " +
            " order by com.registerDate desc")
    List<CommerceTo> getAllCommerceToByPlanCommerceFilter(@Param("filter") Long filter,
                                                            @Param("startDate") LocalDate startDate,
                                                            @Param("endDate") LocalDate endDate);

    @Query(value = "select new api.internalrepository.to.CommerceTo(com.idCommerce, com.commerceDocument, " +
            "com.commerceName, state.stateName, municipality.municipalityName, city.cityDescription, " +
            "com.contactPerson, com.registerDate, status.statusDescription, com.phoneNumberCommerce, " +
            "type, plan,com.updateDate,com.statusCommerce.idStatusCommerce) from CommerceEntity as com " +
            "inner join StatusCommerceEntity as status on status.idStatusCommerce = com.statusCommerce.idStatusCommerce " +
            "inner join TypeCommerceEntity as type on type.idTypeCommerce = com.typeCommerceEntity.idTypeCommerce " +
            "inner join LicenseEntity as license on com.idCommerce = license.commerceEntity.idCommerce " +
            "inner join PlanEntity as plan on plan.idPlan = license.planEntity.idPlan " +
            "left join DirectionEntity as direction on com.directionEntity.idDirection = direction.idDirection " +
            "left join CityEntity as city on direction.cityEntity.idCity = city.idCity " +
            "left join MunicipalityEntity as municipality on city.municipalityEntity.idMunicipality = municipality.idMunicipality " +
            "left join StateEntity as state on municipality.stateEntity.idState = state.idState " +
            "left join CountryEntity as country on state.countryEntity.idCountry = country.idCountry " +
            "where com.typeCommerceEntity.idTypeCommerce = :filter " +
            "and cast(com.registerDate as DATE) BETWEEN (:startDate) AND (:endDate) " +
            " order by com.registerDate desc")
    List<CommerceTo> getAllCommerceToByTypeCommerceFilter(@Param("filter") Long filter,
                                                          @Param("startDate") LocalDate startDate,
                                                          @Param("endDate") LocalDate endDate);

    @Query(value = "select new api.internalrepository.to.CommerceTo(com.idCommerce, com.commerceDocument, " +
            "com.commerceName, state.stateName, municipality.municipalityName, city.cityDescription, " +
            "com.contactPerson, com.registerDate, status.statusDescription, com.phoneNumberCommerce, " +
            "type, plan,com.updateDate,com.statusCommerce.idStatusCommerce) from CommerceEntity as com " +
            "inner join StatusCommerceEntity as status on status.idStatusCommerce = com.statusCommerce.idStatusCommerce " +
            "inner join TypeCommerceEntity as type on type.idTypeCommerce = com.typeCommerceEntity.idTypeCommerce " +
            "inner join LicenseEntity as license on com.idCommerce = license.commerceEntity.idCommerce " +
            "inner join PlanEntity as plan on plan.idPlan = license.planEntity.idPlan " +
            "left join DirectionEntity as direction on com.directionEntity.idDirection = direction.idDirection " +
            "left join CityEntity as city on direction.cityEntity.idCity = city.idCity " +
            "left join MunicipalityEntity as municipality on city.municipalityEntity.idMunicipality = municipality.idMunicipality " +
            "left join StateEntity as state on municipality.stateEntity.idState = state.idState " +
            "left join CountryEntity as country on state.countryEntity.idCountry = country.idCountry " +
            "where state.idState = :filter " +
            "and cast(com.registerDate as DATE) BETWEEN (:startDate) AND (:endDate) " +
            " order by com.registerDate desc")
    List<CommerceTo> getAllCommerceToByStateFilter(@Param("filter") Long filter,
                                                          @Param("startDate") LocalDate startDate,
                                                          @Param("endDate") LocalDate endDate);

    @Modifying
    @Query(value = "update CommerceEntity as commerce set commerce.commerceName = :commerceName, " +
            "commerce.contactPerson = :contactPerson, commerce.directionEntity.address = :address, " +
            "commerce.directionEntity.cityEntity.idCity = :idCity, " +
            "commerce.economicActivityEntity.idEconomicActivity = :idEconomicActivity, " +
            "commerce.contactPersonEmail = :contactPersonEmail, " +
            "commerce.phoneNumberCommerce = :phoneNumber, " +
            "commerce.statusCommerce.idStatusCommerce = :idStatusCommerce " +
            "where commerce.idCommerce = :idCommerce")
    void editProfileByCommerce(@Param("idCommerce") final Long idCommerce,
                               @Param("commerceName") final String commerceName,
                               @Param("contactPerson") final String contactPerson,
                               @Param("address") final String address,
                               @Param("idCity") final Long idCity,
                               @Param("idEconomicActivity") final Long idEconomicActivity,
                               @Param("contactPersonEmail") final String contactPersonEmail,
                               @Param("phoneNumber") final String phoneNumber,
                               @Param("idStatusCommerce") final Long idStatusCommerce);

    @Modifying
    @Query(value = "update CommerceEntity as commerce set commerce.commerceName = :commerceName, " +
            "commerce.contactPerson = :contactPerson, commerce.directionEntity.address = :address, " +
            "commerce.directionEntity.cityEntity.idCity = :idCity, " +
            "commerce.economicActivityEntity.idEconomicActivity = :idEconomicActivity, " +
            "commerce.contactPersonEmail = :contactPersonEmail, " +
            "commerce.phoneNumberCommerce = :phoneNumber, " +
            "commerce.statusCommerce.idStatusCommerce = :idStatusCommerce, " +
            "commerce.updateDate = :unlinkDate " +
            "where commerce.idCommerce = :idCommerce")
    void editProfileByCommerceUnlink(@Param("idCommerce") final Long idCommerce,
                                     @Param("commerceName") final String commerceName,
                                     @Param("contactPerson") final String contactPerson,
                                     @Param("address") final String address,
                                     @Param("idCity") final Long idCity,
                                     @Param("idEconomicActivity") final Long idEconomicActivity,
                                     @Param("contactPersonEmail") final String contactPersonEmail,
                                     @Param("phoneNumber") final String phoneNumber,
                                     @Param("idStatusCommerce") final Long idStatusCommerce,
                                     @Param("unlinkDate") final OffsetDateTime unlinkDate);

    @Query(value = "select status from StatusCommerceEntity as status " +
            "where status.idStatusCommerce in (:idStatusCommerce) order by status.idStatusCommerce asc")
    List<StatusCommerceEntity> getAllStatusCommerceByIdStatusCommerceIn(
            @Param("idStatusCommerce") List<Long> idStatusCommerce);

    @Query(value = "select new api.internalrepository.to.CommerceInformationTo(com.idCommerce,com.commerceName,com.commerceDocument) " +
            "FROM CommerceEntity com " +
            "WHERE com.statusCommerce.idStatusCommerce = 2 and com.typeCommerceEntity.idTypeCommerce is not null")
    List<CommerceInformationTo> findAllCommerce();
}
