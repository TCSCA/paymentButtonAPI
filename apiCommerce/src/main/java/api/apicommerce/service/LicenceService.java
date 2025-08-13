package api.apicommerce.service;

import api.apicommerce.entity.CommerceEntity;
import api.apicommerce.entity.LicenseEntity;
import api.apicommerce.repository.CommerceRepository;
import api.apicommerce.repository.LicenceRepository;
import api.apicommerce.util.Response;
import api.logsClass.LogsClass;
import api.logsClass.ManageLogs;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class LicenceService {

    private final LicenceRepository licenceRepository;

    private final CommerceRepository commerceRepository;

    private final ManageLogs manageLogs;

    public LicenceService(LicenceRepository licenceRepository, CommerceRepository commerceRepository, ManageLogs manageLogs) {
        this.licenceRepository = licenceRepository;
        this.commerceRepository = commerceRepository;
        this.manageLogs = manageLogs;
    }

    public Response validateCommerceHasConfirmationCode(HttpServletRequest httpServletRequest, String rif){

        LogsClass logTo = new LogsClass(httpServletRequest, new Object() {}.
                getClass().getEnclosingMethod().getName());

        String confirmationKey = httpServletRequest.getHeader("CONFIRMATION_KEY");

        CommerceEntity commerceEntity = commerceRepository.findByCommerceDocument(rif);

        if (commerceEntity == null) {
            manageLogs.errorLogger(logTo, httpServletRequest, "Error commerce not found",
                    "user", false);
            return new Response("ERROR", "Rif no registrado");
        }
        LocalDate currentDate = LocalDate.now();
        if(confirmationKey !=null) {
           LicenseEntity licenseEntity = licenceRepository.getLicenceByIdCommerceBeforeExpireDateAndStatusValid(confirmationKey,
                   commerceEntity.getIdCommerce(), currentDate);

           if (licenseEntity != null) {
               return new Response("SUCCESS", "Licencia validada exitosamente");
           } else {
               manageLogs.errorLogger(logTo, httpServletRequest, "Error license not found",
                       "user", false);
               return new Response("ERROR", "Licencia no encontrada");
           }
        } else {
            manageLogs.errorLogger(logTo, httpServletRequest, "Error confirmationKey is null",
                    "user", false);
            return new Response("ERROR", "ConfirmationKey no existe");
        }
    }
}
