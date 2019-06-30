package roomieboomie.gui.controller;

import roomieboomie.business.RoomieBoomieManager;

public class GameOverController {
    private RootController switcher;
    private RoomieBoomieManager roomieBoomieManager;


    public void playAgain(){

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
