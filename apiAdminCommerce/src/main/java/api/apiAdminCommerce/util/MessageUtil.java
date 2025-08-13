package api.apiAdminCommerce.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

@Configuration
@PropertySource(value = "classpath:message.properties")
public class MessageUtil {

    private final Environment env;

    @Autowired
    public MessageUtil(Environment env) {
        this.env = env;
    }

    public LinkedHashMap<String,String> getMessageByKey(String key){
        String value = env.getProperty(key);
        LinkedHashMap<String,String> linkedHashMap = new LinkedHashMap<>();
        linkedHashMap.put("code",key);
        linkedHashMap.put("message",value);
        return linkedHashMap;
    }

    public LinkedHashMap<String,Object> getTimeProcess(LocalTime initTime){
        LinkedHashMap<String,Object> linkedHashMap = new LinkedHashMap<>();
        LocalTime localTime = LocalTime.now(ZoneId.of("America/Caracas"));
        double timeSeconds = (Double.valueOf(String.valueOf(localTime.toNanoOfDay())) -
                Double.valueOf(String.valueOf(initTime.toNanoOfDay())))/1000000000;
        linkedHashMap.put("timeInSeconds",new BigDecimal(timeSeconds).setScale(4, RoundingMode.HALF_EVEN));
        return linkedHashMap;
    }

    public String encryptPassword(String password) {
        Instant date = Instant.now().truncatedTo(ChronoUnit.SECONDS);
        ZoneId caracasAmerica = ZoneId.of("America/Caracas");
        ZonedDateTime issuedAt = ZonedDateTime.ofInstant(date, caracasAmerica);
        ZonedDateTime expiryDate = issuedAt.plus(1, ChronoUnit.YEARS);

        String passwordJson = "password\":" + password;
        List<GrantedAuthority> grantedAuthorities = AuthorityUtils
                .commaSeparatedStringToAuthorityList("Admin");

        String encryptPassword = Jwts
                .builder()
                .claim("link",
                        passwordJson)
                .setAudience("Admin")
                .setIssuedAt(Date.from(issuedAt.toInstant()))
                .setExpiration(Date.from(expiryDate.toInstant()))
                .signWith(SignatureAlgorithm.HS512,
                        env.getProperty("key.secret.token.payment.link")).compact();


        return encryptPassword;
    }
}
