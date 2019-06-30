package roomieboomie.controller;

import roomieboomie.business.RoomieBoomieManager;
import roomieboomie.business.editor.RoomEditor;

public class LayoutEditorController {
    private RootController switcher;
    private RoomieBoomieManager roomieBoomieManager;
    private RoomEditor roomEditor;


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
        init();
    }
    public void init(){
        setRoomEditor(roomieBoomieManager.getRoomEditor());
    }
    public void setRoomEditor(RoomEditor roomEditor){
        this.roomEditor=roomEditor;
    }
}
