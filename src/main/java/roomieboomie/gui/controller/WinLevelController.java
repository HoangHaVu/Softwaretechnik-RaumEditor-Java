package roomieboomie.gui.controller;

import roomieboomie.business.RoomieBoomieManager;

public class WinLevelController {
    private RoomieBoomieManager roomieBoomieManager;
    private RootController switcher;

    public void playNextLevel(){
        switcher.switchView("Play");
    }
    public void backToMenu(){
        switcher.switchView("MainMenu");
    }
    public void rageQuit(){

    }
    public void setSwitcher(RootController rootController){
        this.switcher = rootController;
    }
    public void setRoomieBoomieManager (RoomieBoomieManager roomieBoomieManager){
        this.roomieBoomieManager = roomieBoomieManager;
    }
}
