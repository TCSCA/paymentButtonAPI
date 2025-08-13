package api.externalrepository.to;

import java.util.List;

public class UserStatusTo {

    private Long idStatus;

    private List<String> usernameList;

    public UserStatusTo() {
    }

    public UserStatusTo(Long idStatus, List<String> usernameList) {
        this.idStatus = idStatus;
        this.usernameList = usernameList;
    }

    public Long getIdStatus() {
        return idStatus;
    }

    public void setIdStatus(Long idStatus) {
        this.idStatus = idStatus;
    }

    public List<String> getUsernameList() {
        return usernameList;
    }

    public void setUsernameList(List<String> usernameList) {
        this.usernameList = usernameList;
    }
}
