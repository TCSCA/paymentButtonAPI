package api.internalrepository.service;

import api.internalrepository.entity.RequirementEntity;
import api.internalrepository.entity.ResultFileEntity;
import api.internalrepository.entity.TermsAndConditionsEntity;
import api.internalrepository.repository.CommerceRepository;
import api.internalrepository.repository.ResultFileRepository;
import api.internalrepository.repository.TermsAndConditionsRepository;
import api.logsClass.LogsClass;
import api.logsClass.ManageLogs;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.List;

@Service
public class ResultFileService {

    private static final Logger logger = LoggerFactory.getLogger(ResultFileService.class);

    private final ManageLogs manageLogs;

    private final ResultFileRepository resultFileRepository;

    private final CommerceRepository commerceRepository;

    private final TermsAndConditionsRepository termsAndConditionsRepository;

    public ResultFileService(ManageLogs manageLogs, ResultFileRepository resultFileRepository, CommerceRepository commerceRepository, TermsAndConditionsRepository termsAndConditionsRepository) {
        this.manageLogs = manageLogs;
        this.resultFileRepository = resultFileRepository;
        this.commerceRepository = commerceRepository;
        this.termsAndConditionsRepository = termsAndConditionsRepository;
    }

    @Transactional
    public Boolean saveResultFile(final Long size, final Long chargedBy, final Long idCommerce,
                                  final Long idRequirement, final String fileName,
                                  final HttpServletRequest httpServletRequest) {

        LogsClass logTo = new LogsClass(httpServletRequest, new Object() {}.
                getClass().getEnclosingMethod().getName());

        try {

            ResultFileEntity exist = resultFileRepository.
                    getResultFileByIdCommerceAndIdRequirement(idCommerce, idRequirement);

            if (exist != null){
                exist.setFileName(fileName);
                resultFileRepository.save(exist);
            } else {
                ResultFileEntity resultFileEntity = new ResultFileEntity();

                resultFileEntity.setSize(size);
                resultFileEntity.setChargedBy(chargedBy);
                resultFileEntity.setCommerceEntity(commerceRepository.findByIdCommerce(idCommerce));
                resultFileEntity.setChargedDate(OffsetDateTime.now(ZoneId.of("America/Caracas")));
                resultFileEntity.setRequirementEntity(new RequirementEntity(idRequirement));
                resultFileEntity.setFileName(fileName);

                resultFileRepository.save(resultFileEntity);
            }

            manageLogs.infoLogger(logTo,httpServletRequest,
                    "Successfully: " + logTo.getMethodName(),
                    "user",false);

            return true;

        } catch (Exception e){
            logger.error(e.getMessage(), e);

            manageLogs.severeErrorLogger(logTo,httpServletRequest,"Error: "+ logTo.
                    getMethodName() + ": "+ e, e,"user",false);

            return false;
        }

    }

    public void saveAll(List<ResultFileEntity> resultFileEntities) {
        resultFileRepository.saveAll(resultFileEntities);
    }

    public ResultFileEntity getResultFileByIdCommerceAndIdRequirement(final Long idPreRegister,
                                                                         final Long idRequirement) {
        return resultFileRepository.
                getResultFileByIdCommerceAndIdRequirement(idPreRegister, idRequirement);
    }

    public TermsAndConditionsEntity getLastTermsOrPolicies(final String termsOrPolicies) {
        return termsAndConditionsRepository.findAllOrderByRegisterDateDesc(termsOrPolicies).get(0);
    }

}
