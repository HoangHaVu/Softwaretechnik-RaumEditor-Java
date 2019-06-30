package roomieboomie.controller;

import roomieboomie.business.RoomieBoomieManager;

public class MainMenuController {
    private RootController switcher;
    private RoomieBoomieManager roomieBoomieManager;

    public void choosePlay(){
        switcher.switchView("ChoosePlay");
    }
    public void toTutorial(){
        switcher.switchView("Tutorial");
    }
    public void chooseEdit(){
        switcher.switchView("ChooseEdit");
    }
    public void toHighscore(){
        switcher.switchView("Highscore");
    }
    public void setSwitcher(RootController rootController){
        this.switcher = rootController;
    }
    public void setRoomieBoomieManager (RoomieBoomieManager roomieBoomieManager){
        this.roomieBoomieManager = roomieBoomieManager;
    }

}
