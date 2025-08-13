package api.preRegistro.repository;

import api.preRegistro.entity.PreRegistroEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PreRegistroRepository extends JpaRepository<PreRegistroEntity, Long> {

    PreRegistroEntity findByIdPreRegistro(Long idPreRegistro);

    PreRegistroEntity findByCommerceDocument(String commerceDocument);

    @Query(value = "select preRegister from PreRegistroEntity  as preRegister " +
                   "where preRegister.statusPreRegistroEntity.idStatusPreRegistro <> :idStatusPreregister " +
                   "and preRegister.commerceDocument = :commerceDocument")
    PreRegistroEntity getPreregisterByCommerceDocumentAndIdStatusDocument(@Param("idStatusPreregister") Long idStatusPreregister,
                                                                          @Param("commerceDocument") String commerceDocument);

}
