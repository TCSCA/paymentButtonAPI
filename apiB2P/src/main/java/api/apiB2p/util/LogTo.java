package api.apiB2p.util;

import jakarta.servlet.http.HttpServletRequest;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;

public class LogTo {

    private String userIp;
    private String serverIp;
    private String methodName;
    private LocalDateTime initialTime;
    private BigDecimal timeInSeconds;

    public LogTo(HttpServletRequest httpServletRequest, String methodName) {
        this.serverIp = httpServletRequest.getLocalAddr();
        this.userIp = httpServletRequest.getRemoteAddr();
        this.initialTime = LocalDateTime.now(ZoneId.of("America/Caracas"));
        this.methodName = methodName;
    }

    String getUserIp() {
        return userIp;
    }

    String getServerIp() {
        return serverIp;
    }

    String getMethodName() {
        return methodName;
    }

    LocalDateTime getInitialTime() {
        return initialTime;
    }

    BigDecimal getTimeInSeconds(LocalDateTime localDateTime) {
        LocalTime localTime = localDateTime.toLocalTime();
        LocalTime initTime = this.initialTime.toLocalTime();
        double timeSeconds = (Double.valueOf(String.valueOf(localTime.toNanoOfDay())) -
                Double.valueOf(String.valueOf(initTime.toNanoOfDay())))/1000000000;
        this.timeInSeconds = new BigDecimal(timeSeconds).setScale(5, RoundingMode.HALF_EVEN);
        return timeInSeconds;
    }
}
