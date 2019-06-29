package roomieboomie.business;

import roomieboomie.business.editor.RoomEditor;
import roomieboomie.business.game.Game;
import roomieboomie.business.item.layout.LayoutItem;
import roomieboomie.business.item.placable.PlacableItem;
import roomieboomie.business.room.Room;
import roomieboomie.business.room.RoomMaps;
import roomieboomie.business.user.User;
import roomieboomie.business.user.UserMap;
import roomieboomie.persistence.JsonHandler;
import roomieboomie.persistence.JsonLoadingException;

import java.util.HashMap;

public class RoomieBoomieManager {
    private Game game; //TODO
    private RoomEditor roomEditor; //TODO
    private JsonHandler jsonHandler;
    private RoomMaps roomMaps; //TODO
    private UserMap userMap; //TODO
    private User currentUser; //TODO
    private HashMap<String, PlacableItem> placableItemMap; //TODO
    private HashMap<String, LayoutItem> layoutItemMap; //TODO

    public RoomieBoomieManager(){
      init();
    }
    public UserMap getUserMap(){
        return userMap;
    }
    public User getCurrentUser(){
        return currentUser;
    }
    public RoomEditor getRoomEditor(){
        return roomEditor;
    }
    public Game getGame(){
        return game;
    }

    public void setCurrentUser(User user){
        this.currentUser = user;
    }


    public void init(){
        this.roomEditor = new RoomEditor();
        this.jsonHandler = new JsonHandler();

        try {
            this.userMap = new UserMap(jsonHandler.getUserMap());
        } catch (JsonLoadingException e) {
            e.printStackTrace();
        }
        try {
            HashMap level = jsonHandler.getRoomMapLevel();
            HashMap creative = jsonHandler.getRoomMapCreative();
            this.roomMaps = new RoomMaps(level,creative);
        } catch (JsonLoadingException e) {
            e.printStackTrace();
        }
    }

    public void initGame(){} //TODO
    public Room createRoom(){ //TODO
        return null;
    }
}
