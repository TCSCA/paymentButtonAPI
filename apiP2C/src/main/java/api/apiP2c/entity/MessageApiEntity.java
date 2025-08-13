package api.apiP2c.entity;

import com.google.gson.annotations.Expose;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "t_mensaje_sistema")
@NoArgsConstructor
public class MessageApiEntity {

    @Id
    @Column(name = "id_mensaje_sistema")
    @Expose
    private Long idMessageApi;

    @Column(name = "codigo")
    private String code;

    @Column(name = "mensaje_sistema")
    private String message;

    @Column(name = "status")
    private Boolean status;

}
