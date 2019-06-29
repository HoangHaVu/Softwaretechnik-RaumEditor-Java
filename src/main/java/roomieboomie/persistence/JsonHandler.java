package roomieboomie.persistence;

import roomieboomie.business.highscore.HighscoreList;
import roomieboomie.business.highscore.HighscoreRecord;
import roomieboomie.business.item.placable.PlacableItem;
import roomieboomie.business.room.Room;
import roomieboomie.business.room.RoomPreview;
import roomieboomie.business.user.User;
import roomieboomie.business.user.UserMap;

import javax.json.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.stream.Stream;

public class JsonHandler {
    private HashMap<String,String> configMap = new HashMap<>();
    private final String RESOURCESPATH = "src/main/resources/";
    private final String CONFIGNAME = "config.json";

    public JsonHandler(){
        //configMap erstellen
        JsonObject jsonObject = loadFromJson(RESOURCESPATH + CONFIGNAME);
        for (String s : jsonObject.keySet()){
            configMap.put(s,jsonObject.getString(s));
        }
    }

    //TODO
    public Room getRoom(String name){
        //layout

        //JsonObject jsonObject = loadFromJson(fullPath); //FIXME
        /* layoutsArrays = jsonObject.getJsonArray("layout");
        int x = layoutsArrays.size();
        int y = ((JsonArray) layoutsArrays.get(0)).size();
        byte[][] jLayout = new byte[x][y];
        for (int i = 0; i < x; i++) {
            JsonArray outerArray = (JsonArray) layoutsArrays.get(i);
            for (int j = 0; j < y; j++) {
                byte b = Byte.valueOf(String.valueOf(outerArray.get(j)));
                jLayout[i][j] = b;
            }
        }*/

        //itemList
        // wenn oben gefixt
        /*JsonArray itemArray = jsonObject.getJsonArray("itemList");
        ArrayList<PlacableItem> placableItemsList = new ArrayList<>();
        for (JsonValue value : itemArray){
            PlacableItemType type = PlacableItemType.valueOf( String.valueOf(value) );
            placableItemsList.add(new PlacableItem(type));
        }*/
        return null;
    }

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
                    .add("username", record.getUser().getName())
                    .build();
            jHighscoreArrBuilder.add(recordObject);
        }
        JsonArray jHiscores = jHighscoreArrBuilder.build();

        //JsonObject erstellen
        JsonObject jsonObject = Json.createObjectBuilder()
                .add("hash", room.hashCode())
                .add("name", room.getName())
                .add("neededScore", room.getNeededScore())
                .add("level", room.isLevel())
                .add("layout", jLayout)
                .add("itemList", jItemList)
                .add("highscoreList", jHiscores)
                .build();

        String filename = room.getName() + ".json";
        if(room.isLevel()){
            saveAsJson(jsonObject,configMap.get("levelRoomPath") + filename);
        }else{
            saveAsJson(jsonObject,configMap.get("creativeRoomPath") + filename);
        }
    }

    public RoomPreview getLevelRoomPreview(String roomName, UserMap userMap) throws JsonValidatingException{
        return loadRoomPreview(configMap.get("levelRoomPath") + roomName + ".json", userMap);
    }

    public RoomPreview getCreativeRoomPreview(String roomName, UserMap userMap) throws JsonValidatingException{
        return loadRoomPreview(configMap.get("creativeRoomPath") + roomName + ".json", userMap);
    }

    private RoomPreview loadRoomPreview(String fullPath, UserMap userMap) throws JsonValidatingException {
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
            User user = userMap.getUser(object.getString("username"));
            jHighscoreList.addRecord(new HighscoreRecord(time, points, user));
        }
        //neededScore
        int jNeededScore = jsonObject.getInt("neededScore");
        //level
        boolean jLevel = jsonObject.getBoolean("level");

        //FIXME test
        if(jsonObject.getInt("hash") != RoomPreview.testHash(jName, jThumbnail, jNeededScore, jLevel, jHighscoreList)){
            throw new JsonValidatingException();
        } else {
            RoomPreview roomPreview = new RoomPreview(jName, jThumbnail, jHighscoreList, jNeededScore, jLevel);
            return roomPreview;
        }
    }

    public void delRoom(){//TODO
    }

    /**
     * Generiert ein User-Objekt aus einem JSON-File
     * @param name Name des Users
     * @return Generiertes User-Objekt
     * @throws JsonValidatingException wenn der Hash-Wert des Files nicht mit den Werten uebereinstimmt.
     * In diesem Fall wurde das File ausserhalb des Programms bearbeitet.
     */
    public User getUser(String name) throws JsonValidatingException{//TODO exceptions
        JsonObject jsonObject = loadFromJson(configMap.get("userPath") + name + ".json");
        String jName = jsonObject.getString("name");
        int jReachedLevel = jsonObject.getInt("reachedLevel");

        if(jsonObject.getInt("hash") != User.testHash(jName, jReachedLevel)){
            throw new JsonValidatingException();
        } else {
            User user = new User(jName, jReachedLevel);
            return user;
        }
    }

    /**
     * Erstellt aus allen User-Files im entsprechenden Ordner ein User-Objekt
     * @return HashMap mit dem Namen des Users als Key und Objekt als Value
     * @throws JsonLoadingException Wenn es ein Problem auf Dateiebene gibt
     */
    public HashMap<String, User> getUserMap() throws JsonLoadingException {
        HashMap<String, User> userMap = new HashMap<>();
        try (Stream<Path> paths = Files.walk(Paths.get(configMap.get("userPath")))) {
            paths.filter(Files::isRegularFile).forEach(u -> {
                        try {
                            User user = getUser(String.valueOf(u.getFileName()).replace(".json",""));
                            userMap.put(user.getName(), user);
                        } catch (JsonValidatingException e) {
                            //TODO hier kann wegen Lambda nicht gethrowt werden;
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

        saveAsJson(jsonObject, configMap.get("userPath") + user.getName() + ".json");
    }

    public void delUser(User user) throws JsonDeletingException {
        try {
            Files.delete(Paths.get(configMap.get("userPath") + user.getName() + ".json"));
        } catch (IOException e) {
            e.printStackTrace();
            throw new JsonDeletingException();
        }
    }

    public String getConfigAttribute(String attributeName){
        try {
            return configMap.get(attributeName);
        } catch (NullPointerException e){
            e.printStackTrace();
            System.err.println(String.format("Config-Attribut \"%a\" konnte nicht gefunden werden.", attributeName));
        }
        return "ATTRIBUTE_NOT_FOUND";
    }

    /**
     * Laedt ein JSON-File in ein JsonObject
     * @param source Quellpfad des Files inklusive Dateinamen
     * @return aus dem File generiertes JsonObject
     */
    private static JsonObject loadFromJson(String source){
        FileInputStream in = null;
        try {
            in = new FileInputStream(source);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        JsonReader jsonReader = Json.createReader(in);
        return jsonReader.readObject();
    }

    /**
     * Speichert ein JsonObjekt im Dateisystem
     * @param jsonObj JsonObjekt, das gespeichert werden soll
     * @param destination Speicherort des Objekts inklusive Dateinamen
     */
    private void saveAsJson(JsonObject jsonObj, String destination) throws JsonWritingException {
        File file = new File(destination);
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            throw new JsonWritingException(e.getMessage());
        }

        JsonWriter writer = Json.createWriter(out);
        writer.write(jsonObj);
        writer.close();
    }
}
