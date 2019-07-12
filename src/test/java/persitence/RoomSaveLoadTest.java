package persitence;

import org.junit.Before;
import org.junit.Test;
import roomieboomie.business.highscore.HighscoreList;
import roomieboomie.business.highscore.HighscoreRecord;
import roomieboomie.business.item.Orientation;
import roomieboomie.business.item.layout.LayoutItem;
import roomieboomie.business.item.layout.LayoutItemType;
import roomieboomie.business.item.placable.PlacableItem;
import roomieboomie.business.item.placable.PlacableItemType;
import roomieboomie.business.room.Room;
import roomieboomie.business.room.RoomPreview;
import roomieboomie.business.user.User;
import roomieboomie.persistence.Config;
import roomieboomie.persistence.JsonHandler;
import roomieboomie.persistence.exception.JsonLoadingException;
import roomieboomie.persistence.exception.JsonValidatingException;
import roomieboomie.persistence.exception.JsonWritingException;

import java.util.ArrayList;

public class RoomSaveLoadTest {
    JsonHandler jsonHandler;
    HighscoreList highscoreList;
    User user1;
    User user2;
    HighscoreRecord record1;
    HighscoreRecord record2;
    Room roomToSave;
    String roomname;

    @Before
    public void init(){

        jsonHandler = new JsonHandler();
        highscoreList = new HighscoreList();

        user1 = new User("Joendhardt",3);
        user2 = new User("Joghurta",2);

        record1 = new HighscoreRecord(10, 1000, user1.getName());
        highscoreList.addRecord(record1);
        record2 = new HighscoreRecord(12,1500, user2.getName());
        highscoreList.addRecord(record2);

        roomname = "meinRaum";
        roomToSave = initRoom(highscoreList, roomname);
    }

    @Test
    /**
     * Testet, ob ein Raum gespeicherert werden kann
     */
    public void saveRoom(){
        boolean success = true;
        try {
            jsonHandler.saveRoom(roomToSave);
        } catch (JsonWritingException e) {
            success = false;
        }

        assert success;
    }

    @Test
    /**
     * Laedt einen Raum und testet, ob er mit dem gespeicherten uebereinstimmt
     */
    public void loadRoom(){
        boolean success = true;

        RoomPreview rp = null;
        Room loadedRoom = null;

        try {
            rp = jsonHandler.getCreativeRoomPreview(roomname);
        } catch (JsonValidatingException e) {
            success = false;
        } catch (JsonLoadingException e) {
            success = false;
        }

        try {
            loadedRoom = jsonHandler.getCreativeRoom(roomname, rp);
        } catch (JsonValidatingException e) {
            success = false;
        } catch (JsonLoadingException e) {
            success = false;
        }

        assert roomToSave.getName().equals(loadedRoom.getName());
        assert roomToSave.isLevel() == loadedRoom.isLevel();
        assert roomToSave.getHeight() == loadedRoom.getHeight();
        assert roomToSave.getWidth() == loadedRoom.getWidth();
        assert roomToSave.getStartX() == loadedRoom.getStartX();
        assert roomToSave.getPlacableItemList().size() == loadedRoom.getPlacableItemList().size();
        assert roomToSave.getWalls().size() == loadedRoom.getWalls().size();
        assert roomToSave.getWindows().size() == loadedRoom.getWindows().size();
        assert roomToSave.getDoors().size() == loadedRoom.getDoors().size();
        assert roomToSave.hashCode() == loadedRoom.hashCode();
        assert roomToSave.getRoomPreview().hashCode() == loadedRoom.getRoomPreview().hashCode();
        assert success;
    }

    public Room initRoom(HighscoreList list, String name){
        byte[][] layout = new byte[][]{
                { 1, 1, 1, 1, 1, 2, 2, 2, 1, 1 },
                { 1, 0, 0, 0, 0, 0, 0, 0, 0, 1 },
                { 3, 0, 1, 1, 1, 0, 0, 0, 0, 1 },
                { 3, 0, 1, 0, 0, 0, 0, 0, 0, 1 },
                { 1, 1, 1, 1, 3, 3, 1, 1, 1, 1 }
        };

        ArrayList<PlacableItem> placableItemList =  new ArrayList<>();
        placableItemList.add(new PlacableItem(1,2, Orientation.BOTTOM, PlacableItemType.BED));
        placableItemList.add(new PlacableItem(7,2, Orientation.TOP, PlacableItemType.CHAIR));
        placableItemList.add(new PlacableItem(1,2, Orientation.LEFT, PlacableItemType.COUCH));

        ArrayList<LayoutItem> walls = new ArrayList<>();
        walls.add(new LayoutItem(LayoutItemType.WALL, 0, 1, 5, 7, Orientation.TOP));
        walls.add(new LayoutItem(LayoutItemType.WALL, 4, 2, 6, 3, Orientation.RIGHT));

        ArrayList<LayoutItem> windows = new ArrayList<>();
        windows.add(new LayoutItem(LayoutItemType.WINDOW, 5, 1, 6, 1, Orientation.TOP));
        windows.add(new LayoutItem(LayoutItemType.WINDOW, 3, 2, 2, 2, Orientation.LEFT));

        ArrayList<LayoutItem> doors = new ArrayList<>();
        doors.add(new LayoutItem(LayoutItemType.DOOR, 3, 2, 2, 1, Orientation.BOTTOM));

        RoomPreview rp = new RoomPreview(name, list,50,false, jsonHandler);

        Room room = new Room(Config.get().MAXWIDTH(), Config.get().MAXHEIGHT(), rp);
        room.setLayout(layout);

        room = new Room(rp, layout, 0,0, placableItemList, walls, windows, doors);

        return room;
    }
}
