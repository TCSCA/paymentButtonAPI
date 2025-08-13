package api.authentication.repository;

import api.authentication.entity.MenuEntity;
import api.authentication.projection.MenuProjection;
import api.authentication.projection.MenuTransaccionProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuRepository extends JpaRepository<MenuEntity, Long> {

    @Query("select distinct menuPadre.idMenu as idMenu, menuPadre.opcionMenu as opcionMenu, " +
            "menuPadre.orden as orden, menuPadre.icon as icon from MenuEntity as menu " +
            "inner join MenuEntity as menuPadre on menuPadre.idMenu = menu.menuEntity.idMenu " +
            "inner join MenuProfileEntity as mp on menuPadre.idMenu = mp.menuEntity.idMenu " +
            "inner join ProfileEntity as profile on profile.idProfile = mp.profileEntity.idProfile " +
            "where profile.idProfile = :idProfile and mp.status is true order by menuPadre.orden asc")
    List<MenuTransaccionProjection> findMenuEntityByIdProfile(@Param("idProfile") Long idProfile);

    @Query(value = "select menu.idMenu as idMenu, menu.opcionMenu as opcionMenu, " +
            "menu.orden as orden, menu.url as url, menu.icon as icon, seccion.idSeccion as idSeccion " +
            "from MenuEntity as menu " +
            "inner join MenuEntity as menuPadre on menuPadre.idMenu = menu.menuEntity.idMenu " +
            "inner join SeccionEntity as seccion on seccion.idSeccion = menu.seccionEntity.idSeccion " +
            "inner join MenuProfileEntity as mp on menu.idMenu = mp.menuEntity.idMenu " +
            "inner join ProfileEntity as profile on profile.idProfile = mp.profileEntity.idProfile " +
            "where menu.menuEntity.idMenu = :idMenu and mp.status is true " +
            "and profile.idProfile = :idProfile order by menu.orden asc")
    List<MenuProjection> findByIdMenuPadre(@Param("idMenu") final Long idMenu,
                                           @Param("idProfile") final Long idProfile);

}
