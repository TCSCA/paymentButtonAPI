package api.apiP2c.util;

public class GmailCredential {


    private String client_id;
    private String client_secret;
    private String refresh_token;
    private String grant_type;
    private String access_token;
    private String userEmail;

    public GmailCredential(String client_id, String client_secret, String refresh_token, String grant_type, String access_token, String userEmail) {
        if (client_id != null) {
            this.client_id = client_id.replaceAll("\\s+", "");
        }else{
            this.client_id = client_id;
        }

        if (client_secret != null) {
            this.client_secret = client_secret.replaceAll("\\s+", "");
        }else{
            this.client_secret = client_secret;
        }

        if (refresh_token != null) {
            this.refresh_token = refresh_token.replaceAll("\\s+", "");
        }else{
            this.refresh_token = refresh_token;
        }
        this.grant_type = grant_type;
        this.access_token = access_token;
        this.userEmail = userEmail;
    }

    public String getClient_id() {
        return client_id;
    }

    public void setClient_id(String client_id) {
        this.client_id = client_id;
    }

    public String getClient_secret() {
        return client_secret;
    }

    public void setClient_secret(String client_secret) {
        this.client_secret = client_secret;
    }

    public String getRefresh_token() {
        return refresh_token;
    }

    public void setRefresh_token(String refresh_token) {
        this.refresh_token = refresh_token;
    }

    public String getGrant_type() {
        return grant_type;
    }

    public void setGrant_type(String grant_type) {
        this.grant_type = grant_type;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    @Override
    public String toString() {
        return "GmailCredential{" +
                "client_id='" + client_id + '\'' +
                ", client_secret='" + client_secret + '\'' +
                ", refresh_token='" + refresh_token + '\'' +
                ", grant_type='" + grant_type + '\'' +
                ", access_token='" + access_token + '\'' +
                ", userEmail='" + userEmail + '\'' +
                '}';
    }
}
