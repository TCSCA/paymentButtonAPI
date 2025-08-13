package api.apiAdminCommerce.repository.intDbRepository.intDbConnection;

import api.apiAdminCommerce.entity.PaymentChannel;
import api.apiAdminCommerce.entity.TransactionTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaymentChannelRepository extends JpaRepository<PaymentChannel, Long> {

    List<PaymentChannel> findAllByStatusTrue();
}
