package api.apiP2c.entity;

import com.google.gson.annotations.Expose;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

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
