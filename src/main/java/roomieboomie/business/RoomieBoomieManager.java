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
    private JsonHandler jsonHandler = new JsonHandler();
    private RoomMaps roomMaps; //TODO
    private UserMap userMap; //TODO
    private User currentUser; //TODO
    private HashMap<String, PlacableItem> placableItemMap; //TODO
    private HashMap<String, LayoutItem> layoutItemMap; //TODO

    public RoomieBoomieManager(){
        try {
            userMap = new UserMap(jsonHandler.getUserMap());
        } catch (JsonLoadingException e) {
            e.printStackTrace();
        }
    }

    public void init(){} //TODO
    public void initGame(){} //TODO
    public Room createRoom(){ //TODO
        return null;
    }
}
