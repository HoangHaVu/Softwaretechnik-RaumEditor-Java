package roomieboomie.gui.controller;

import roomieboomie.business.RoomieBoomieManager;

public class SelectRoomController {
    private RoomieBoomieManager roomieBoomieManager;
    private RootController switcher;

    public void backToMenu(){
        switcher.switchView("MainMenu");
    }
    public void loadRoom(){
        switcher.switchView("LayoutEditor");

        switcher.switchView("Play");
    }
    public void setSwitcher(RootController rootController){
        this.switcher = rootController;
    }
    public void setRoomieBoomieManager (RoomieBoomieManager roomieBoomieManager){
        this.roomieBoomieManager = roomieBoomieManager;
    }
}
