package api.apiP2c.entity;

import com.google.gson.annotations.Expose;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@Entity
@Table(name = "t_servicio_bancario")
@NoArgsConstructor
public class BankServiceEntity {

    @Id
    @Column(name = "id_servicio_bancario")
    @Expose
    private Long idBankService;

    @Column(name = "servicio_bancario")
    private String bankService;

    @ManyToOne()
    @Expose
    @JoinColumn(name = "id_banco")
    private BankEntity bankEntity;

    @Column(name = "url")
    private String url;

    @Column(name = "fecha_registro")
    private OffsetDateTime registerDate;

    @Column(name = "fecha_actualizacion")
    private OffsetDateTime updateDate;

    @Column(name = "status")
    private Boolean status;


}
