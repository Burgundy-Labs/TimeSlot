package controllers.ApplicationComponents;

public class MenuLinks {
    public String href;
    public String menuText;     /* NOTE: Should match the "Title" for the page it goes to. */
    public String mdlIcon;     /* https://material.io/icons/ */
    public String desc;
    public boolean locked; // admin only
    public MenuLinks(String href, String menuText, String mdlIcon, String desc, boolean locked) {
        this.href = href;
        this.menuText = menuText;
        this.mdlIcon = mdlIcon;
        this.desc = desc;
        this.locked = locked;
    }
}