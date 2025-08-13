package api.externalrepository.service;

import api.externalrepository.entity.DirectionEntity;
import api.externalrepository.entity.PlanEntity;
import api.externalrepository.entity.PreRegisterEntity;
import api.externalrepository.entity.StateEntity;
import api.externalrepository.entity.TypeCommerceEntity;
import api.externalrepository.repository.DirectionRepository;
import api.externalrepository.repository.PlanRepository;
import api.externalrepository.repository.PreRegisterRepository;
import api.externalrepository.repository.RequirementRepository;
import api.externalrepository.repository.StateRepository;
import api.externalrepository.repository.TypeCommerceRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.List;

@Service
public class PreRegisterService {

    private final PreRegisterRepository preRegisterRepository;

    private final TypeCommerceRepository typeCommerceRepository;

    private final PlanRepository planRepository;

    private final RequirementRepository requirementRepository;

    private final DirectionRepository directionRepository;

    private final StateRepository stateRepository;

    public PreRegisterService(PreRegisterRepository preRegisterRepository, TypeCommerceRepository typeCommerceRepository, PlanRepository planRepository, RequirementRepository requirementRepository, DirectionRepository directionRepository, StateRepository stateRepository) {
        this.preRegisterRepository = preRegisterRepository;
        this.typeCommerceRepository = typeCommerceRepository;
        this.planRepository = planRepository;
        this.requirementRepository = requirementRepository;
        this.directionRepository = directionRepository;
        this.stateRepository = stateRepository;
    }

    @Transactional
    public Boolean setStatusPreRegisterExt(Long idStatus, Long idPreRegister) {
        try {
            if(idStatus == 2L) {
                preRegisterRepository.updateStatusPreRegisterExt(idStatus, idPreRegister);
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Transactional
    public PreRegisterEntity getPreRegisterById(Long idPreRegister) {
        return preRegisterRepository.findByIdPreRegistro(idPreRegister);
    }

    @Transactional
    public PreRegisterEntity getPreRegisterByRif(String rif) {
        return preRegisterRepository.findByCommerceDocument(rif);
    }

    @Transactional
    public Boolean changePreRegisterStatus(Long idPreRegister, Long statusPreRegister, String rejectMotive) {
        try {
            OffsetDateTime updateDate = OffsetDateTime.now(ZoneId.of("America/Caracas"));
            preRegisterRepository.updateStatusPreRegisterExtRejected(statusPreRegister,
                    idPreRegister, rejectMotive,updateDate);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Page<PreRegisterEntity> getAllPreRegistersByStatus(final Pageable pageable, final Long idStatus) {
        return preRegisterRepository.
                findByStatusPreRegisterEntity_IdStatusPreRegisterOrderByUpdateDateDesc(pageable, idStatus);
    }

    public List<PreRegisterEntity> getAllPreRegistersByStatusExport(final Long idStatus) {
        return preRegisterRepository.
                getPreregisterByStatusExport(idStatus);
    }

    public List<TypeCommerceEntity> getAllTypeCommerce() {
        return typeCommerceRepository.findAll();
    }

    public List<PlanEntity> getAllPlanByIdTypeCommerce(final Long idTypeCommerce) {
        return planRepository.getAllPlanEntityByIdTypeCommerce(idTypeCommerce);
    }

    public Boolean validateChargedRequiredFilesByIdPreRegister(final Long idPreRegister) {
        return requirementRepository.existsUnchargedRequiredRequirements(idPreRegister);
    }

    public Page<PreRegisterEntity> getAllPreRegistersByDate(final Pageable pageable, final Date startDate, final Date endDate) {
        return preRegisterRepository.getPreRegisterInformationByDate(pageable, startDate,endDate);
    }

    public List<PreRegisterEntity> getAllPreRegistersByDateExport(final Date startDate, final Date endDate) {
        return preRegisterRepository.getPreRegisterInformationByDateExport(startDate,endDate);
    }

    public List<PreRegisterEntity> getAllCommercesByRejectedDateExport(final Date startDate, final Date endDate) {
        return preRegisterRepository.getCommerceRejectedInformationByDateExport(startDate,endDate);
    }

    public Page<PreRegisterEntity> getAllCommercesByRejectedDate(final Pageable pageable, final Date startDate, final Date endDate) {
        return preRegisterRepository.getCommerceRejectedInformationByDate(pageable, startDate,endDate);
    }

    public String editProfileByPreRegister(final Long idPreRegister, final String commerceName,
                                                      final String commerceDocument, final String contactPerson,
                                                      final String address, final Long idState,
                                                      final String contactPersonEmail,
                                                      final String phoneNumber, final Long idPlan) {

        if (preRegisterRepository.
                findByCommerceDocumentAndStatus(idPreRegister, commerceDocument, 1L) != null) {
            return "El RIF ingresado ya se encuentra preRegistrado";
        } else if (preRegisterRepository.
                findByCommerceDocumentAndStatus(idPreRegister, commerceDocument, 3L) != null) {
            return "El RIF ingresado pertenece a un preRegistro que fue rechazado";
        }

        PreRegisterEntity preRegisterEntity = preRegisterRepository.findByIdPreRegistro(idPreRegister);
        PlanEntity planEntity = planRepository.findByIdPlan(idPlan);

        preRegisterEntity.setCommerceName(commerceName);
        preRegisterEntity.setCommerceDocument(commerceDocument);
        preRegisterEntity.setContactPerson(contactPerson);
        preRegisterEntity.setContactPersonEmail(contactPersonEmail);
        preRegisterEntity.setPhoneNumberCommerce(phoneNumber);
        preRegisterEntity.setPlanEntity(planEntity);

        DirectionEntity directionEntity = preRegisterEntity.getDirectionEntity();
        StateEntity stateEntity = stateRepository.findByIdState(idState);
        directionEntity.setAddress(address);
        directionEntity.setStateEntity(stateEntity);

        preRegisterRepository.save(preRegisterEntity);
        directionRepository.save(directionEntity);

        return "Transacción exitosa";
    }

}
