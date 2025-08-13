package api.apiAdminCommerce.repository.extDbRepository;

import api.apiAdminCommerce.entity.PreRegisterEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PreRegisterRepository extends JpaRepository<PreRegisterEntity, Long> {

    @Modifying
    @Query(value = "update PreRegisterEntity preRegisterEntity set " +
                   "preRegisterEntity.statusPreRegisterEntity.idStatusPreRegister = :idStatusPreRegister " +
                   "where preRegisterEntity.idPreRegistro = :idPreRegister")
    void updateStatusPreRegisterExt(@Param("idStatusPreRegister") Long idStatusPreRegister,
                                    @Param("idPreRegister") Long idPreRegister);

    @Modifying
    @Query(value = "update PreRegisterEntity preRegisterEntity set " +
            "preRegisterEntity.statusPreRegisterEntity.idStatusPreRegister = :idStatusPreRegister, " +
            "preRegisterEntity.rejectMotive = :rejectMotive " +
            "where preRegisterEntity.idPreRegistro = :idPreRegister")
    void updateStatusPreRegisterExtRejected(@Param("idStatusPreRegister") Long idStatusPreRegister,
                                            @Param("idPreRegister") Long idPreRegister,
                                            @Param("rejectMotive") String rejectMotive);

    PreRegisterEntity findByIdPreRegistro(Long idPreRegistro);

    List<PreRegisterEntity> findAllByStatusPreRegisterEntityIdStatusPreRegister(Long idStatus);
}
