package api.externalrepository.service;


import api.externalrepository.entity.PreRegisterEntity;
import api.externalrepository.repository.PreRegisterRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

@Service
public class FilterService {

    private final PreRegisterRepository preRegisterRepository;

    public FilterService(PreRegisterRepository preRegisterRepository) {
        this.preRegisterRepository = preRegisterRepository;
    }

    public List<PreRegisterEntity> getAllPreregisterFilter(LinkedHashMap requestBody, ObjectMapper objectMapper) {

        Integer typeFilter = objectMapper.convertValue(requestBody.get("typeFilter"), Integer.class);
        String filter = objectMapper.convertValue(requestBody.get("filter"), String.class);
        String startDateStr = objectMapper.convertValue(requestBody.get("startDate"), String.class);
        String endDateStr = objectMapper.convertValue(requestBody.get("endDate"), String.class);
        Long idStatusPreregister = objectMapper.convertValue(requestBody.get("idStatusPreregister"), Long.class);

        LocalDate startDateLocal;
        LocalDate endDateLocal;

        if(startDateStr != null && endDateStr!= null) {

             startDateLocal = LocalDate.parse(startDateStr);
             endDateLocal = LocalDate.parse(endDateStr);

        } else {
            startDateLocal = LocalDate.parse("1900-01-01");
            endDateLocal = LocalDate.parse("9999-12-31");
        }

        List<PreRegisterEntity> preRegisterEntities = new ArrayList<>();

        switch (typeFilter) {
            case 0:
                preRegisterEntities = preRegisterRepository.getPreregisterByStatusFilter(idStatusPreregister, startDateLocal, endDateLocal);
                break;
            case 1:
                preRegisterEntities = preRegisterRepository.getPreregisterByRifFilter(idStatusPreregister, filter,startDateLocal, endDateLocal);
                break;
            case 2:
                preRegisterEntities = preRegisterRepository.getPreregisterByCommerceNameFilter(idStatusPreregister, filter.toLowerCase(), startDateLocal, endDateLocal);
                break;
            case 3:
                preRegisterEntities = preRegisterRepository.getPreregisterByContactPersonFilter(idStatusPreregister, filter.toLowerCase(), startDateLocal, endDateLocal);
                break;
            case 4:
                preRegisterEntities = preRegisterRepository.getPreregisterByTypeCommerceFilter(idStatusPreregister, Long.valueOf(filter), startDateLocal, endDateLocal);
                break;
            case 5:
                preRegisterEntities = preRegisterRepository.getPreregisterByPlanFilter(idStatusPreregister, Long.valueOf(filter), startDateLocal, endDateLocal);
                break;
            case 6:

                break;
        }

        return preRegisterEntities;

    }


}
