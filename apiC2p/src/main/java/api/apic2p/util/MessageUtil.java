package api.apic2p.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.LinkedHashMap;

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
}
