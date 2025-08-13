package api.authentication.to;

import java.util.List;

public class MenuTransaccionTo {

    private Long idMenu;

    private String opcionMenu;

    private Integer orden;

    private String icon;

    private List<MenuTo> menuToList;

    public MenuTransaccionTo(Long idMenu, String opcionMenu, Integer orden, String icon) {
        this.idMenu = idMenu;
        this.opcionMenu = opcionMenu;
        this.orden = orden;
        this.icon = icon;
    }

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

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public List<MenuTo> getMenuToList() {
        return menuToList;
    }

    public void setMenuToList(List<MenuTo> menuToList) {
        this.menuToList = menuToList;
    }
}
