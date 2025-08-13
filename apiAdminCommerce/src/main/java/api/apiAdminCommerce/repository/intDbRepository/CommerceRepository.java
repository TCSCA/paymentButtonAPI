package api.apiAdminCommerce.repository.intDbRepository;

import api.apiAdminCommerce.entity.CommerceEntity;
import api.apiAdminCommerce.to.CommerceInformationDetailTo;
import api.apiAdminCommerce.to.CommerceTo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface CommerceRepository extends JpaRepository<CommerceEntity, Long> {

    @Query(value = "select new api.apiAdminCommerce.to.CommerceTo(com.idCommerce, com.commerceDocument, " +
            "com.commerceName, state.stateName, municipality.municipalityName, city.cityDescription, " +
            "com.contactPerson, com.registerDate, status.statusDescription, com.phoneNumberCommerce, " +
            "plan.typeCommerceEntity.typeCommerce, plan.planName) from CommerceEntity as com " +
            "inner join StatusCommerceEntity as status on status.idStatusCommerce = com.statusCommerce.idStatusCommerce " +
            "inner join TypeCommerceEntity as type on type.idTypeCommerce = com.typeCommerceEntity.idTypeCommerce " +
            "inner join LicenseEntity as license on com.idCommerce = license.commerceEntity.idCommerce " +
            "inner join PlanEntity as plan on plan.idPlan = license.planEntity.idPlan " +
            "left join DirectionEntity as direction on com.directionEntity.idDirection = direction.idDirection " +
            "left join CityEntity as city on direction.cityEntity.idCity = city.idCity " +
            "left join MunicipalityEntity as municipality on city.municipalityEntity.idMunicipality = municipality.idMunicipality " +
            "left join StateEntity as state on municipality.stateEntity.idState = state.idState " +
            "left join CountryEntity as country on state.countryEntity.idCountry = country.idCountry " +
            "where com.statusCommerce.idStatusCommerce IN (2, 6) order by com.registerDate desc")
    Page<CommerceTo> findAllByStatusCommerce_IdStatus(Pageable pageable);

    @Query(value = "select new api.apiAdminCommerce.to.CommerceTo(com.idCommerce, com.commerceDocument, " +
            "com.commerceName, state.stateName, municipality.municipalityName, city.cityDescription, " +
            "com.contactPerson, com.registerDate, status.statusDescription, com.phoneNumberCommerce, " +
            "type.typeCommerce, plan.planName) from CommerceEntity as com " +
            "inner join StatusCommerceEntity as status on status.idStatusCommerce = com.statusCommerce.idStatusCommerce " +
            "inner join TypeCommerceEntity as type on type.idTypeCommerce = com.typeCommerceEntity.idTypeCommerce " +
            "inner join LicenseEntity as license on com.idCommerce = license.commerceEntity.idCommerce " +
            "inner join PlanEntity as plan on plan.idPlan = license.planEntity.idPlan " +
            "left join DirectionEntity as direction on com.directionEntity.idDirection = direction.idDirection " +
            "left join CityEntity as city on direction.cityEntity.idCity = city.idCity " +
            "left join MunicipalityEntity as municipality on city.municipalityEntity.idMunicipality = municipality.idMunicipality " +
            "left join StateEntity as state on municipality.stateEntity.idState = state.idState " +
            "left join CountryEntity as country on state.countryEntity.idCountry = country.idCountry " +
            "where com.statusCommerce.idStatusCommerce IN (2, 6) order by com.registerDate desc")
    List<CommerceTo> findAllByStatusCommerce_IdStatusExport();

    @Query(value = "select new api.apiAdminCommerce.to.CommerceInformationDetailTo(com.idCommerce, " +
            "com.registerDate, status.statusDescription, com.commerceDocument, " +
            "com.commerceName, com.contactPerson, com.phoneNumberCommerce, com.commerceEmail, " +
            "city, " +
            "direction.address, bankCommerce.bankEntity.bankName, bankCommerce.bankAccount," +
            "com.typeCommerceEntity.typeCommerce, plan.planName ) from CommerceEntity as com " +
            "inner join StatusCommerceEntity as status on status.idStatusCommerce = com.statusCommerce.idStatusCommerce " +
            "inner join ClientEntity as client on com.idCommerce = client.commerceEntity.idCommerce " +
            "inner join UserEntity as user on user.idUser = client.userEntity.idUser " +
            "left join DirectionEntity as direction on com.directionEntity.idDirection = direction.idDirection " +
            "left join CityEntity as city on direction.cityEntity.idCity = city.idCity " +
            "left join MunicipalityEntity as municipality on city.municipalityEntity.idMunicipality = municipality.idMunicipality " +
            "left join StateEntity as state on municipality.stateEntity.idState = state.idState " +
            "left join CountryEntity as country on state.countryEntity.idCountry = country.idCountry " +
            "inner join LicenseEntity as license on com.idCommerce = license.commerceEntity.idCommerce " +
            "inner join PlanEntity as plan on plan.idPlan = license.planEntity.idPlan " +
            "inner join BankCommerceEntity as bankCommerce on com.idCommerce = bankCommerce.commerceEntity.idCommerce " +
            "where user.idUser = :idUser " +
            "and bankCommerce.status = true " +
            "and bankCommerce.bankEntity.idBank <> 99")
    CommerceInformationDetailTo findCommerceByIdUser(@Param("idUser") final Long idUser);

    @Query(value = "select new api.apiAdminCommerce.to.CommerceInformationDetailTo(com.idCommerce, " +
            "com.registerDate, status.statusDescription, com.commerceDocument, " +
            "com.commerceName, com.contactPerson, com.phoneNumberCommerce, com.commerceEmail, " +
            "city,direction.address, " +
            "com.typeCommerceEntity.typeCommerce, plan.planName ) from CommerceEntity as com " +
            "inner join StatusCommerceEntity as status on status.idStatusCommerce = com.statusCommerce.idStatusCommerce " +
            "inner join ClientEntity as client on com.idCommerce = client.commerceEntity.idCommerce " +
            "inner join UserEntity as user on user.idUser = client.userEntity.idUser " +
            "left join DirectionEntity as direction on com.directionEntity.idDirection = direction.idDirection " +
            "left join CityEntity as city on direction.cityEntity.idCity = city.idCity " +
            "left join MunicipalityEntity as municipality on city.municipalityEntity.idMunicipality = municipality.idMunicipality " +
            "left join StateEntity as state on municipality.stateEntity.idState = state.idState " +
            "left join CountryEntity as country on state.countryEntity.idCountry = country.idCountry " +
            "inner join LicenseEntity as license on com.idCommerce = license.commerceEntity.idCommerce " +
            "inner join PlanEntity as plan on plan.idPlan = license.planEntity.idPlan " +
            "where user.idUser = :idUser ")
    CommerceInformationDetailTo findCommerceByIdUserNoBankInformation(@Param("idUser") final Long idUser);

    CommerceEntity findByCommerceDocument(String rif);

    @Query(value = "select distinct new api.apiAdminCommerce.to.CommerceInformationDetailTo(com.idCommerce, " +
            "com.registerDate, status.statusDescription, com.commerceDocument, " +
            "com.commerceName, com.contactPerson, com.phoneNumberCommerce, com.commerceEmail, " +
            "city, " +
            "direction.address, bankCommerce.bankEntity.bankName, bankCommerce.bankAccount, " +
            "com.typeCommerceEntity.typeCommerce, plan.planName) from CommerceEntity as com " +
            "inner join StatusCommerceEntity as status on status.idStatusCommerce = com.statusCommerce.idStatusCommerce " +
            "left join DirectionEntity as direction on com.directionEntity.idDirection = direction.idDirection " +
            "left join CityEntity as city on direction.cityEntity.idCity = city.idCity " +
            "left join MunicipalityEntity as municipality on city.municipalityEntity.idMunicipality = municipality.idMunicipality " +
            "left join StateEntity as state on municipality.stateEntity.idState = state.idState " +
            "left join CountryEntity as country on state.countryEntity.idCountry = country.idCountry " +
            "inner join LicenseEntity as license on com.idCommerce = license.commerceEntity.idCommerce " +
            "inner join PlanEntity as plan on plan.idPlan = license.planEntity.idPlan " +
            "inner join BankCommerceEntity as bankCommerce on com.idCommerce = bankCommerce.commerceEntity.idCommerce " +
            "where com.commerceDocument like :rif " +
            "and bankCommerce.status = true " +
            "and bankCommerce.bankEntity.idBank <> 99")
    CommerceInformationDetailTo findCommerceByCommerceDocument(@Param("rif") final String rif);

    @Query(value = "select distinct new api.apiAdminCommerce.to.CommerceInformationDetailTo(com.idCommerce, " +
            "com.registerDate, status.statusDescription, com.commerceDocument, " +
            "com.commerceName, com.contactPerson, com.phoneNumberCommerce, com.commerceEmail, " +
            "city, direction.address, com.typeCommerceEntity.typeCommerce, plan.planName) from CommerceEntity as com " +
            "inner join StatusCommerceEntity as status on status.idStatusCommerce = com.statusCommerce.idStatusCommerce " +
            "left join DirectionEntity as direction on com.directionEntity.idDirection = direction.idDirection " +
            "left join CityEntity as city on direction.cityEntity.idCity = city.idCity " +
            "left join MunicipalityEntity as municipality on city.municipalityEntity.idMunicipality = municipality.idMunicipality " +
            "left join StateEntity as state on municipality.stateEntity.idState = state.idState " +
            "left join CountryEntity as country on state.countryEntity.idCountry = country.idCountry " +
            "inner join LicenseEntity as license on com.idCommerce = license.commerceEntity.idCommerce " +
            "inner join PlanEntity as plan on plan.idPlan = license.planEntity.idPlan " +
            "where com.commerceDocument like :rif ")
    CommerceInformationDetailTo findCommerceByCommerceDocumentNoBankInformation(@Param("rif") final String rif);

    @Query(value = "select new api.apiAdminCommerce.to.CommerceTo(com.idCommerce, com.commerceDocument, " +
            "com.commerceName, state.stateName, municipality.municipalityName, city.cityDescription, " +
            "com.contactPerson, com.registerDate, status.statusDescription, com.phoneNumberCommerce, " +
            "type.typeCommerce, plan.planName) from CommerceEntity as com " +
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
            "order by com.registerDate desc")
    List<CommerceTo> getAllCommerceToByFilter(@Param("filter") String filter);

    @Query(value = "select new api.apiAdminCommerce.to.CommerceTo(com.idCommerce, com.commerceDocument, " +
            "com.commerceName, state.stateName, municipality.municipalityName, city.cityDescription, " +
            "com.contactPerson, com.registerDate, status.statusDescription, com.phoneNumberCommerce, " +
            "type.typeCommerce, plan.planName) from CommerceEntity as com " +
            "inner join StatusCommerceEntity as status on status.idStatusCommerce = com.statusCommerce.idStatusCommerce " +
            "inner join TypeCommerceEntity as type on type.idTypeCommerce = com.typeCommerceEntity.idTypeCommerce " +
            "inner join LicenseEntity as license on com.idCommerce = license.commerceEntity.idCommerce " +
            "inner join PlanEntity as plan on plan.idPlan = license.planEntity.idPlan " +
            "left join DirectionEntity as direction on com.directionEntity.idDirection = direction.idDirection " +
            "left join CityEntity as city on direction.cityEntity.idCity = city.idCity " +
            "left join MunicipalityEntity as municipality on city.municipalityEntity.idMunicipality = municipality.idMunicipality " +
            "left join StateEntity as state on municipality.stateEntity.idState = state.idState " +
            "left join CountryEntity as country on state.countryEntity.idCountry = country.idCountry " +
            "where lower(com.commerceDocument) like lower(:filter) or lower(com.commerceName) like lower(:filter) " +
            "or lower(state.stateName) like lower(:filter) or lower(com.typeCommerceEntity.typeCommerce) like lower(:filter) " +
            "or lower(plan.planName) like lower(:filter) or lower(com.statusCommerce.statusDescription) like lower(:filter)" +
            " order by com.registerDate desc")
    Page<CommerceTo> getAllCommerceToByFilterPageable(Pageable pageable, @Param("filter") String filter);

}
