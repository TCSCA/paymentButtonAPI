package api.apiB2p.entity;

import com.google.gson.annotations.Expose;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@Entity
@Table(name = "t_banco_comercio")
@NoArgsConstructor
public class BankCommerceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "t_banco_comercio_seq")
    @SequenceGenerator(name = "t_banco_comercio_seq", sequenceName = "t_banco_comercio_seq", allocationSize = 1)
    @Column(name = "id_banco_comercio")
    @Expose
    private Long idBankCommerce;

    @ManyToOne()
    @Expose
    @JoinColumn(name = "id_comercio")
    private CommerceEntity commerceEntity;

    @ManyToOne()
    @Expose
    @JoinColumn(name = "id_banco")
    private BankEntity bankEntity;

    @Column(name = "consumer_key")
    private String consumerKey;

    @Column(name = "consumer_secret")
    private String consumerSecret;

    @Column(name = "telefono")
    private String commercePhone;

    @Column(name = "hash")
    private String bankHash;

    @Column(name = "token_banco")
    private String token;

    @Column(name = "status")
    private Boolean status;

    @Column(name = "expiry_date_token")
    private OffsetDateTime tokenExpireDate;
}
