package roomieboomie.business.room;

import roomieboomie.business.highscore.HighscoreList;
import roomieboomie.business.highscore.HighscoreRecord;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Room Maps verwaltet alle Level-Rooms und Creative-Rooms in getrennten HashMaps.
 * Die Room-Objekte sind ueber ihren Namen abrufbar (get__Room()) oder komplett in Collections (get__Rooms()) abfragbar
 */
public class RoomMaps {
    private HashMap<String, RoomPreview> levelRooms = new HashMap<>();
    private HashMap<String, RoomPreview> creativeRooms = new HashMap<>();

    /**
     * Neue RoomMaps, die Level-Rooms und Creative-Rooms verwaltet
     * @param levelMap HashMap mit Level-Rooms
     * @param creativeMap HashMap Creative-Rooms
     */
    public RoomMaps(HashMap<String,RoomPreview> levelMap, HashMap<String,RoomPreview> creativeMap) {
        this.levelRooms = levelMap;
        this.creativeRooms = creativeMap;
    }

    /**
     * Fuegt eine RoomPreview in die Level-Map ein
     * @param room RoomPreview, die hinzugefuegt werden soll
     */
    public void addLevelRoom(RoomPreview room){
        levelRooms.put(room.getName(), room);
    }

    /**
     * Fuegt eine RoomPreview in die Creative-Map ein
     * @param room RoomPreview, die hinzugefuegt werden soll
     */
    public void addCreativeRoom(RoomPreview room){
        creativeRooms.put(room.getName(), room);
    }

    /**
     * Loescht eine RoomPreview aus der Level-Map
     * @param roomName Name der RoomPreview, die geloescht werden soll
     */
    public void deleteLevelRoom(String roomName){
        levelRooms.remove(roomName);
    }

    /**
     * Loescht eine RoomPreview aus der Creative-Map
     * @param roomName Name der RoomPreview, die geloescht werden soll
     */
    public void deleteCreativeRoom(String roomName){
        creativeRooms.remove(roomName);
    }

    /**
     * Gibt eine RoomPreview aus der Level-Map zurueck
     * @param roomName Name der RoomPreview
     */
    public RoomPreview getLevelRoom(String roomName){
        return levelRooms.get(roomName);
    }

    /**
     * Gibt eine RoomPreview aus der Creative-Map zurueck
     * @param roomName Name der RoomPreview
     */
    public RoomPreview getCreativeRoom(String roomName){
        return creativeRooms.get(roomName);
    }

    /**
     * Gibt alle RoomPreviews aus der Level-Map zurueck
     * @return RoomPreviews als Collection
     */
    public Collection<RoomPreview> getLevelRooms(){
        return levelRooms.values();
    }

    /**
     * Gibt alle RoomPreviews aus der Creative-Map zurueck
     * @return RoomPreviews als Collection
     */
    public Collection<RoomPreview> getCreativeRooms(){
        return creativeRooms.values();
    }

    /**
     * Rechnet die Highscore-Werte der Highscores aller Rooms zusammen, um einen gesamt-Hoghscore zu erstellen.
     * Dabei wird pro Room nur der jeweils beste Score eines Users berücksichtigt.
     * @return HighscoreList mit akkumulierten Scores
     */
    public HighscoreList getOverallHighscore() {
        HighscoreList highscoreList = new HighscoreList();
        HashMap<String, Integer> userScoreMap = new HashMap<>();

        for (RoomPreview roomPreview : levelRooms.values()) { //Für jeden Raum
            HighscoreList currHighscoreList = roomPreview.getHighscoreList();
            ArrayList<String> registredUsernames = new ArrayList<>(); //Zum User registrieren

            for (HighscoreRecord record : currHighscoreList) { //Liste rausziehen und durchiterieren
                String username = record.getUsername();
                if (!registredUsernames.contains(username)) { //Wenn noch nicht drin
                    registredUsernames.add(username);// User für diesen Room registrieren
                    if (!userScoreMap.containsKey(username)){
                        userScoreMap.put(username, record.getPoints());
                    } else{
                        userScoreMap.put(username, userScoreMap.get(username) + record.getPoints());
                    }
                }
            }
        }

        for (Map.Entry<String,Integer> userScore : userScoreMap.entrySet()){
            String username = userScore.getKey();
            int score = userScore.getValue();
            HighscoreRecord record = new HighscoreRecord(score, username);
            highscoreList.addRecord(record);
        }

        return highscoreList;
    }
}
