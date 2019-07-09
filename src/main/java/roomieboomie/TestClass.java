package roomieboomie;

import roomieboomie.business.highscore.HighscoreList;
import roomieboomie.business.highscore.HighscoreRecord;
import roomieboomie.business.item.Orientation;
import roomieboomie.business.item.placable.PlacableItem;
import roomieboomie.business.item.placable.PlacableItemType;
import roomieboomie.business.room.Room;
import roomieboomie.business.room.RoomMaps;
import roomieboomie.business.room.RoomPreview;
import roomieboomie.business.user.User;
import roomieboomie.business.user.UserMap;
import roomieboomie.persistence.*;
import roomieboomie.persistence.exception.JsonLoadingException;
import roomieboomie.persistence.exception.JsonValidatingException;
import roomieboomie.persistence.exception.JsonWritingException;

import java.util.ArrayList;

public class TestClass {
    private static JsonHandler jHandler = new JsonHandler();

    public static void run(){

        RoomMaps roomMaps = getRoomMaps();
        UserMap userMap = getUserMap();

        User mawien = userMap.getUser("Mawien");
        User roomie = userMap.getUser("roomie");
        User boomie = userMap.getUser("boomie");

        HighscoreList list = new HighscoreList();
        HighscoreRecord r1 = new HighscoreRecord(10, 1000, mawien.getName());
        HighscoreRecord r2 = new HighscoreRecord(8, 1000, roomie.getName());
        HighscoreRecord r3 = new HighscoreRecord(10, 1200, mawien.getName());
        HighscoreRecord r4 = new HighscoreRecord(6, 10, boomie.getName());

        list.addRecord(r1);
        list.addRecord(r2);
        list.addRecord(r3);
        list.addRecord(r4);

        Room room1 = getRoom(list, "Raum1");

        //testRoom.setPlacableItemList(itemList);
        //saveRoom(testRoom);

        /*saveRoom(room1);

        HighscoreList list2 = new HighscoreList();
        HighscoreRecord r5 = new HighscoreRecord(6, 800, roomie.getName());
        HighscoreRecord r6 = new HighscoreRecord(10, 240, roomie.getName());
        HighscoreRecord r7 = new HighscoreRecord(10, 100, boomie.getName());
        HighscoreRecord r8 = new HighscoreRecord(9, 900, mawien.getName());

        list2.addRecord(r5);
        list2.addRecord(r6);
        list2.addRecord(r7);
        list2.addRecord(r8);

        Room room2 = getRoom(list2, "Raum2");
        saveRoom(room2);*/

        //Room loadedRoom = loadRoom("Raum1");
    }

    private static void saveRoom(Room room){
        try {
            jHandler.saveRoom(room);
        } catch (JsonWritingException e) {
            e.printStackTrace();
        }
    }

    private static Room loadRoom(String name) {
        RoomPreview rp = null;
        try {
            rp = jHandler.getLevelRoomPreview(name);
        } catch (JsonValidatingException e) {
            e.printStackTrace();
        } catch (JsonLoadingException e) {
            e.printStackTrace();
        }

        Room room = null;
        try {
            room = rp.getFullRoom();
        } catch (JsonValidatingException e) {
            e.printStackTrace();
        } catch (JsonLoadingException e) {
            e.printStackTrace();
        }
        return room;
    }

    private static Room getRoom(HighscoreList list, String name){
        byte[][] layout = new byte[][]{
                { 1, 1, 1, 1, 1, 2, 2, 2, 1, 1 },
                { 1, 0, 0, 0, 0, 0, 0, 0, 0, 1 },
                { 3, 0, 1, 1, 1, 0, 0, 0, 0, 1 },
                { 3, 0, 1, 0, 0, 0, 0, 0, 0, 1 },
                { 1, 1, 1, 1, 3, 3, 1, 1, 1, 1 }
        };

        ArrayList<PlacableItem> itemList =  new ArrayList<>();
        itemList.add(new PlacableItem(1,2, Orientation.BOTTOM, PlacableItemType.BED));
        itemList.add(new PlacableItem(7,2, Orientation.TOP, PlacableItemType.SHELF));
        itemList.add(new PlacableItem(1,2, Orientation.LEFT, PlacableItemType.COUCH));

        RoomPreview rp = new RoomPreview(name, list,50,true, jHandler);

        Room room = null;

        //room = new Room(rp, layout, 0, 0, itemList);

        return room;
    }

    private static UserMap getUserMap() {
        UserMap userMap = null;
        try {
            userMap = new UserMap(jHandler.getUserMap());
        } catch (JsonLoadingException e) {
            e.printStackTrace();
        }
        return userMap;
    }

    private static RoomMaps getRoomMaps(){
        RoomMaps roomMaps = null;
        try {
            roomMaps = new RoomMaps(jHandler.getRoomMapLevel(), jHandler.getRoomMapCreative());
        } catch (JsonLoadingException e) {
            e.printStackTrace();
        }
        return roomMaps;
    }
}
