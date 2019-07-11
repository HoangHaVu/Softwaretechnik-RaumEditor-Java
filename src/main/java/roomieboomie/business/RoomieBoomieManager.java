package roomieboomie.business;

import roomieboomie.business.editor.RoomEditor;
import roomieboomie.business.game.Game;
import roomieboomie.business.highscore.HighscoreList;
import roomieboomie.business.item.layout.LayoutItem;
import roomieboomie.business.item.placable.PlacableItem;
import roomieboomie.business.room.Room;
import roomieboomie.business.room.RoomMaps;
import roomieboomie.business.user.User;
import roomieboomie.business.user.UserMap;
import roomieboomie.persistence.JsonHandler;
import roomieboomie.persistence.exception.JsonLoadingException;

import java.util.HashMap;

/**
 * RoomieBoomieManager managed alle verwendeten Objekte und stellt die jeweils dem Controller zur Verfügung
 */
public class RoomieBoomieManager {
    private Game game; //TODO
    private RoomEditor roomEditor;
    private JsonHandler jsonHandler;
    private RoomMaps roomMaps;
    private UserMap userMap;
    private User currentUser; //TODO
    private HighscoreList highscoreListRanked; // TODO
    private HashMap<String, PlacableItem> placableItemMap;
    private HashMap<String, LayoutItem> layoutItemMap;

    /**
     * Konstruktor
     */
    public RoomieBoomieManager() {
        this.jsonHandler = new JsonHandler();

        this.roomEditor = new RoomEditor(jsonHandler);

        roomEditor.loadNewRoom("meinRaum", false);

        try {
            this.userMap = new UserMap(jsonHandler.getUserMap());
        } catch (JsonLoadingException e) {
            System.err.println(e.getMessage());
        }

        try {
            HashMap level = jsonHandler.getRoomMapLevel();
            HashMap creative = jsonHandler.getRoomMapCreative();
            this.roomMaps = new RoomMaps(level, creative);
        } catch (JsonLoadingException e) {
            System.err.println(e.getMessage());
        }
    }

    public UserMap getUserMap() {
        return userMap;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public RoomEditor getRoomEditor() {
        return roomEditor;
    }

    public Game getGame() {
        return game;
    }

    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

    public RoomMaps getRoomMaps() {
        return roomMaps;
    }

    public HighscoreList getOverallHighscore() {
        return roomMaps.getOverallHighscore();
    }

    public JsonHandler getJsonHandler() {
        return jsonHandler;
    }


    /**
     * startet eine neue Game-Session
     */
    public void initGame() {
    } //TODO

    /**
     * kreiert einen neuen Raum
     *
     * @return
     */
    public Room createRoom() { //TODO
        return null;
    }
}
