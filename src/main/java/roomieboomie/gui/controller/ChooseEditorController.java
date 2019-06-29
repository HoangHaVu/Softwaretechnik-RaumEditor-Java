package roomieboomie.gui.controller;

import roomieboomie.business.RoomieBoomieManager;

public class ChooseEditorController {
    private RootController switcher;
    private RoomieBoomieManager roomieBoomieManager;

    public void createNewRoom(){
        switcher.switchView("LayoutEditor");
    }
    public void loadRoom(){
        switcher.switchView("SelectRoom");
    }
    public void setSwitcher(RootController rootController){
        this.switcher = rootController;
    }
    public void setRoomieBoomieManager (RoomieBoomieManager roomieBoomieManager){
        this.roomieBoomieManager = roomieBoomieManager;
    }
    public void backToMenu(){
        switcher.switchView("MainMenu");
    }

}
