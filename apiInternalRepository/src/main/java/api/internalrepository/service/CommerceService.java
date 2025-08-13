package api.internalrepository.service;

import api.internalrepository.entity.*;
import api.internalrepository.repository.*;
import api.internalrepository.to.*;
import api.internalrepository.to.CommerceBankInformationTo;
import api.internalrepository.to.CommerceTo;
import api.internalrepository.to.RegisterCommerceTo;
import api.internalrepository.to.SupportContactTo;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class CommerceService {
    private final UserService userService;

    private final CommerceRepository commerceRepository;

    private final DirectionRepository directionRepository;

    private final LicenseRepository licenseRepository;

    private final ClientRepository clientRepository;

    private final BankRepository bankRepository;

    private final SupportRepository supportRepository;

    private final PlanRepository planRepository;

    private final UnlinkCommerceRepository unlinkCommerceRepository;

    private final ConfigurationRepository configurationRepository;

    private final MessageApiRepository messageApiRepository;


    public CommerceService(UserService userService,
                           CommerceRepository commerceRepository,
                           DirectionRepository directionRepository,
                           LicenseRepository licenseRepository,
                           ClientRepository clientRepository,
                           BankRepository bankRepository,
                           SupportRepository supportRepository,
                           PlanRepository planRepository, UnlinkCommerceRepository unlinkCommerceRepository,
                           ConfigurationRepository configurationRepository, MessageApiRepository messageApiRepository) {
        this.userService = userService;
        this.commerceRepository = commerceRepository;
        this.directionRepository = directionRepository;
        this.licenseRepository = licenseRepository;
        this.clientRepository = clientRepository;
        this.bankRepository = bankRepository;
        this.supportRepository = supportRepository;
        this.planRepository = planRepository;
        this.unlinkCommerceRepository = unlinkCommerceRepository;
        this.configurationRepository = configurationRepository;
        this.messageApiRepository = messageApiRepository;
    }

    @Transactional
    public Boolean saveCommerce(final RegisterCommerceTo registerCommerceTo, final Long idEconomicActivity,
                                final Long idCity) {

        try {
            CommerceEntity commerceEntity = new CommerceEntity();
            DirectionEntity directionEntity = new DirectionEntity();

            commerceEntity.setCommerceName(registerCommerceTo.getCommerceName());
            commerceEntity.setCommerceDocument(registerCommerceTo.getCommerceDocument());
            commerceEntity.setCommerceEmail(registerCommerceTo.getCommerceEmail());
            commerceEntity.setPhoneNumberCommerce(registerCommerceTo.getPhoneNumberCommerce());
            commerceEntity.setContactPerson(registerCommerceTo.getContactPerson());
            commerceEntity.setContactPersonEmail(registerCommerceTo.getContactPersonEmail());
            commerceEntity.setTypeCommerceEntity(new TypeCommerceEntity(registerCommerceTo.getIdTypeCommerce()));
            commerceEntity.setEconomicActivityEntity(new EconomicActivityEntity(idEconomicActivity));
            commerceEntity.setStatusCommerce(new StatusCommerceEntity(2L));

            directionEntity.setStatus(true);
            directionEntity.setCityEntity(new CityEntity(idCity));
            directionEntity.setAddress(registerCommerceTo.getAddress());
            directionRepository.save(directionEntity);

            commerceEntity.setDirectionEntity(directionEntity);
            commerceEntity.setRegisterDate(OffsetDateTime.now(ZoneId.of("America/Caracas")));
            commerceEntity.setUpdateDate(OffsetDateTime.now(ZoneId.of("America/Caracas")));
            commerceRepository.save(commerceEntity);
            return true;

        } catch (Exception e) {
            return false;
        }

    }

    public CommerceEntity getCommerceById(final Long idCommerce) {
        return commerceRepository.findByIdCommerce(idCommerce);
    }

    public List<CommerceEntity> getAllActiveCommerce() {
        return commerceRepository.findAllActiveCommerce();
    }

    public CommerceEntity getCommerceByDocumentAndStatus(final String commerceDocument,
                                                         final Long idStatusCommerce) {
        return commerceRepository.
                findByCommerceDocumentAndStatusCommerce_IdStatusCommerce(commerceDocument,
                        idStatusCommerce);
    }

    public CommerceEntity getCommerceInformationDetailByRif(final String commerceDocument) {
        return commerceRepository.findCommerceByCommerceDocument(commerceDocument);
    }

    public CommerceEntity getCommerceInformationDetailByIdUser(final Long idUser) {
        return commerceRepository.findCommerceByIdUser(idUser);
    }

    public PlanEntity getPlanByIdCommerce(final Long idCommerce) {
        return commerceRepository.getPlanByIdCommerce(idCommerce);
    }

    @Transactional
    public Boolean generateLicence(final Long idCommerce, final Long idPlan) {

        String uuidAsString;
        try {
            LicenseEntity licenseEntity = new LicenseEntity();
            LocalDate currentDate = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;
            /*LocalDate activationLocalDate = LocalDate.parse(activationDate, formatter);*/
            Long yearExpiration = Long.valueOf(configurationRepository.findByPassword("licenceExpiration").getValue());

            UUID uuid = UUID.randomUUID();
            uuidAsString = uuid.toString();
            uuidAsString = uuidAsString.substring(0, 8);

            CommerceEntity commerceEntity = commerceRepository.findByIdCommerce(idCommerce);

            licenseEntity.setCommerceEntity(commerceEntity);
            licenseEntity.setLicenseCode(uuidAsString);
            StatusLicenseEntity statusLicenseEntity = new StatusLicenseEntity();
            statusLicenseEntity.setIdStatusLicense(1L);
            licenseEntity.setStatusLicenseEntity(statusLicenseEntity);
            licenseEntity.setEmisionDate(currentDate);
            licenseEntity.setExpireDate(currentDate.plusYears(yearExpiration));
            licenseEntity.setPlanEntity(new PlanEntity(idPlan));
            /*licenseEntity.setActivationDate(activationLocalDate);*/

            licenseRepository.save(licenseEntity);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public List<BankEntity> getAllBanksByStatusTrue() {
        return bankRepository.findAllByStatusTrue();
    }

    public SupportContactTo getSupportContactInformation(final Long idUser,final String phoneNumberCommerce
    ,final String contactPersonEmail,final String description){

        SupportEntity supportEntity = new SupportEntity();
        supportEntity.setPhoneContactPerson(phoneNumberCommerce);
        supportEntity.setContactEmail(contactPersonEmail);
        supportEntity.setRequest(description);
        supportEntity.setRegisterDate(OffsetDateTime.now());
        supportEntity.setUpdateDate(OffsetDateTime.now());
        supportEntity.setClientEntity(clientRepository.findByUserEntity_IdUser(idUser));
        supportRepository.save(supportEntity);
        List<SupportContactTo> supportContacts = clientRepository.findByIdUser(idUser);

        SupportContactTo supportContactTo = supportContacts.get(supportContacts.size() - 1);

        supportEntity.setClientEntity(supportContactTo.getClientEntity());

        supportContactTo.setClientEntity(supportEntity.getClientEntity());



        return supportContactTo;

    }

    public CommerceBankInformationTo getCommerceBankInformation(final String rif) {
       CommerceBankInformationTo commerceBankInformationTo = commerceRepository.getCommerceBankInformation(rif);
       return commerceBankInformationTo;
    }


    public Page<CommerceTo> getAllCommerceByDate(final Pageable pageable, final Date startDate, final Date endDate, final String filter) {

        if(filter != null) {
            return commerceRepository.getAllCommerceToByFilterAndDatePageable(pageable, filter, startDate,endDate);
        }

        return commerceRepository.findCommerceByFilterDate(pageable, startDate,endDate);
    }

    public List<CommerceTo> getAllCommerceByDateExport(final Date startDate, final Date endDate, final String filter) {

        if(filter != null) {
            return commerceRepository.getAllCommerceToByFilterAndDate(filter, startDate, endDate);
        }
        return commerceRepository.findCommerceByFilterDateExport( startDate,endDate);
    }

    public CommerceEntity editProfileByCommerce(final Long idCommerce, final String commerceName,
                                                final String contactPerson, final String contactPersonEmail,
                                                final String phoneNumber, final Long idStatusCommerce,
                                                final Long idPlan, final String reasonStatus ,final Long idCity,
                                                final String address, final Long idCommerceActivity, final String activationDate,
                                                final HttpServletRequest httpServletRequest) {

        CommerceEntity commerceEntity = commerceRepository.findByIdCommerce(idCommerce);

        if (commerceEntity.getStatusCommerce().getIdStatusCommerce() == 6 && idStatusCommerce != 6) {

            commerceEntity.setCommerceName(commerceName);
            commerceEntity.setContactPerson(contactPerson);
            commerceEntity.setContactPersonEmail(contactPersonEmail);
            commerceEntity.setPhoneNumberCommerce(phoneNumber);
            commerceEntity.setEconomicActivityEntity(new EconomicActivityEntity(idCommerceActivity));
            commerceEntity.setStatusCommerce(new StatusCommerceEntity(idStatusCommerce));

            PlanEntity planEntity = planRepository.findByIdPlan(idPlan);

            LicenseEntity licenseEntity = licenseRepository.getLicenseEntityActiveByIdCommerce(idCommerce);
            licenseEntity.setPlanEntity(planEntity);
            if(activationDate != null) {
                DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;
                LocalDate activationLocalDate = LocalDate.parse(activationDate, formatter);
                licenseEntity.setActivationDate(activationLocalDate);
            }

            commerceEntity.setTypeCommerceEntity(planEntity.getTypeCommerceEntity());

            DirectionEntity directionEntity = commerceEntity.getDirectionEntity();
            directionEntity.setCityEntity(new CityEntity(idCity));
            directionEntity.setAddress(address);

            licenseRepository.save(licenseEntity);
            directionRepository.save(directionEntity);
            commerceRepository.save(commerceEntity);

            userService.updateUserStatusByRif(commerceEntity, 1L, reasonStatus,
                    httpServletRequest);

        }

        if (idStatusCommerce == 6) {

            commerceEntity.setCommerceName(commerceName);
            commerceEntity.setContactPerson(contactPerson);
            commerceEntity.setContactPersonEmail(contactPersonEmail);
            commerceEntity.setPhoneNumberCommerce(phoneNumber);
            commerceEntity.setStatusCommerce(new StatusCommerceEntity(idStatusCommerce));

            PlanEntity planEntity = planRepository.findByIdPlan(idPlan);

            LicenseEntity licenseEntity = licenseRepository.getLicenseEntityActiveByIdCommerce(idCommerce);
            licenseEntity.setPlanEntity(planEntity);
            licenseEntity.setPlanEntity(planEntity);
            if(activationDate != null) {
                DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;
                LocalDate activationLocalDate = LocalDate.parse(activationDate, formatter);
                licenseEntity.setActivationDate(activationLocalDate);
            }


            commerceEntity.setTypeCommerceEntity(planEntity.getTypeCommerceEntity());

            DirectionEntity directionEntity = commerceEntity.getDirectionEntity();
            directionEntity.setCityEntity(new CityEntity(idCity));
            directionEntity.setAddress(address);

            licenseRepository.save(licenseEntity);
            directionRepository.save(directionEntity);
            commerceRepository.save(commerceEntity);

            updateUnlinkCommerce(commerceEntity.getIdCommerce(), reasonStatus);
            userService.updateUserStatusByRif(commerceEntity, 3L, reasonStatus,
                    httpServletRequest);

        } else {

            commerceEntity.setCommerceName(commerceName);
            commerceEntity.setContactPerson(contactPerson);
            commerceEntity.setContactPersonEmail(contactPersonEmail);
            commerceEntity.setPhoneNumberCommerce(phoneNumber);
            commerceEntity.setStatusCommerce(new StatusCommerceEntity(idStatusCommerce));

            PlanEntity planEntity = planRepository.findByIdPlan(idPlan);

            LicenseEntity licenseEntity = licenseRepository.getLicenseEntityActiveByIdCommerce(idCommerce);
            licenseEntity.setPlanEntity(planEntity);
            licenseEntity.setPlanEntity(planEntity);
            if(activationDate != null) {
                DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;
                LocalDate activationLocalDate = LocalDate.parse(activationDate, formatter);
                licenseEntity.setActivationDate(activationLocalDate);
            }

            commerceEntity.setTypeCommerceEntity(planEntity.getTypeCommerceEntity());

            DirectionEntity directionEntity = commerceEntity.getDirectionEntity();
            directionEntity.setCityEntity(new CityEntity(idCity));
            directionEntity.setAddress(address);

            licenseRepository.save(licenseEntity);
            directionRepository.save(directionEntity);
            commerceRepository.save(commerceEntity);

        }

        return commerceRepository.findByIdCommerce(idCommerce);
    }

    public List<StatusCommerceEntity> getAllStatusCommerceByIdStatus(final Long idStatusCommerce) {

        List<Long> idStatusCommerceList = new ArrayList<>();

        /**
         * Logica para que si se esta editando un comercio aprobado solo mostrar
         * en la lista de status Aprobado y Desvinculado
         */
        if (idStatusCommerce == 2){
            idStatusCommerceList.add(2L);
            idStatusCommerceList.add(6L);
        }

        if (idStatusCommerce == 6){
            idStatusCommerceList.add(2L);
            idStatusCommerceList.add(6L);
        }

        return commerceRepository.getAllStatusCommerceByIdStatusCommerceIn(idStatusCommerceList);
    }

    public CommerceEntity validateExistByCommerceDocument(final String commerceDocument) {
        return commerceRepository.findByCommerceDocument(commerceDocument);
    }

    public String validateCommerceLicence(final String rif) {
        LocalDate currentDate = LocalDate.now();
        CommerceEntity commerceEntity = commerceRepository.getLicenceValidByCommerce(rif, currentDate);

        if(commerceEntity == null) {
            MessageApiEntity messageApiEntityLicenseExpired = messageApiRepository.findByCodeAndStatusTrue("MSG-112");
            return messageApiEntityLicenseExpired.getMessage();
        }

        CommerceEntity isActivated = commerceRepository.getLicenceActivationDate(rif, currentDate);

        if(isActivated == null) {
            MessageApiEntity messageApiEntityActivated = messageApiRepository.findByCodeAndStatusTrue("MSG-111");
            return messageApiEntityActivated.getMessage();
        }

        return "SUCCESS";

    }

    public void updateUnlinkCommerce(final Long idCommerce, final String unlinkReason) {

        UnlinkCommerceEntity unlinkCommerceEntityExist = unlinkCommerceRepository.
                findUnlinkCommerceByIdCommerceAndStatusIsTrue(idCommerce);

        UnlinkCommerceEntity unlinkCommerceEntity = new UnlinkCommerceEntity();

        if (unlinkCommerceEntityExist != null) {
            unlinkCommerceEntityExist.setStatus(false);
            unlinkCommerceRepository.save(unlinkCommerceEntityExist);
        }

        unlinkCommerceEntity.setUnlinkReason(unlinkReason);
        unlinkCommerceEntity.setCommerceEntity(commerceRepository.findByIdCommerce(idCommerce));
        unlinkCommerceEntity.setRegisterDate(OffsetDateTime.now(ZoneId.of("America/Caracas")));
        unlinkCommerceEntity.setUpdateDate(OffsetDateTime.now(ZoneId.of("America/Caracas")));
        unlinkCommerceEntity.setStatus(true);
        unlinkCommerceRepository.save(unlinkCommerceEntity);

    }


    public List<CommerceInformationTo> findAllCommerces(){
        return commerceRepository.findAllCommerce();
    }

    public LicenseEntity findLicenceByCommerce(final Long idCommerce) {
        return licenseRepository.getLicenseEntityByIdCommerce(idCommerce);
    }
}
