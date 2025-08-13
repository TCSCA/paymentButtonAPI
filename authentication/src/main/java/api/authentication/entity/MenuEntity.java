package api.authentication.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "t_menu")
public class MenuEntity {

    @Id
    @Column(name = "id_menu")
    private Long idMenu;

    @Column(name = "opcion_menu")
    private String opcionMenu;

    @Column(name = "orden")
    private Integer orden;

    @Column(name = "url")
    private String url;

    @Column(name = "icon")
    private String icon;

    @ManyToOne
    @JoinColumn(name = "padre")
    private MenuEntity menuEntity;

    @ManyToOne
    @JoinColumn(name = "id_seccion")
    private SeccionEntity seccionEntity;

    public Long getIdMenu() {
        return idMenu;
    }

    public void setIdMenu(Long idMenu) {
        this.idMenu = idMenu;
    }

    public String getOpcionMenu() {
        return opcionMenu;
    }

    public void setOpcionMenu(String opcionMenu) {
        this.opcionMenu = opcionMenu;
    }

    public Integer getOrden() {
        return orden;
    }

    public void setOrden(Integer orden) {
        this.orden = orden;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public MenuEntity getMenuEntity() {
        return menuEntity;
    }

    public void setMenuEntity(MenuEntity menuEntity) {
        this.menuEntity = menuEntity;
    }

    public SeccionEntity getSeccionEntity() {
        return seccionEntity;
    }

    public void setSeccionEntity(SeccionEntity seccionEntity) {
        this.seccionEntity = seccionEntity;
    }

}
