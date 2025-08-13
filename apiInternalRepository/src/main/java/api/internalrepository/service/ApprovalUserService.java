package api.internalrepository.service;

import api.internalrepository.entity.ApprovalUserEntity;
import api.internalrepository.entity.ProfileEntity;
import api.internalrepository.entity.TermsAndConditionsEntity;
import api.internalrepository.entity.UserEntity;
import api.internalrepository.repository.ApprovalUserRepository;
import api.internalrepository.repository.ProfileRepository;
import api.internalrepository.repository.UserRepository;
import api.internalrepository.to.ApprovalUserTo;
import api.logsClass.LogsClass;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

@Service
public class ApprovalUserService {

    private static final Logger logger = LoggerFactory.getLogger(ApprovalUserService.class);

    private final ProfileRepository profileRepository;

    private final ApprovalUserRepository approvalUserRepository;

    private final UserRepository userRepository;

    public ApprovalUserService(ProfileRepository profileRepository, ApprovalUserRepository approvalUserRepository, UserRepository userRepository) {
        this.profileRepository = profileRepository;
        this.approvalUserRepository = approvalUserRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public void createNewApprovalUserByTermsAndConditions(
            final TermsAndConditionsEntity termsAndConditionsEntity,
            final HttpServletRequest httpServletRequest) {

        LogsClass logTo = new LogsClass(httpServletRequest, new Object() {}.
                getClass().getEnclosingMethod().getName());

        LocalDateTime localDateTime = LocalDateTime.now(ZoneId.of("America/Caracas"));

        List<UserEntity> userEntityList = userRepository.findAll();
        List<ApprovalUserEntity> approvalUserEntities = new ArrayList<>();

        OffsetDateTime registerDate = OffsetDateTime.now(ZoneId.of("America/Caracas"));

        userEntityList.forEach(userEntity -> {
            ApprovalUserEntity approvalUserEntity = new ApprovalUserEntity();
            approvalUserEntity.setTermsAndConditionsEntity(termsAndConditionsEntity);
            approvalUserEntity.setUserEntity(userEntity);
            approvalUserEntity.setApprovalDate(null);
            approvalUserEntity.setRegisterDate(registerDate);
            approvalUserEntity.setApprovalStatus(false);
            approvalUserEntities.add(approvalUserEntity);
        });

        approvalUserRepository.saveAll(approvalUserEntities);

        logger.info("All approvalUser changed successfully {}", logTo.getTimeInSeconds(localDateTime));

    }

    @Transactional(readOnly = true)
    public List<ApprovalUserTo> getApprovalUserByIdUser(final Long idUser) {
        List<String> termsAndPolicies = new ArrayList<>();
        termsAndPolicies.add("Terminos");
        termsAndPolicies.add("Politicas");

        ProfileEntity profileEntity = profileRepository.getProfileEntityByIdUser(idUser);

        if (profileEntity.getIdProfile() == 2){
            List<ApprovalUserTo> approvalUserToList = new ArrayList<>();
            approvalUserToList.add(new ApprovalUserTo(true, null, "Terminos"));
            approvalUserToList.add(new ApprovalUserTo(true, null, "Politicas"));
            return approvalUserToList;
        } else {
            return approvalUserRepository.getApprovalUserEntityByIdUser(idUser, termsAndPolicies);
        }

    }

    public void changeStatusApprovalUser(final Long idUser, final String termsOrPolicies) {

        OffsetDateTime dateTimeInCaracas = OffsetDateTime.now(ZoneId.of("America/Caracas"));

        OffsetDateTime dateTimeInUTC = dateTimeInCaracas.withOffsetSameInstant(ZoneOffset.UTC);

        approvalUserRepository.updateStatusApprovalUser(idUser, termsOrPolicies, dateTimeInUTC);
    }

}
