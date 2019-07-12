package highscore;

import org.junit.Before;
import org.junit.Test;
import roomieboomie.business.highscore.HighscoreList;
import roomieboomie.business.highscore.HighscoreRecord;
import roomieboomie.business.room.RoomMaps;
import roomieboomie.business.room.RoomPreview;
import roomieboomie.business.user.User;

import java.util.HashMap;

public class HighscoreTest {

    User joendhard;
    User joghurta;
    User willi;
    User trostpreisfred;
    HighscoreList highscoreList;

    @Before
    public void init(){
        joendhard = new User("Joendhard");
        joghurta = new User("Joghurta");
        willi = new User("Willi");
        trostpreisfred = new User("Trostpreisfred");
        highscoreList = new HighscoreList();
    }

    @Test
    /**
     * Testet, ob Highscoren korrekt hinzugefuegt und sortiert werden
     */
    public void testHighscores(){
        HighscoreRecord record3 = new HighscoreRecord(12, 900, willi.getName()); //Platz 2
        HighscoreRecord record1 = new HighscoreRecord(14, 1100, joendhard.getName()); //Platz 0
        HighscoreRecord record4 = new HighscoreRecord(8, 700, trostpreisfred.getName()); //Platz 3
        HighscoreRecord record2 = new HighscoreRecord(10, 900, joghurta.getName()); //Platz 1

        highscoreList.addRecord(record1);
        highscoreList.addRecord(record2);
        highscoreList.addRecord(record3);
        highscoreList.addRecord(record4);

        assert highscoreList.getHighestScore() == 1100;
        assert highscoreList.getList().get(0).getUsername().equals(joendhard.getName());
        assert highscoreList.getList().get(1).getUsername().equals(joghurta.getName());
        assert highscoreList.getList().get(2).getUsername().equals(willi.getName());
        assert highscoreList.getList().get(3).getUsername().equals(trostpreisfred.getName());
    }

    @Test
    /**
     * Testet, ob beim Erstellen des grossen Highscores jeder User nur ein Mal mit seinem besten Score beruecksichtigt wird
     */
    public void testOverallHighscore(){
        HighscoreRecord record1 = new HighscoreRecord(12, 900, willi.getName()); //sollte nicht beruecksichtig werden
        HighscoreRecord record2 = new HighscoreRecord(14, 1100, willi.getName());
        HighscoreRecord record3 = new HighscoreRecord(8, 950, joghurta.getName());
        HighscoreRecord record4 = new HighscoreRecord(10, 700, joghurta.getName()); //sollte nicht beruecksichtig werden

        HighscoreList highscoreList1 = new HighscoreList();
        highscoreList1.addRecord(record1);
        highscoreList1.addRecord(record2);
        HighscoreList highscoreList2 = new HighscoreList();
        highscoreList2.addRecord(record3);
        highscoreList2.addRecord(record4);

        RoomPreview roomPreview1 = new RoomPreview("room1",highscoreList1, 200, true, null);
        RoomPreview roomPreview2 = new RoomPreview("room2",highscoreList2, 200, true, null);

        HashMap<String, RoomPreview> roomMap = new HashMap<>();
        roomMap.put(roomPreview1.getName(), roomPreview1);
        roomMap.put(roomPreview2.getName(), roomPreview2);

        RoomMaps roomMaps = new RoomMaps(roomMap,null);

        HighscoreList overallHighscore = roomMaps.getOverallHighscore();

        assert overallHighscore.getList().size() == 2;
        assert overallHighscore.getHighestScore() == 1100;
        assert overallHighscore.getList().get(0).getUsername().equals(willi.getName());
        assert overallHighscore.getList().get(0).getPoints() == 1100;
        assert overallHighscore.getList().get(1).getUsername().equals(joghurta.getName());
        assert overallHighscore.getList().get(1).getPoints() == 950;
    }
}
