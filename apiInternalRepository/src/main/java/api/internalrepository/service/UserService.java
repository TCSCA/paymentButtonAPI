package api.internalrepository.service;

import api.internalrepository.entity.*;
import api.internalrepository.repository.*;
import api.internalrepository.to.AdministrativesUserTo;
import api.internalrepository.to.UsersByCommerceTo;
import api.internalrepository.util.Response;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;


@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final TermsAndConditionsRepository termsAndConditionsRepository;

    private final ConfigurationRepository configurationRepository;

    private final UserRepository userRepository;

    private final ClientRepository clientRepository;

    private final CommerceRepository commerceRepository;

    private final ApprovalUserRepository approvalUserRepository;

    private final HistoryStatusUserRepository historyStatusUserRepository;

    private final AdministrativeUserRepository administrativeUserRepository;

    private final TokenRepository tokenRepository;

    public UserService(TermsAndConditionsRepository termsAndConditionsRepository, ConfigurationRepository configurationRepository, UserRepository userRepository, ClientRepository clientRepository, CommerceRepository commerceRepository, ApprovalUserRepository approvalUserRepository, HistoryStatusUserRepository historyStatusUserRepository, AdministrativeUserRepository administrativeUserRepository, TokenRepository tokenRepository) {
        this.termsAndConditionsRepository = termsAndConditionsRepository;
        this.configurationRepository = configurationRepository;
        this.userRepository = userRepository;
        this.clientRepository = clientRepository;
        this.commerceRepository = commerceRepository;
        this.approvalUserRepository = approvalUserRepository;
        this.historyStatusUserRepository = historyStatusUserRepository;
        this.administrativeUserRepository = administrativeUserRepository;
        this.tokenRepository = tokenRepository;
    }

    @Transactional
    public Long createUserAdmin(final String rif, final String password){

        try {

            ConfigurationEntity configurationEntity = configurationRepository.
                    findConfigurationEntityByIdConfiguration(1L);

            configurationEntity.setValue(configurationEntity.getValue());
            UserEntity userEntity= new UserEntity();
            userEntity.setUserName(rif);
            userEntity.setFirstLogin(false);
            userEntity.setPassword(password);
            userEntity.setProfileEntity(new ProfileEntity(1L));
            StatusUserEntity statusUserEntity = new StatusUserEntity();
            statusUserEntity.setIdStatusUser(1L);
            userEntity.setStatusUserEntity(statusUserEntity);

            userRepository.save(userEntity);

            List<TermsAndConditionsEntity> termsAndConditionsEntityList = termsAndConditionsRepository.
                    getLastTermsAndConditions();

            List<ApprovalUserEntity> approvalUserEntities = new ArrayList<>();

            OffsetDateTime registerDate = OffsetDateTime.now(ZoneId.of("America/Caracas"));

            for (TermsAndConditionsEntity terms : termsAndConditionsEntityList) {
                ApprovalUserEntity approvalUserEntity = new ApprovalUserEntity();
                approvalUserEntity.setTermsAndConditionsEntity(terms);
                approvalUserEntity.setUserEntity(userEntity);
                approvalUserEntity.setApprovalDate(null);
                approvalUserEntity.setRegisterDate(registerDate);
                approvalUserEntity.setApprovalStatus(false);
                approvalUserEntities.add(approvalUserEntity);
            }

            approvalUserRepository.saveAll(approvalUserEntities);

            return userEntity.getIdUser();

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }

    @Transactional
    public Long createUserInt(final String rif, final String password,final Long idProfile){

        try {

            UserEntity existingUser = userRepository.findByUserName(rif);
            if (existingUser != null) {
                return null;
            }
            ConfigurationEntity configurationEntity = configurationRepository.
                    findConfigurationEntityByIdConfiguration(1L);

            configurationEntity.setValue(configurationEntity.getValue());
            UserEntity userEntity= new UserEntity();
            userEntity.setUserName(rif);
            userEntity.setFirstLogin(true);
            userEntity.setPassword(password);
            userEntity.setProfileEntity(new ProfileEntity(idProfile));
            StatusUserEntity statusUserEntity = new StatusUserEntity();
            statusUserEntity.setIdStatusUser(1L);
            userEntity.setStatusUserEntity(statusUserEntity);

            userRepository.save(userEntity);
            return userEntity.getIdUser();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }

    public Response editProfile(final Long idUser, final String fullName, final String phoneNumber, final String email){

        try {

            ClientEntity clientEntity = clientRepository.findByUserEntity_IdUser(idUser);

            if(!fullName.equals("null")){
                clientEntity.setClientName(fullName);
            }

            if (!phoneNumber.equals("null")){
                clientEntity.setPhoneNumber(phoneNumber);
            }

            if (!email.equals("null")){
                clientEntity.setEmail(email);
            }
            clientRepository.save(clientEntity);
            return new Response("SUCCESS","Se actualizo la información correctamente");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new Response("ERROR","Error actualizando la información");
        }
    }

    public UserEntity findByIdUser(final Long idUser) {
        return userRepository.findByIdUser(idUser);
    }

    public Page<UsersByCommerceTo> getAllUsersByCommerce(final Long idCommerce, Pageable pageable){

        try {
            return clientRepository.findUsersByCommerce(idCommerce,pageable);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }

    public CommerceEntity getCommerceEntityByDocument(final String commerceDocument) {
        return commerceRepository.findByCommerceDocument(commerceDocument);
    }

    public Page<UsersByCommerceTo> getFilterForUser(final int idTypeService, final String filterField,final String endDate,final String rif, Pageable pageable) {
        Page<UsersByCommerceTo> sendFilter = null;
        try {
            CommerceEntity commerceEntity = commerceRepository.findByCommerceDocument(rif);
            switch (idTypeService) {
                case 1:
                    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    LocalDate localDate = LocalDate.parse(filterField, dateFormatter);
                    DateTimeFormatter endDateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    LocalDate localEndDate = LocalDate.parse(endDate, endDateFormatter);
                    sendFilter = clientRepository.findByRegisterDateAndCommerce(localDate,localEndDate,commerceEntity.getIdCommerce(), pageable);
                    break;
                case 2:
                    sendFilter = clientRepository.findByFullNameAndCommerce(filterField, pageable,commerceEntity.getIdCommerce());
                    break;
                case 3:
                    sendFilter = clientRepository.findByIdentificationDocumentAndCommerce(filterField, commerceEntity.getIdCommerce(),pageable);
                    break;
                case 4:
                    sendFilter = clientRepository.findByUsernameAndCommerce(filterField, pageable,commerceEntity.getIdCommerce());
                    break;
                case 5:
                    sendFilter = clientRepository.findByProfileAndCommerce(filterField, pageable,commerceEntity.getIdCommerce());
                    break;
                case 6:
                    sendFilter = clientRepository.findByRifAndCommerce(filterField,commerceEntity.getIdCommerce(), pageable);
                    break;
                case 7:
                    sendFilter = clientRepository.findByCommerceNameAndCommerce(filterField,commerceEntity.getIdCommerce(),pageable);
                    break;
                case 8:
                    sendFilter = clientRepository.findByStatusUserAndCommerce(filterField,commerceEntity.getIdCommerce(),pageable);
                    break;

            }
            return sendFilter;

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }

    @Transactional
    public void updateUserStatusByRif(final CommerceEntity commerceEntity, final Long idStatus,
                                      final String reasonStatus, final HttpServletRequest httpServletRequest) {

        userRepository.updateStatusByCommerceRif(commerceEntity.getCommerceDocument(), idStatus, reasonStatus,
                OffsetDateTime.now(ZoneId.of("America/Caracas")));

        List<String> usernameList = userRepository.
                getALlUsernamesByIdCommerce(commerceEntity.getIdCommerce());

        /**
         * TODO: Quitar el cambio de estatus en el usuario cuando se cambie en la interna
         */
        if (idStatus == 3) {

            userRepository.updateAllUserStatusByUsername(usernameList, idStatus, 1L);

            List<Long> idUserList = userRepository.
                    getAllIdUserByIdCommerce(commerceEntity.getIdCommerce());

            tokenRepository.updateAllTokensByIdUser(idUserList);

        } else if (idStatus == 1) {
            userRepository.updateAllUserStatusByUsername(usernameList, idStatus, 3L);
        }

        HttpHeaders headers = new HttpHeaders();
        RestTemplate restTemplate = new RestTemplate();

        headers.add("API_KEY", httpServletRequest.getHeader("API_KEY"));

        String uri = "http://localhost:8091/horizonte";

        LinkedHashMap<String, Object> requestBody = new LinkedHashMap<>();

        requestBody.put("idStatus", idStatus);
        requestBody.put("usernameList", usernameList);

        HttpEntity<LinkedHashMap<String, Object>> request = new HttpEntity<>(requestBody, headers);

        restTemplate.exchange(uri.concat("/updateAllUserStatusByUsername"),
                HttpMethod.POST, request, String.class);

    }


    public List<Object> getAllUsersByAdmin(final Pageable pageable){

        try {

            List<UsersByCommerceTo> clientUser = userRepository.findUsersByAdmin();
            List<AdministrativesUserTo> adminUser = administrativeUserRepository.findAllAdministrativeUsers();

            List<Object> combinedList = new ArrayList<>();
            combinedList.addAll(clientUser);
            combinedList.addAll(adminUser);

            combinedList.sort((o1, o2) -> {
                OffsetDateTime date1, date2;

                if (o1 instanceof UsersByCommerceTo) {
                    date1 = ((UsersByCommerceTo) o1).getRegisterDate();
                } else {
                    date1 = ((AdministrativesUserTo) o1).getRegisterDate();
                }

                if (o2 instanceof UsersByCommerceTo) {
                    date2 = ((UsersByCommerceTo) o2).getRegisterDate();
                } else {
                    date2 = ((AdministrativesUserTo) o2).getRegisterDate();
                }

                return date2.compareTo(date1);
            });

            return combinedList;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }

    public Response blockUser(final Long idUser,final String reasonStatus){
        UserEntity userEntity = userRepository.findByIdUser(idUser);
        userEntity.setStatusUserEntity(new StatusUserEntity(4L));
        userRepository.save(userEntity);
        HistoryStatusUserEntity historyStatusUserEntity = new HistoryStatusUserEntity();
        historyStatusUserEntity.setReasonStatus(reasonStatus);
        historyStatusUserEntity.setUserEntity(userEntity);
        historyStatusUserEntity.setRegisterDate(OffsetDateTime.now(ZoneId.of("America/Caracas")));
        historyStatusUserEntity.setStatusUserEntity(new StatusUserEntity(4L));
        historyStatusUserRepository.save(historyStatusUserEntity);
        return new Response("SUCCESS",userEntity.getUserName());

    }

    public Response unlockUserWithReason(final Long idUser,final String reasonStatus){
        UserEntity userEntity = userRepository.findByIdUser(idUser);
        userEntity.setStatusUserEntity(new StatusUserEntity(1L));
        userRepository.save(userEntity);
        HistoryStatusUserEntity historyStatusUserEntity = new HistoryStatusUserEntity();
        historyStatusUserEntity.setReasonStatus(reasonStatus);
        historyStatusUserEntity.setUserEntity(userEntity);
        historyStatusUserEntity.setRegisterDate(OffsetDateTime.now(ZoneId.of("America/Caracas")));
        historyStatusUserEntity.setStatusUserEntity(new StatusUserEntity(1L));
        historyStatusUserRepository.save(historyStatusUserEntity);
        return new Response("SUCCESS","Usuario Desbloqueado");

    }


    @Transactional
    public Long createUserAdminForUser(final String documentNumber,final String phoneNUmber
            ,final Long idUser,final String email,final String fullName){

        try {

            AdministrativeUserEntity administrativeUserEntity = new AdministrativeUserEntity();
            administrativeUserEntity.setActive(true);
            administrativeUserEntity.setUserEntity(new UserEntity(idUser));
            administrativeUserEntity.setDocument(documentNumber);
            administrativeUserEntity.setEmail(email);
            administrativeUserEntity.setPhoneNumber(phoneNUmber);
            administrativeUserEntity.setRegisterDate(OffsetDateTime.now(ZoneId.of("America/Caracas")));
            administrativeUserEntity.setName(fullName);
            administrativeUserRepository.save(administrativeUserEntity);
            HistoryStatusUserEntity historyStatusUserEntity = new HistoryStatusUserEntity();
            historyStatusUserEntity.setReasonStatus(null);
            historyStatusUserEntity.setUserEntity(new UserEntity(idUser));
            historyStatusUserEntity.setRegisterDate(OffsetDateTime.now(ZoneId.of("America/Caracas")));
            historyStatusUserEntity.setStatusUserEntity(new StatusUserEntity(1L));
            historyStatusUserRepository.save(historyStatusUserEntity);
            return administrativeUserEntity.getIdAdministrativeUser();

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }


    public Page<Object> getFilterForUsersAdmin(final int idTypeService, final String filterField,final String endDate, Pageable pageable) {
        Page<UsersByCommerceTo> sendFilter ;
        Page<AdministrativesUserTo> administrativesUserTos ;
        List<Object> data = new ArrayList<>();



        try {
            switch (idTypeService) {
                case 1:
                    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    LocalDate localDate = LocalDate.parse(filterField, dateFormatter);
                    DateTimeFormatter endDateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    LocalDate localEndDate = LocalDate.parse(endDate, endDateFormatter);
                    sendFilter = clientRepository.findByRegisterDate(localDate,localEndDate, pageable);
                    administrativesUserTos = administrativeUserRepository.findRegisterDateByAdmin(localDate,localEndDate,pageable);
                    data.addAll(sendFilter.getContent());
                    data.addAll(administrativesUserTos.getContent());
                    break;
                case 2:
                    sendFilter = clientRepository.findByFullName(filterField, pageable);
                    administrativesUserTos = administrativeUserRepository.findFullNameByAdmin(filterField,pageable);
                    data.addAll(sendFilter.getContent());
                    data.addAll(administrativesUserTos.getContent());
                    break;
                case 3:
                    sendFilter = clientRepository.findByIdentificationDocument(filterField, pageable);
                    administrativesUserTos = administrativeUserRepository.findIdentificationDocumentByAdmin(filterField,pageable);
                    data.addAll(sendFilter.getContent());
                    data.addAll(administrativesUserTos.getContent());
                    break;
                case 4:
                    sendFilter = clientRepository.findByUsername(filterField, pageable);
                    administrativesUserTos = administrativeUserRepository.findUsernameByAdmin(filterField,pageable);
                    data.addAll(sendFilter.getContent());
                    data.addAll(administrativesUserTos.getContent());
                    break;
                case 5:
                    sendFilter = clientRepository.findByProfile(filterField, pageable);
                    administrativesUserTos = administrativeUserRepository.findProfileByAdmin(filterField,pageable);
                    data.addAll(sendFilter.getContent());
                    data.addAll(administrativesUserTos.getContent());
                    break;
                case 6:
                    sendFilter = clientRepository.findByRif(filterField, pageable);
                    data.addAll(sendFilter.getContent());
                    break;
                case 7:
                    sendFilter = clientRepository.findByCommerceName(filterField, pageable);
                    if (filterField.trim().equalsIgnoreCase("Intelipay")){
                        administrativesUserTos = administrativeUserRepository.findCommerceNameByAdmin(filterField,pageable);
                        data.addAll(administrativesUserTos.getContent());
                    }

                    data.addAll(sendFilter.getContent());

                    break;
                case 8:
                    sendFilter = clientRepository.findByStatusUser(filterField,pageable);
                    administrativesUserTos = administrativeUserRepository.findStatusUserByAdmin(filterField,pageable);
                    data.addAll(sendFilter.getContent());
                    data.addAll(administrativesUserTos.getContent());
                    break;

            }

            int start = (int) pageable.getOffset();
            int end = Math.min((start + pageable.getPageSize()), data.size());
            return new PageImpl<>(data.subList(start, end), pageable, data.size());

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }


    public Response editProfileAdmin(final Long idUser, final String fullName, final String phoneNumber, final String email){

        try {

            AdministrativeUserEntity administrativeUserEntity = administrativeUserRepository.findByUserEntity_IdUser(idUser);

            if(!fullName.equals("null")){
                administrativeUserEntity.setName(fullName);
            }

            if (!phoneNumber.equals("null")){
                administrativeUserEntity.setPhoneNumber(phoneNumber);
            }

            if (!email.equals("null")){
                administrativeUserEntity.setEmail(email);
            }
            administrativeUserRepository.save(administrativeUserEntity);
            return new Response("SUCCESS","Se actualizo la información correctamente");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new Response("ERROR","Error actualizando la información");
        }
    }

}
