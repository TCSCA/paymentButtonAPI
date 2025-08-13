package api.preRegistro.controller;

import api.preRegistro.service.DirectionService;
import api.preRegistro.util.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;
import java.util.GregorianCalendar;

@RestController
@RequestMapping(value = "/preRegistro")
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST})
public class DirectionController {

    private final DirectionService directionService;

    public DirectionController(DirectionService directionService) {
        this.directionService = directionService;
    }


    @GetMapping("/getAllCountry")
    public String getAllCountry(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        GsonBuilder b = new GsonBuilder();
        b.registerTypeAdapterFactory(HibernateProxyTypeAdapter.FACTORY);
        b.registerTypeAdapter(GregorianCalendar.class, new CalendarSerializer());
        b.registerTypeAdapter(OffsetDateTime.class, new OffsetDateTimeSerializer());
        Gson gson = b.setPrettyPrinting().create();

        Response response;

        try {
            response = directionService.getAllCountry();
            httpServletResponse.setStatus(HttpServletResponse.SC_OK);
            return gson.toJson(response);
        } catch (Exception e) {
            httpServletResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return gson.toJson(new Response("Error", e));
        }
    }

    @GetMapping("/getAllState/{idCountry}")
    @Operation(summary = "Obtener todos los estados",
            description = "Obtiene una lista de estados por país",
            security = {@SecurityRequirement(name = "API_KEY")})
    public String getAllState(
            @Parameter(description = "ID del pais", required = true)
            @PathVariable Long idCountry,
            HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse) {

        GsonBuilder b = new GsonBuilder();
        b.registerTypeAdapterFactory(HibernateProxyTypeAdapter.FACTORY);
        b.registerTypeAdapter(GregorianCalendar.class, new CalendarSerializer());
        b.registerTypeAdapter(OffsetDateTime.class, new OffsetDateTimeSerializer());
        Gson gson = b.setPrettyPrinting().create();

        Response response;

        if (!ValidationUtil.validateApiKey(httpServletRequest)) {
            httpServletResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return gson.toJson(new Response("ERROR", "UNAUTHORIZED"));
        }

        try {
            response = directionService.getAllStateByIdCountry(idCountry);
            httpServletResponse.setStatus(HttpServletResponse.SC_OK);
            return gson.toJson(response);
        } catch (Exception e) {
            httpServletResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return gson.toJson(new Response("Error", e));
        }
    }

    @GetMapping("/getAllCity/{idMunicipality}")
    public String getAllCity(@PathVariable Long idMunicipality, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        GsonBuilder b = new GsonBuilder();
        b.registerTypeAdapterFactory(HibernateProxyTypeAdapter.FACTORY);
        b.registerTypeAdapter(GregorianCalendar.class, new CalendarSerializer());
        b.registerTypeAdapter(OffsetDateTime.class, new OffsetDateTimeSerializer());
        Gson gson = b.setPrettyPrinting().create();

        Response response;

        if(!ValidationUtil.validateApiKey(httpServletRequest)) {
            return gson.toJson(new Response("ERROR", "UNAUTHORIZED"));
        }

        try {
            response = directionService.getAllCountryByMunicipality(idMunicipality);
            httpServletResponse.setStatus(HttpServletResponse.SC_OK);
            return gson.toJson(response);
        } catch (Exception e) {
            httpServletResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return gson.toJson(new Response("Error", e));
        }
    }

    @GetMapping("/getAllMunicipality/{idState}")
    public String getAllMunicipality(@PathVariable Long idState, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        GsonBuilder b = new GsonBuilder();
        b.registerTypeAdapterFactory(HibernateProxyTypeAdapter.FACTORY);
        b.registerTypeAdapter(GregorianCalendar.class, new CalendarSerializer());
        b.registerTypeAdapter(OffsetDateTime.class, new OffsetDateTimeSerializer());
        Gson gson = b.setPrettyPrinting().create();

        Response response;

        if(!ValidationUtil.validateApiKey(httpServletRequest)) {
            httpServletResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return gson.toJson(new Response("ERROR", "UNAUTHORIZED"));
        }

        try {
            response = directionService.getAllMunicipalityByStateId(idState);
            httpServletResponse.setStatus(HttpServletResponse.SC_OK);
            return gson.toJson(response);
        } catch (Exception e) {
            httpServletResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return gson.toJson(new Response("Error", e));
        }
    }
}
