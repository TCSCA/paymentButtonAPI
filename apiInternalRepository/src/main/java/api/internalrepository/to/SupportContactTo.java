package api.internalrepository.to;

import api.internalrepository.entity.ClientEntity;
import api.internalrepository.entity.CommerceEntity;
import api.internalrepository.entity.SupportEntity;

public class SupportContactTo {

    private ClientEntity clientEntity;

    private CommerceEntity commerceEntity;

    private SupportEntity supportEntity;

    public SupportContactTo() {
    }

    public SupportContactTo(ClientEntity clientEntity, CommerceEntity commerceEntity, SupportEntity supportEntity) {
        this.clientEntity = clientEntity;
        this.commerceEntity = commerceEntity;
        this.supportEntity = supportEntity;
    }

    public ClientEntity getClientEntity() {
        return clientEntity;
    }

    public void setClientEntity(ClientEntity clientEntity) {
        this.clientEntity = clientEntity;
    }

    public CommerceEntity getCommerceEntity() {
        return commerceEntity;
    }

    public void setCommerceEntity(CommerceEntity commerceEntity) {
        this.commerceEntity = commerceEntity;
    }

    public SupportEntity getSupportEntity() {
        return supportEntity;
    }

    public void setSupportEntity(SupportEntity supportEntity) {
        this.supportEntity = supportEntity;
    }
}
