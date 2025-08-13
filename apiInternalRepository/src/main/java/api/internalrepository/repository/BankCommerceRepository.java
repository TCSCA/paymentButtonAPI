package api.internalrepository.repository;

import api.internalrepository.entity.BankCommerceEntity;
import api.internalrepository.to.BankTransactionListTo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BankCommerceRepository extends JpaRepository<BankCommerceEntity, Long> {

    @Query(value = "select bankCommerce from BankCommerceEntity bankCommerce " +
                   "inner join CommerceEntity as commerce on bankCommerce.commerceEntity.idCommerce = commerce.idCommerce " +
                   "inner join BankEntity as bank on bank.idBank = bankCommerce.bankEntity.idBank " +
                   "where bank.idBank <> 99 " +
                   "and commerce.idCommerce = :idCommerce " +
                   "and bankCommerce.status = true")
    BankCommerceEntity getBankCommerceInformationByCommerceId(@Param("idCommerce") Long idCommerce);


}
