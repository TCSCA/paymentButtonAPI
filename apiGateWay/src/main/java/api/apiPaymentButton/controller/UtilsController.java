package api.apiPaymentButton.controller;

import api.apiPaymentButton.util.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;
import java.util.Date;
import java.util.GregorianCalendar;

@RestController
@RequestMapping(value = "/util")
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST})
public class UtilsController {

    private final Environment environment;

    public UtilsController(Environment environment) {
        this.environment = environment;
    }

    @PostMapping("/encryptRoutesFile")
    public void encryptRoutesFile(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {

        LogTo logTo = new LogTo(httpServletRequest,"encryptRoutesFile");
        GsonBuilder b = new GsonBuilder();
        b.registerTypeAdapterFactory(HibernateProxyTypeAdapter.FACTORY);
        b.registerTypeAdapter(GregorianCalendar.class, new CalendarSerializer());
        b.registerTypeAdapter(OffsetDateTime.class, new OffsetDateTimeSerializer());
        Gson gson = b.setPrettyPrinting().create();

        Response response;

        try {
            CryptoUtils.encrypt();

        } catch (Exception e) {
            httpServletResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response = new Response("ERROR","Eror en el metodo");
        }
    }
}
