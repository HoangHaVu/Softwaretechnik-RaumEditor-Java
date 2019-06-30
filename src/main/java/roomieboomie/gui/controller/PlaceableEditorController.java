package roomieboomie.gui.controller;

import roomieboomie.business.RoomieBoomieManager;
import roomieboomie.business.editor.RoomEditor;

public class PlaceableEditorController {
    private RoomieBoomieManager roomieBoomieManager;
    private RootController switcher;
    private RoomEditor roomEditor;


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
        init();
    }
    public void init(){
        setRoomEditor(roomieBoomieManager.getRoomEditor());
    }
    public void setRoomEditor (RoomEditor roomEditor){
        this.roomEditor = roomEditor;
    }
}
