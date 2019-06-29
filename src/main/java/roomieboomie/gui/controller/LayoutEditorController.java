package roomieboomie.gui.controller;

import roomieboomie.business.RoomieBoomieManager;

public class LayoutEditorController {
    private RootController switcher;
    private RoomieBoomieManager roomieBoomieManager;


    public void nextToPlaceObject(){
        switcher.switchView("PlaceableEditor");
    }
    public void backToMenu(){
        switcher.switchView("MainManu");
    }
    public void addLayoutItem(){

    }
    public void setSwitcher(RootController rootController){
        this.switcher = rootController;
    }
    public void setRoomieBoomieManager (RoomieBoomieManager roomieBoomieManager){
        this.roomieBoomieManager = roomieBoomieManager;
    }

}
