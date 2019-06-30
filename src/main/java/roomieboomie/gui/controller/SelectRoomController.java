package roomieboomie.gui.controller;

import roomieboomie.business.RoomieBoomieManager;
import roomieboomie.business.room.Room;
import roomieboomie.business.room.RoomMaps;

import java.util.Collection;

public class SelectRoomController {
    private RoomieBoomieManager roomieBoomieManager;
    private RootController switcher;
    private RoomMaps roomMaps;
    private boolean creative;
    private Collection levelRooms;
    private Collection creativeRooms;
    private Collection showRooms;

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
    public void setCreative(boolean value){
        this.creative = value;
    }
    public void init(){
        setRoomMaps(roomieBoomieManager.getRoomMaps());
        this.levelRooms = roomMaps.getLevelRooms();
        this.creativeRooms = roomMaps.getCreativeRooms();
        if(creative){
            this.showRooms = creativeRooms;
        }else{
            this.showRooms = levelRooms;
        }
    }

    public void setRoomMaps(RoomMaps roomMaps){
        this.roomMaps = roomMaps;
    }
}
