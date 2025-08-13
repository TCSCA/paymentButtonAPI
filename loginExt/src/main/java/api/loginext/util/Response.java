package api.loginext.util;

import java.io.Serializable;
import java.util.LinkedHashMap;

public class Response implements Serializable {

    private String status;

    private Object data;

    private Object profile;

    private Object menu;

    private Object firstLogin;

    private Object transaction;

    private Object user;

    private Object tokenBiscomm;

    private Object username;

    private LinkedHashMap properties;

    private Object temporalPassword;

    private Boolean approvalTermsAndConditionsStatus;

    private Boolean approvalPoliciesStatus;

    public Response(String status, Object data) {
        this.status = status;
        this.data = data;
    }

    public Response(String status, Object data, LinkedHashMap properties) {
        this.status = status;
        this.data = data;
        this.properties = properties;
    }

    public Response(String status, Object profile, Object menu) {
        this.status = status;
        this.profile = profile;
        this.menu = menu;
    }

    public Response(String status, Object data, Object profile, Object menu) {
        this.status = status;
        this.data = data;
        this.profile = profile;
        this.menu = menu;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public Object getProfile() {
        return profile;
    }

    public void setProfile(Object profile) {
        this.profile = profile;
    }

    public Object getMenu() {
        return menu;
    }

    public void setMenu(Object menu) {
        this.menu = menu;
    }

    public Object getUser() {
        return user;
    }

    public void setUser(Object user) {
        this.user = user;
    }

    public void setProperties(LinkedHashMap properties) {
        this.properties = properties;
    }

    public Object getUsername() {
        return username;
    }

    public void setUsername(Object username) {
        this.username = username;
    }

    public Object getTransaction() {
        return transaction;
    }

    public void setTransaction(Object transaction) {
        this.transaction = transaction;
    }

    public LinkedHashMap getProperties() {
        return properties;
    }

    public Object getTokenBiscomm() {
        return tokenBiscomm;
    }

    public void setTokenBiscomm(Object tokenBiscomm) {
        this.tokenBiscomm = tokenBiscomm;
    }

    public Object getFirstLogin() {
        return firstLogin;
    }

    public Object getTemporalPassword() {
        return temporalPassword;
    }

    public void setFirstLogin(Object firstLogin) {
        this.firstLogin = firstLogin;
    }

    public void setTemporalPassword(Object temporalPassword) {
        this.temporalPassword = temporalPassword;
    }

    public Boolean getApprovalTermsAndConditionsStatus() {
        return approvalTermsAndConditionsStatus;
    }

    public void setApprovalTermsAndConditionsStatus(Boolean approvalTermsAndConditionsStatus) {
        this.approvalTermsAndConditionsStatus = approvalTermsAndConditionsStatus;
    }

    public Boolean getApprovalPoliciesStatus() {
        return approvalPoliciesStatus;
    }

    public void setApprovalPoliciesStatus(Boolean approvalPoliciesStatus) {
        this.approvalPoliciesStatus = approvalPoliciesStatus;
    }

    @Override
    public String toString() {
        return "Response{" +
                "status='" + status + '\'' +
                ", data=" + data +
                ", profile=" + profile +
                ", menu=" + menu +
                ", user=" + user +
                '}';
    }
}

