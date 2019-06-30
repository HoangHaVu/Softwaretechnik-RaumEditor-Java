package roomieboomie.controller;

import roomieboomie.business.RoomieBoomieManager;

public class LoginController {
    private RootController switcher;
    private boolean adminValue;
    private RoomieBoomieManager roomieBoomieManager;

    public void chooseWhichUser(String username){
    }
    public void toMenu(){
        switcher.switchView("MainMenu");
    }
    public void setSwitcher(RootController rootController){
        this.switcher = rootController;
    }
    public void setRoomieBoomieManager (RoomieBoomieManager roomieBoomieManager){
        this.roomieBoomieManager = roomieBoomieManager;
    }
}
