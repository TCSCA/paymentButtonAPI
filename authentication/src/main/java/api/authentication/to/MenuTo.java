package api.authentication.to;

public class MenuTo {

    private Long idMenu;

    private String opcionMenu;

    private Integer orden;

    private String url;

    private String icon;

    private Long idSeccion;

    public MenuTo(Long idMenu, String opcionMenu, Integer orden, String url, String icon, Long idSeccion) {
        this.idMenu = idMenu;
        this.opcionMenu = opcionMenu;
        this.orden = orden;
        this.url = url;
        this.icon = icon;
        this.idSeccion = idSeccion;
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

    public Long getIdSeccion() {
        return idSeccion;
    }

    public void setIdSeccion(Long idSeccion) {
        this.idSeccion = idSeccion;
    }
}
