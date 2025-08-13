package api.loginext.controller;

import api.loginext.util.HibernateProxyTypeAdapter;
import api.loginext.util.Response;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping(value = "/util")
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST})
public class ApiVersionController {

    private final Environment environment;

    public ApiVersionController(Environment environment) {
        this.environment = environment;
    }

    @GetMapping("/api-version")
    public String apiVersion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse){
        GsonBuilder b = new GsonBuilder();
        b.registerTypeAdapterFactory(HibernateProxyTypeAdapter.FACTORY);
        Gson gson = b.setPrettyPrinting().create();
        String date = new Date().toString();
        String apiVersion = environment.getProperty("app.name").concat(" ").concat(environment.getProperty("app.platform"))
                .concat(environment.getProperty("api.version"));
        Response response = new Response("SUCCESS", "Version ".concat(apiVersion).concat(" ").concat(date));
        return gson.toJson(response);
    }
}
