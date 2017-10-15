package controllers.ApplicationComponents;

public class MenuLink {
    public String href;
    public String menuText;     /* NOTE: Should match the "Title" for the page it goes to. */
    public String mdlIcon;     /* https://material.io/icons/ */
    public String desc;

    public MenuLink(String href, String menuText, String mdlIcon, String desc) {
        this.href = href;
        this.menuText = menuText;
        this.mdlIcon = mdlIcon;
        this.desc = desc;
    }
}