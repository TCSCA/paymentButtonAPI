package api.internalrepository.service;

import api.internalrepository.entity.TermsAndConditionsEntity;
import api.internalrepository.repository.TermsAndConditionsRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.List;

@Service
public class TermsAndConditionsService {

    private static final Logger logger = LoggerFactory.getLogger(TermsAndConditionsService.class);

    private final ApprovalUserService approvalUserService;

    private final TermsAndConditionsRepository termsAndConditionsRepository;

    private final UserService userService;

    public TermsAndConditionsService(ApprovalUserService approvalUserService, TermsAndConditionsRepository termsAndConditionsRepository, UserService userService) {
        this.approvalUserService = approvalUserService;
        this.termsAndConditionsRepository = termsAndConditionsRepository;
        this.userService = userService;
    }

    public void saveNewTermsAndConditions(final String urlFile, final String termsOrPolicies,
                                          final Long idUser, final HttpServletRequest httpServletRequest) {

        List<TermsAndConditionsEntity> termsList = termsAndConditionsRepository.
                findAllOrderByRegisterDateDesc(termsOrPolicies);

        if (!termsList.isEmpty()) {
            TermsAndConditionsEntity latestTerms = termsList.get(0);
            termsAndConditionsRepository.updateStatusToFalse(latestTerms.getIdTermsAndConditions());
        }

        TermsAndConditionsEntity termsAndConditionsEntity = new TermsAndConditionsEntity();
        termsAndConditionsEntity.setUrlFile(urlFile);
        termsAndConditionsEntity.setFileName(termsOrPolicies);
        termsAndConditionsEntity.setRegisterDate(OffsetDateTime.now(ZoneId.of("America/Caracas")));
        termsAndConditionsEntity.setRegisterBy(userService.findByIdUser(idUser).getUserName());
        termsAndConditionsEntity.setStatus(true);
        termsAndConditionsRepository.save(termsAndConditionsEntity);

        logger.info(termsOrPolicies + " saved successfully");

        approvalUserService.createNewApprovalUserByTermsAndConditions(termsAndConditionsEntity, httpServletRequest);
    }

    public List<TermsAndConditionsEntity> getLastTermsConditionsAndPolicies() {
        return termsAndConditionsRepository.getLastTermsAndConditions();
    }

}
