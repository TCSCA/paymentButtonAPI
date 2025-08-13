package api.externalrepository.service;

import api.externalrepository.entity.RequirementEntity;
import api.externalrepository.repository.RequirementRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RequirementService {

    private final RequirementRepository requirementRepository;

    public RequirementService(RequirementRepository requirementRepository) {
        this.requirementRepository = requirementRepository;
    }

    public List<RequirementEntity> getAllRequirementChargedByIdTypeCommerce(final Long idTypeCommerce,
                                                                            final Long idPreRegister) {
        return requirementRepository.getAllRequirementsChargedByIdTypeCommerce(idTypeCommerce, idPreRegister);
    }

    public List<RequirementEntity> getAllRequirementNoChargedByIdTypeCommerce(final Long idRequirement, final Long idPreRegister) {
        return requirementRepository.getAllRequirementsNoChargedByIdTypeCommerce(idRequirement, idPreRegister);
    }

    public String getRequirementByIdRequirement(final Long idRequirement) {

        RequirementEntity requirementEntity = requirementRepository.
                getRequirementEntityByIdRequirement(idRequirement);

        return requirementEntity.getRecaudo().replace(" ", "");
    }

}
