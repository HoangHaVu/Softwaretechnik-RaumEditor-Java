package roomieboomie.gui.controller;

import roomieboomie.business.RoomieBoomieManager;
import roomieboomie.business.room.Room;
import roomieboomie.business.room.RoomMaps;

public class SelectRoomController {
    private RoomieBoomieManager roomieBoomieManager;
    private RootController switcher;
    private RoomMaps roomMaps;

    public void backToMenu(){
        switcher.switchView("MainMenu");
    }
    public void loadRoom(){
        switcher.switchView("LayoutEditor");

        switcher.switchView("Play");
    }
    public void setSwitcher(RootController rootController){
        this.switcher = rootController;
    }
    public void setRoomieBoomieManager (RoomieBoomieManager roomieBoomieManager){
        this.roomieBoomieManager = roomieBoomieManager;
        init();
    }
    public void init(){
        setRoomMaps(roomieBoomieManager.getRoomMaps());
    }
    public void setRoomMaps(RoomMaps roomMaps){
        this.roomMaps = roomMaps;
    }
}
