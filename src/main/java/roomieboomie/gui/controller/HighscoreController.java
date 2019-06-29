package roomieboomie.gui.controller;

import roomieboomie.business.RoomieBoomieManager;

public class HighscoreController {
    private RoomieBoomieManager roomieBoomieManager;
    private RootController switcher;

    public void backToMenu(){
        switcher.switchView("MainMenu");
    }
    public void setSwitcher(RootController rootController){
        this.switcher = rootController;
    }
    public void setRoomieBoomieManager (RoomieBoomieManager roomieBoomieManager){
        this.roomieBoomieManager = roomieBoomieManager;
    }
}
