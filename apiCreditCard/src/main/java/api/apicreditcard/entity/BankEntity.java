package api.apicreditcard.entity;

import com.google.gson.annotations.Expose;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "t_banco")
@NoArgsConstructor
public class BankEntity {

    @Id
    @Column(name = "id_banco")
    @Expose
    @NotNull
    private Long idBank;

    @Column(name = "banco")
    private String bankName;

    @Column(name = "codigo")
    private String bankCode;

    @Column(name = "status")
    private Boolean status;
}
