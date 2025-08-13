package api.externalrepository.service;

import api.externalrepository.entity.RequirementEntity;
import api.externalrepository.entity.ResultFileEntity;
import api.externalrepository.repository.PreRegisterRepository;
import api.externalrepository.repository.ResultFileRepository;
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

    private final PreRegisterRepository preRegisterRepository;

    private final ResultFileRepository resultFileRepository;

    public ResultFileService(ManageLogs manageLogs, PreRegisterRepository preRegisterRepository, ResultFileRepository resultFileRepository) {
        this.manageLogs = manageLogs;
        this.preRegisterRepository = preRegisterRepository;
        this.resultFileRepository = resultFileRepository;
    }

    @Transactional
    public Boolean saveResultFile(final Long size, final Long chargedBy, final Long idPreRegister,
                                  final Long idRequirement, final String fileName,
                                  final HttpServletRequest httpServletRequest) {

        LogsClass logTo = new LogsClass(httpServletRequest, new Object() {}.
                getClass().getEnclosingMethod().getName());

        try {

            ResultFileEntity exist = resultFileRepository.
                    getResultFileByIdPreRegisterAndIdRequirement(idPreRegister, idRequirement);

            if (exist != null){
                exist.setFileName(fileName);
                resultFileRepository.save(exist);
            } else {
                ResultFileEntity resultFileEntity = new ResultFileEntity();

                resultFileEntity.setSize(size);
                resultFileEntity.setChargedBy(chargedBy);
                resultFileEntity.setPreRegisterEntity(preRegisterRepository.findByIdPreRegistro(idPreRegister));
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

    public ResultFileEntity getResultFileByIdPreRegisterAndIdRequirement(final Long idPreRegister,
                                                                         final Long idRequirement) {
        return resultFileRepository.
                getResultFileByIdPreRegisterAndIdRequirement(idPreRegister, idRequirement);
    }

    public List<ResultFileEntity> getAllResultFileByIdPreRegister(final Long idPreRegister) {
        return resultFileRepository.getAllResultFileByIdPreRegister(idPreRegister);
    }

}
