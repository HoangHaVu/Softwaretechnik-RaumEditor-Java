package singleFunctions;

import org.junit.Before;
import org.junit.Test;
import persitence.RoomSaveLoadTest;
import roomieboomie.business.highscore.HighscoreList;
import roomieboomie.business.highscore.HighscoreRecord;
import roomieboomie.business.room.Room;
import roomieboomie.business.room.RoomPreview;
import roomieboomie.business.user.User;

/**
 * Testet, ob der HashCode bei 17 Aufrufen immer gleich ist
 */
public class HashCodesTest {
    private Room room;
    private RoomPreview roomPreview;
    private HighscoreList highscoreList;
    private User user;

    @Before
    public void init(){
        RoomSaveLoadTest roomGetter = new RoomSaveLoadTest();
        user = new User("Joendhardt",3);
        highscoreList = new HighscoreList();

        HighscoreRecord record = new HighscoreRecord(10, 1000, user.getName());
        highscoreList.addRecord(record);

        room = roomGetter.initRoom(highscoreList, "Raum1");

        roomPreview = new RoomPreview("Raum2", highscoreList,50,false, null);
    }

    @Test
    public void testUser(){
        int hash = this.user.hashCode();
        for (int i = 0; i < 17; i++) {
            assert this.user.hashCode() == hash;
        }
    }

    @Test
    public void testRoom(){
        int hash = this.room.hashCode();
        for (int i = 0; i < 17; i++) {
            assert this.room.hashCode() == hash;
        }
    }

    @Test
    public void testRoomPreview(){
        int hash = this.roomPreview.hashCode();
        for (int i = 0; i < 17; i++) {
            assert this.roomPreview.hashCode() == hash;
        }
    }
}
