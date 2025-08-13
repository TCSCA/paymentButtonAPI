package api.apiP2c.entity;

import com.google.gson.annotations.Expose;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "t_mensaje")
@NoArgsConstructor
public class MessageBankEntity {

    @Id
    @Column(name = "id_mensaje")
    @Expose
    private Long idBankMessage;

    @Column(name = "codigo")
    private String code;

    @Column(name = "mensaje")
    private String message;

    @ManyToOne()
    @Expose
    @JoinColumn(name = "id_bank")
    private BankEntity bankEntity;

    @Column(name = "status")
    private Boolean status;
}
