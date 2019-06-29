package roomieboomie.gui.controller;

import roomieboomie.business.RoomieBoomieManager;

public class PlaceableEditorController {
    private RoomieBoomieManager roomieBoomieManager;
    private RootController switcher;


    public void saveRoom(){
        switcher.switchView("MainMenu");
    }
    public void backToLayout(){
        switcher.switchView("SelectRoom");
    }
    public void addPlaceableItem(){

    }
    public void setSwitcher(RootController rootController){
        this.switcher = rootController;
    }
    public void setRoomieBoomieManager (RoomieBoomieManager roomieBoomieManager){
        this.roomieBoomieManager = roomieBoomieManager;
    }
}
