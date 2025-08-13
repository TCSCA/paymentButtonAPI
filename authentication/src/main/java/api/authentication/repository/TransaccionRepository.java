package api.authentication.repository;

import api.authentication.entity.TransaccionEntity;
import api.authentication.projection.TransaccionProjection;
import api.authentication.to.TransaccionTo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransaccionRepository extends JpaRepository<TransaccionEntity, Long> {

    @Query(value = "select traba.idTransaccion as idTransaccion, traba.transaccion as transaccion " +
            "from TransaccionEntity as traba " +
            "inner join TransaccionUsuarioEntity as tp on traba.idTransaccion = tp.transaccionEntity.idTransaccion " +
            "inner join UserEntity as user on user.idUser = tp.userEntity.idUser " +
            "inner join SeccionEntity as seccion on seccion.idSeccion = traba.seccionEntity.idSeccion " +
            "where user.idUser = :idUser and seccion.idSeccion = :idSeccion ")
    List<TransaccionProjection> findByIdProfileAndIdSeccion(@Param("idUser") final Long idUser,
                                                            @Param("idSeccion") final Long idSeccion);

}
