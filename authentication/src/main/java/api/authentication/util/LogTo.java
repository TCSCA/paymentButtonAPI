package api.authentication.util;

import jakarta.servlet.http.HttpServletRequest;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;

public class LogTo {
    private Long idThread;
    private final String userIp;
    private final String serverIp;
    private final String methodName;
    private final LocalDateTime initialTime;

    public LogTo(HttpServletRequest httpServletRequest, String methodName) {
        this.serverIp = httpServletRequest.getLocalAddr();
        this.userIp = httpServletRequest.getRemoteAddr();
        this.initialTime = LocalDateTime.now(ZoneId.of("America/Caracas"));
        this.methodName = methodName;
    }

    public LogTo(Long idThread, HttpServletRequest httpServletRequest, String methodName, LocalDateTime initialTime) {
        this.idThread = idThread;
        if (httpServletRequest.getHeader("X-Forwarded-For")==null) {
            this.userIp = httpServletRequest.getRemoteAddr();
        } else {
            this.userIp = httpServletRequest.getHeader("X-Forwarded-For");
        }
        this.serverIp = httpServletRequest.getLocalAddr();
        this.methodName = methodName;
        this.initialTime = initialTime;
    }

    public Long getIdThread() {
        return idThread;
    }

    public void setIdThread(Long idThread) {
        this.idThread = idThread;
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
        double timeSeconds = (Double.parseDouble(String.valueOf(localTime.toNanoOfDay())) -
                Double.parseDouble(String.valueOf(initTime.toNanoOfDay())))/1000000000;
        return new BigDecimal(timeSeconds).setScale(5, RoundingMode.HALF_EVEN);
    }
}
