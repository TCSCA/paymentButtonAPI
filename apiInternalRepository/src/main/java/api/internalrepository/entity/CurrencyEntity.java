package api.internalrepository.entity;

import com.google.gson.annotations.Expose;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "t_moneda")
public class CurrencyEntity {

    @Id
    @Column(name = "id_moneda")
    @Expose
    private Long idCurrency;

    @Column(name = "moneda")
    private String currencyDescription;

    @Column(name = "status")
    private Boolean status;

    public CurrencyEntity() {
    }

    public CurrencyEntity(Long idCurrency) {
        this.idCurrency = idCurrency;
    }

    public CurrencyEntity(Long idCurrency, String currencyDescription, Boolean status) {
        this.idCurrency = idCurrency;
        this.currencyDescription = currencyDescription;
        this.status = status;
    }

    public Long getIdCurrency() {
        return idCurrency;
    }

    public void setIdCurrency(Long idCurrency) {
        this.idCurrency = idCurrency;
    }

    public String getCurrencyDescription() {
        return currencyDescription;
    }

    public void setCurrencyDescription(String currencyDescription) {
        this.currencyDescription = currencyDescription;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }
}
