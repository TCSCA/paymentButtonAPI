package api.apiB2p.entity;
import com.google.gson.annotations.Expose;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

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
