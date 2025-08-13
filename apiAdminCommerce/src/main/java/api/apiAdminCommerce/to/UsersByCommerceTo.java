package api.apiAdminCommerce.to;


import api.apiAdminCommerce.entity.ClientEntity;
import api.apiAdminCommerce.entity.CommerceEntity;

public class UsersByCommerceTo {

    private ClientEntity clientEntity;

    private CommerceEntity commerceEntity;

    public UsersByCommerceTo() {
    }

    public UsersByCommerceTo(ClientEntity clientEntity, CommerceEntity commerceEntity) {
        this.clientEntity = clientEntity;
        this.commerceEntity = commerceEntity;
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
}
