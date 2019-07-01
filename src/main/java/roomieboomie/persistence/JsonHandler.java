package roomieboomie.persistence;

import roomieboomie.business.highscore.HighscoreList;
import roomieboomie.business.highscore.HighscoreRecord;
import roomieboomie.business.item.placable.PlacableItem;
import roomieboomie.business.item.placable.PlacableItemType;
import roomieboomie.business.room.Room;
import roomieboomie.business.room.RoomPreview;
import roomieboomie.business.user.User;

import javax.json.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Stream;

/**
 * Schnittstelle zu persistenten Dateien. Kann sowohl gespeicherte {@link User} und {@link Room}s laden und speichern
 */
public class JsonHandler {
    private final String RESOURCESPATH = "src/main/resources/";
    private final String CONFIGNAME = "config.json";

    /**
     * Erstellt einen neuen JsonHandler
     */
    public JsonHandler(){

    }

    /**
     * Liest Configurations-Attribute aus dem Dateisystem und gibt sie mit ihrem Namen in einer HashMap zurueck
     * @return HashMap mit Attributnamen und -wert
     */
    HashMap<String,String> getConfigMap() throws JsonLoadingException {
        HashMap<String,String> configMap = new HashMap<>();
        JsonObject jsonObject = loadFromJson(RESOURCESPATH + CONFIGNAME);
        for (String s : jsonObject.keySet()){
            configMap.put(s,jsonObject.getString(s));
        }
        return configMap;
    }

    /**
     * Laedt den angegebenen Level-Room aus dem Dateisystem und gibt ihn als Objekt zurueck
     * @param roomName Name des Raums
     * @param roomPreview RoomPreview, von der aus der Raum erzeugt wird
     * @return Room-Objekt inklusive Referenz zur angegebenen RoomPreview
     * @throws JsonValidatingException Wenn das JSON-File nicht valide ist (in einem anderen Zustand als bei der letzten Speicherung)
     */
    public Room getLevelRoom(String roomName, RoomPreview roomPreview) throws JsonValidatingException, JsonLoadingException {
        return loadRoom(Config.get().LEVELROOMPATH() + roomName + ".json", roomPreview);
    }

    /**
     * Laedt den angegebenen Creative-Room aus dem Dateisystem und gibt ihn als Objekt zurueck
     * @param roomName Name des Raums
     * @param roomPreview RoomPreview, von der aus der Raum erzeugt wird
     * @return Room-Objekt inklusive Referenz zur angegebenen RoomPreview
     * @throws JsonValidatingException Wenn das JSON-File nicht valide ist (in einem anderen Zustand als bei der letzten Speicherung)
     */
    public Room getCreativeRoom(String roomName, RoomPreview roomPreview) throws JsonValidatingException, JsonLoadingException {
        return loadRoom(Config.get().CREATIVEROOMPATH() + roomName + ".json", roomPreview);
    }

    /**
     * Gibt ein Room-Objekt nach Angabe des Pfades inklusive Dateinamen zurueck
     * @param fullPath Pfad mit Datenamen und -endung
     * @param roomPreview RoomPreview, von der aus der Raum erzeugt wird
     * @return Wenn das JSON-File nicht valide ist (in einem anderen Zustand als bei der letzten Speicherung)
     * @throws JsonValidatingException
     */
    private Room loadRoom(String fullPath, RoomPreview roomPreview) throws JsonValidatingException, JsonLoadingException {
        JsonObject jsonObject = loadFromJson(fullPath);
        //layout
        JsonArray layoutsArrays = jsonObject.getJsonArray("layout");
        int x = layoutsArrays.size();
        int y = ((JsonArray) layoutsArrays.get(0)).size();
        byte[][] jLayout = new byte[x][y];
        for (int i = 0; i < x; i++) {
            JsonArray outerArray = (JsonArray) layoutsArrays.get(i);
            for (int j = 0; j < y; j++) {
                byte b = Byte.valueOf(String.valueOf(outerArray.get(j)));
                jLayout[i][j] = b;
            }
        }

        //itemList
        JsonArray itemArray = jsonObject.getJsonArray("itemList");
        ArrayList<PlacableItem> placableItemsList = new ArrayList<>();
        for (JsonValue value : itemArray) {
            String str = String.valueOf(value).replace("\"","");
            PlacableItemType type = PlacableItemType.valueOf(str);
            placableItemsList.add(new PlacableItem(type));
        }

        if(jsonObject.getInt("roomHash") != Room.testHash(jLayout, placableItemsList)){
            throw new JsonValidatingException();
        } else {
            return new Room(roomPreview, jLayout, placableItemsList);
        }
    }

    /**
     * Speichert einen Raum persistent ab
     * @param room Raum, der gespeichert werden soll
     * @throws JsonWritingException Bei Problemen während des Abspeicherns
     */
    public void saveRoom(Room room) throws JsonWritingException {
        //layout
        JsonArrayBuilder jLayoutArrBuilder = Json.createArrayBuilder();
        byte[][] layout = room.getLayout();
        for (int i=0; i < layout.length; i++){
            JsonArrayBuilder arrBuilder = Json.createArrayBuilder();
            for (int j = 0; j < layout[i].length; j++) {
                arrBuilder.add(layout[i][j]);
            }
            jLayoutArrBuilder.add(arrBuilder);
        }
        JsonArray jLayout = jLayoutArrBuilder.build();

        //PlacableItems
        JsonArrayBuilder jItemArrBuilder = Json.createArrayBuilder();
        for (PlacableItem item : room.getItemList()){
            jItemArrBuilder.add(item.getType().toString());
        }
        JsonArray jItemList = jItemArrBuilder.build();

        //HighscoreList
        JsonArrayBuilder jHighscoreArrBuilder = Json.createArrayBuilder();
        HighscoreList highscoreList = room.getHighscoreList();
        for (HighscoreRecord record : highscoreList.getList()){
            JsonObject recordObject = Json.createObjectBuilder()
                    .add("time", record.getTime())
                    .add("points", record.getPoints())
                    .add("username", record.getUsername())
                    .build();
            jHighscoreArrBuilder.add(recordObject);
        }
        JsonArray jHighscores = jHighscoreArrBuilder.build();

        //JsonObject erstellen
        JsonObject jsonObject = Json.createObjectBuilder()
                .add("roomHash", room.hashCode())
                .add("previewHash", room.getRoomPreview().hashCode())
                .add("name", room.getName())
                .add("neededScore", room.getNeededScore())
                .add("level", room.isLevel())
                .add("layout", jLayout)
                .add("itemList", jItemList)
                .add("highscoreList", jHighscores)
                .build();

        String filename = room.getName() + ".json";
        if(room.isLevel()){
            saveAsJson(jsonObject, Config.get().LEVELROOMPATH() + filename);
        }else{
            saveAsJson(jsonObject, Config.get().CREATIVEROOMPATH() + filename);
        }
    }

    /**
     * Laedt die angegebenen Level-RoomPreview aus dem Dateisystem und gibt sie als Objekt zurueck
     * @param roomName Name des Raums
     * @return RoomPreview-Objekt
     * @throws JsonValidatingException Wenn das JSON-File nicht valide ist (in einem anderen Zustand als bei der letzten Speicherung)
     */
    public RoomPreview getLevelRoomPreview(String roomName) throws JsonValidatingException, JsonLoadingException {
        return loadRoomPreview(Config.get().LEVELROOMPATH() + roomName + ".json");
    }

    /**
     * Laedt die angegebenen Creative-RoomPreview aus dem Dateisystem und gibt sie als Objekt zurueck
     * @param roomName Name des Raums
     * @return RoomPreview-Objekt
     * @throws JsonValidatingException Wenn das JSON-File nicht valide ist (in einem anderen Zustand als bei der letzten Speicherung)
     */
    public RoomPreview getCreativeRoomPreview(String roomName) throws JsonValidatingException, JsonLoadingException {
        return loadRoomPreview(Config.get().CREATIVEROOMPATH() + roomName + ".json");
    }

    /**
     * Gibt ein Room-Objekt nach Angabe des Pfades inklusive Dateinamen zurueck
     * @param fullPath Pfad mit Datenamen und -endung
     * @return RoomPreview-Objekt
     * @throws JsonValidatingException Wenn das JSON-File nicht valide ist (in einem anderen Zustand als bei der letzten Speicherung)
     */
    private RoomPreview loadRoomPreview(String fullPath) throws JsonValidatingException, JsonLoadingException {
        JsonObject jsonObject = loadFromJson(fullPath);

        //name
        String jName = jsonObject.getString("name");

        //thumbnail
        String jThumbnail = "";//TODO

        //highscoreList
        JsonArray highscoreArray = jsonObject.getJsonArray("highscoreList");
        HighscoreList jHighscoreList = new HighscoreList();
        for (JsonValue value : highscoreArray){
            JsonObject object = (JsonObject) value;
            int time = object.getInt("time");
            int points = object.getInt("points");
            String username = object.getString("username");
            jHighscoreList.addRecord(new HighscoreRecord(time, points, username));
        }

        //neededScore
        int jNeededScore = jsonObject.getInt("neededScore");

        //level
        boolean jLevel = jsonObject.getBoolean("level");

        if(jsonObject.getInt("previewHash") != RoomPreview.testHash(jName, jNeededScore, jLevel, jHighscoreList)){
            throw new JsonValidatingException();
        } else {
            return new RoomPreview(jName, jThumbnail, jHighscoreList, jNeededScore, jLevel, this);
        }
    }

    /**
     * Loescht die JSON-Datei eines Rooms
     * @param room Room-Objekt
     * @throws JsonDeletingException Bei Loesch-Problemen auf Dateiebene
     */
    public void delRoom(Room room) throws JsonDeletingException {
        String path = room.isLevel() ? Config.get().LEVELROOMPATH() : Config.get().CREATIVEROOMPATH();
        try {
            Files.delete(Paths.get(path + room.getName() + ".json"));
        } catch (IOException e) {
            e.printStackTrace();
            throw new JsonDeletingException();
        }
    }

    /**
     * Liest alle Level-Rooms aus dem Dateisystem und gibt sie als HashMap zurueck.
     * Rooms sind ueber ihren Namen abrufbar.
     * @return HashMap mit Room-Namen und RoomPreview-Objekt
     * @throws JsonLoadingException Wenn Fehler auf Dateiebene auftreten
     */
    public HashMap<String, RoomPreview> getRoomMapLevel() throws JsonLoadingException {
        return loadRoomMap(Config.get().LEVELROOMPATH());
    }

    /**
     * Liest alle Creative-Rooms aus dem Dateisystem und gibt sie als HashMap zurueck.
     * Rooms sind ueber ihren Namen abrufbar.
     * @return HashMap mit Room-Namen und RoomPreview-Objekt
     * @throws JsonLoadingException Wenn Fehler auf Dateiebene auftreten
     */
    public HashMap<String, RoomPreview> getRoomMapCreative() throws JsonLoadingException {
        return loadRoomMap(Config.get().CREATIVEROOMPATH());
    }

    /**
     * Liest Rooms im Json-Format ein und gibt sie als HashMap mit Namen und Objekt zurueck
     * @param path Vollstaendiger Pfad zum Dateiverzeichnis
     * @return HashMap mit Room-Namen und RoomPreview-Objekt
     * @throws JsonLoadingException Wenn Fehler auf Dateiebene auftreten
     */
    private HashMap<String, RoomPreview> loadRoomMap(String path) throws JsonLoadingException {
        HashMap<String, RoomPreview> roomMap = new HashMap<>();
        try (Stream<Path> paths = Files.walk(Paths.get(path))) {
            paths.filter(Files::isRegularFile).forEach(u -> {
                        try {
                            RoomPreview roomPreview = loadRoomPreview(path + u.getFileName());
                            roomMap.put(roomPreview.getName(), roomPreview);
                        } catch (JsonValidatingException e) {
                            System.err.println(e.getMessage());
                        } catch (JsonLoadingException e) {
                            System.err.println(e.getMessage());
                        }
                    }
            );
        } catch (IOException e) {
            throw new JsonLoadingException(("Ein Raum kann aufgrund eines Dateifehlers nicht geladen werden"));
        }

        return roomMap;
    }

    //TODO
    public HighscoreList getHighscoreRanked() throws JsonLoadingException {
        HighscoreList highscoreList = new HighscoreList();
        return highscoreList;
    }

    /**
     * Generiert ein User-Objekt aus einem JSON-File
     * @param name Name des Users
     * @return Generiertes User-Objekt
     * @throws JsonValidatingException wenn der Hash-Wert des Files nicht mit den Werten uebereinstimmt.
     * In diesem Fall wurde das File ausserhalb des Programms bearbeitet.
     */
    public User getUser(String name) throws JsonValidatingException, JsonLoadingException {
        JsonObject jsonObject = loadFromJson(Config.get().USERPATH() + name + ".json");
        String jName = jsonObject.getString("name");
        int jReachedLevel = jsonObject.getInt("reachedLevel");

        if(jsonObject.getInt("hash") != User.testHash(jName, jReachedLevel)){
            throw new JsonValidatingException();
        } else {
            return new User(jName, jReachedLevel);
        }
    }

    /**
     * Erstellt aus allen User-Files im entsprechenden Ordner ein User-Objekt
     * @return HashMap mit dem Namen des Users als Key und Objekt als Value
     * @throws JsonLoadingException Wenn es ein Problem auf Dateiebene gibt
     */
    public HashMap<String, User> getUserMap() throws JsonLoadingException {
        HashMap<String, User> userMap = new HashMap<>();
        try (Stream<Path> paths = Files.walk(Paths.get(Config.get().USERPATH()))) {
            paths.filter(Files::isRegularFile).forEach(u -> {
                        try {
                            String name = String.valueOf(u.getFileName()).replace(".json", "");
                            User user = getUser(name);
                            userMap.put(name, user);
                        } catch (JsonValidatingException e) {
                            System.err.println(e.getMessage());
                        } catch (JsonLoadingException e) {
                            System.err.println(e.getMessage());
                        }
                    }
            );
        } catch (IOException e) {
            throw new JsonLoadingException();
        }
        return userMap;
    }

    /**
     * Speichert ein User-Objekt in ein JSON-File
     * @param user User, der gespeichert werden soll
     */
    public void saveUser(User user) throws JsonWritingException {
        JsonObject jsonObject = Json.createObjectBuilder()
                .add("hash", user.hashCode())
                .add("name", user.getName())
                .add("reachedLevel", user.getReachedLevel())
                .build();

        saveAsJson(jsonObject, Config.get().USERPATH() + user.getName() + ".json");
    }

    /**
     * Loescht die JSON-Datei eines Users
     * @param user User-Objekt
     * @throws JsonDeletingException Bei Loesch-Problemen auf Dateiebene
     */
    public void delUser(User user) throws JsonDeletingException {
        try {
            Files.delete(Paths.get(Config.get().USERPATH() + user.getName() + ".json"));
        } catch (IOException e) {
            e.printStackTrace();
            throw new JsonDeletingException();
        }
    }

    /**
     * Laedt ein JSON-File in ein JsonObject
     * @param source Quellpfad des Files inklusive Dateinamen
     * @return aus dem File generiertes JsonObject
     */
    private JsonObject loadFromJson(String source) throws JsonLoadingException {
        FileInputStream in = null;
        try {
            in = new FileInputStream(source);
        } catch (FileNotFoundException e) {
            throw new JsonLoadingException(String.format("Datei \"%p\" konnte nicht geladen werden.\n" + e.getMessage(), source));
        }

        JsonReader jsonReader = Json.createReader(in);
        JsonObject returnObj = jsonReader.readObject();
        jsonReader.close();
        return returnObj;
    }

    /**
     * Speichert ein JsonObjekt im Dateisystem
     * @param jsonObj JsonObjekt, das gespeichert werden soll
     * @param destination Speicherort des Objekts inklusive Dateinamen
     */
    private void saveAsJson(JsonObject jsonObj, String destination) throws JsonWritingException {
        File file = new File(destination);
        FileOutputStream out;
        try {
            out = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            throw new JsonWritingException(String.format("Datei \"%d\" konnte nicht geschrieben werden.\n" + e.getMessage(), destination));
        }

        JsonWriter writer = Json.createWriter(out);
        writer.write(jsonObj);
        writer.close();
    }

}
