package api.apicommerce.request;

import api.apicommerce.entity.DirectionEntity;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

public class CommerceUpdateRequest {

    private String rif;

    private DirectionEntity directionEntity;

    public CommerceUpdateRequest() {
    }

    public CommerceUpdateRequest(String rif, DirectionEntity directionEntity) {
        this.rif = rif;
        this.directionEntity = directionEntity;
    }

    public String getRif() {
        return rif;
    }

    public void setRif(String rif) {
        this.rif = rif;
    }

    public DirectionEntity getDirectionEntity() {
        return directionEntity;
    }

    public void setDirectionEntity(DirectionEntity directionEntity) {
        this.directionEntity = directionEntity;
    }
}
