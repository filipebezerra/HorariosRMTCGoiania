package mx.x10.filipebezerra.horariosrmtcgoiania.view.util.fragment;

import android.widget.BaseAdapter;

import mx.x10.filipebezerra.horariosrmtcgoiania.model.widget.NavDrawerItem;

/**
 * @author Michenux (http://www.michenux.net/android-navigation-drawer-748.html)
 * @since 2.0
 */
public class NavDrawerActivityConfiguration {
    private int drawerShadow;
    private int drawerLayoutId;
    private int leftDrawerId;
    private int[] actionMenuItemsToHideWhenDrawerOpen;
    private NavDrawerItem[] navItems;
    private int drawerOpenDesc;
    private int drawerCloseDesc;
    private BaseAdapter baseAdapter;

    public int getDrawerShadow() {
        return drawerShadow;
    }

    public void setDrawerShadow(int drawerShadow) {
        this.drawerShadow = drawerShadow;
    }

    public int getDrawerLayoutId() {
        return drawerLayoutId;
    }

    public void setDrawerLayoutId(int drawerLayoutId) {
        this.drawerLayoutId = drawerLayoutId;
    }

    public int getLeftDrawerId() {
        return leftDrawerId;
    }

    public void setLeftDrawerId(int leftDrawerId) {
        this.leftDrawerId = leftDrawerId;
    }

    public int[] getActionMenuItemsToHideWhenDrawerOpen() {
        return actionMenuItemsToHideWhenDrawerOpen;
    }

    public void setActionMenuItemsToHideWhenDrawerOpen(
            int[] actionMenuItemsToHideWhenDrawerOpen) {
        this.actionMenuItemsToHideWhenDrawerOpen = actionMenuItemsToHideWhenDrawerOpen;
    }

    public NavDrawerItem[] getNavItems() {
        return navItems;
    }

    public void setNavItems(NavDrawerItem[] navItems) {
        this.navItems = navItems;
    }

    public int getDrawerOpenDesc() {
        return drawerOpenDesc;
    }

    public void setDrawerOpenDesc(int drawerOpenDesc) {
        this.drawerOpenDesc = drawerOpenDesc;
    }

    public int getDrawerCloseDesc() {
        return drawerCloseDesc;
    }

    public void setDrawerCloseDesc(int drawerCloseDesc) {
        this.drawerCloseDesc = drawerCloseDesc;
    }

    public BaseAdapter getBaseAdapter() {
        return baseAdapter;
    }

    public void setBaseAdapter(BaseAdapter baseAdapter) {
        this.baseAdapter = baseAdapter;
    }
}