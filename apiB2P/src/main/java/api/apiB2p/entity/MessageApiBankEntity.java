package api.apiB2p.entity;

import com.google.gson.annotations.Expose;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "t_respuesta_sistema")
@NoArgsConstructor
public class MessageApiBankEntity {

    @Id
    @Column(name = "id_respuesta_sistema")
    @Expose
    private Long idMessageApiBank;

    @ManyToOne()
    @Expose
    @JoinColumn(name = "id_mensaje_banco")
    private MessageBankEntity messageBankEntity;

    @ManyToOne()
    @Expose
    @JoinColumn(name = "id_mensaje_sistema")
    private MessageApiEntity messageApiEntity;

}
