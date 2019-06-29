package roomieboomie.gui.controller;

import roomieboomie.business.RoomieBoomieManager;

public class ChoosePlayController {
    private RoomieBoomieManager roomieBoomieManager;
    private RootController switcher;


    public void creativePlayMod(){
        switcher.setCreative(true);
        switcher.switchView("SelectRoom");
    }
    public void LevelPlayMod(){
        switcher.setCreative(false);
        switcher.switchView("SelectRoom");
    }
    public void setSwitcher(RootController rootController){
        this.switcher = rootController;
    }
    public void setRoomieBoomieManager (RoomieBoomieManager roomieBoomieManager){
        this.roomieBoomieManager = roomieBoomieManager;
    }
}
