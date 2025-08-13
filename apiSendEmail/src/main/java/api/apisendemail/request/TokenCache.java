package api.apisendemail.request;

import java.time.Instant;

public class TokenCache {
    private String accessToken;
    private Instant expiryTime;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public Instant getExpiryTime() {
        return expiryTime;
    }

    public void setExpiryTime(Instant expiryTime) {
        this.expiryTime = expiryTime;
    }

    public boolean isTokenValid() {
        return accessToken != null && expiryTime != null && Instant.now().isBefore(expiryTime);
    }

}
