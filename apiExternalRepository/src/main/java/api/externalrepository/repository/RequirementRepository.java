package api.externalrepository.repository;

import api.externalrepository.entity.RequirementEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RequirementRepository extends JpaRepository<RequirementEntity, Long> {

    @Query(value = "select requirement from RequirementEntity as requirement " +
            "where requirement.idRequirement = :idRequirement ")
    RequirementEntity getRequirementEntityByIdRequirement(@Param("idRequirement") final Long idRequirement);

    @Query(value = "select requirement from RequirementEntity as requirement " +
            "inner join TypeCommerceEntity as type on type.idTypeCommerce = requirement.typeCommerceEntity.idTypeCommerce " +
            "left join ResultFileEntity as file on requirement.idRequirement = file.requirementEntity.idRequirement " +
            "and file.preRegisterEntity.idPreRegistro = :idPreRegister " +
            "where type.idTypeCommerce = :idTypeCommerce and file.chargedDate is null " +
            "order by requirement.required desc, requirement.idRequirement asc")
    List<RequirementEntity> getAllRequirementsNoChargedByIdTypeCommerce(
            @Param("idTypeCommerce") final Long idTypeCommerce,
            @Param("idPreRegister") final Long idPreRegister);

    @Query(value = "select requirement from RequirementEntity as requirement " +
            "inner join TypeCommerceEntity as type on type.idTypeCommerce = requirement.typeCommerceEntity.idTypeCommerce " +
            "inner join ResultFileEntity as file on requirement.idRequirement = file.requirementEntity.idRequirement " +
            "and file.preRegisterEntity.idPreRegistro = :idPreRegister " +
            "where type.idTypeCommerce = :idTypeCommerce and file.chargedDate is not null " +
            "order by requirement.required desc, requirement.idRequirement asc")
    List<RequirementEntity> getAllRequirementsChargedByIdTypeCommerce(
            @Param("idTypeCommerce") final Long idTypeCommerce,
            @Param("idPreRegister") final Long idPreRegister);

    @Query(value = "select case when count(requirement) = 0 then true else false end " +
            "from RequirementEntity as requirement " +
            "inner join TypeCommerceEntity as type on type.idTypeCommerce = requirement.typeCommerceEntity.idTypeCommerce " +
            "inner join PlanEntity as plan on plan.typeCommerceEntity.idTypeCommerce = type.idTypeCommerce " +
            "inner join PreRegisterEntity as preRegister on preRegister.planEntity.idPlan = plan.idPlan " +
            "left join ResultFileEntity as file on file.preRegisterEntity.idPreRegistro = preRegister.idPreRegistro " +
            "and file.requirementEntity.idRequirement = requirement.idRequirement " +
            "where preRegister.idPreRegistro = :idPreRegister " +
            "and requirement.required = true " +
            "and (file.idResultFile is null or file.chargedDate is null)")
    Boolean existsUnchargedRequiredRequirements(
            @Param("idPreRegister") final Long idPreRegister);

}
