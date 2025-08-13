package api.apicreditcard.entity;

import com.google.gson.annotations.Expose;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "t_moneda")
@NoArgsConstructor
public class CurrencyEntity {

    @Id
    @Column(name = "id_moneda")
    @Expose
    private Long idCurrency;

    @Column(name = "moneda")
    private String currencyDescription;

    @Column(name = "status")
    private Boolean status;

    public CurrencyEntity(Long idCurrency) {
        this.idCurrency = idCurrency;
    }
}
