package roomieboomie.persistence;

import roomieboomie.business.room.Room;
import roomieboomie.business.room.RoomPreview;
import roomieboomie.business.user.User;


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
    private String usersPath;

    public JsonHandler(){
        //configMap erstellen
        JsonObject jsonObject = loadFromJson(RESOURCESPATH + CONFIGNAME);
        for (String s : jsonObject.keySet()){
            configMap.put(s,jsonObject.getString(s));
        }
        usersPath = getConfigAttribute("usersPath");
        //
    }

    public Room getRoom(String name){//TODO
        return null;
    }

    public void saveRoom(Room room){}//TODO

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
        JsonObject jsonObject = loadFromJson(usersPath + name + ".json");
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
        try (Stream<Path> paths = Files.walk(Paths.get(configMap.get("usersPath")))) {
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
   public HashMap<String,RoomPreview>getRoomMapLevel()throws JsonLoadingException{
        HashMap<String,RoomPreview> levelRoomMap = new HashMap<>();
        return levelRoomMap;
   }
    public HashMap<String,RoomPreview>getRoomMapCreative()throws JsonLoadingException{
        HashMap<String,RoomPreview> creativeRoomMap = new HashMap<>();
        return creativeRoomMap;
    }

    /**
     * Speichert ein User-Objekt in ein JSON-File
     * @param user User, der gespeichert werden soll
     */
    public void saveUser(User user) throws JsonWritingException {
        JsonObjectBuilder builder = Json.createObjectBuilder()
                .add("hash",user.hashCode())
                .add("name", user.getName())
                .add("reachedLevel",user.getReachedLevel());

        JsonObject jsonObject = builder.build();
        saveAsJson(jsonObject, usersPath + user.getName() + ".json");
    }

    public void delUser(User user) throws JsonDeletingException {
        try {
            Files.delete(Paths.get(usersPath + user.getName() + ".json"));
        } catch (IOException e) {
            e.printStackTrace();
            throw new JsonDeletingException();
        }
    }

    public RoomPreview loadLevelRoomPreview(){//TODO
        return null;
    }//TODO

    public RoomPreview loadCreativeRoomPreview(){//TODO
        return null;
    }//TODO

    public String getConfigAttribute(String attributeName){
        //TODO extra Exception?
        return configMap.get(attributeName);
    }

    /**
     * Laedt ein JSON-File in ein JsonObject
     * @param source Quellpfad des Files inklusive Dateinamen
     * @return aus dem File generiertes JsonObject
     */
    private JsonObject loadFromJson(String source){
        FileInputStream in = null;
        try {
            in = new FileInputStream(source);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        JsonReader jsonReader = Json.createReader(in);
        JsonObject jsonObject = jsonReader.readObject();
        return jsonObject;
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
            throw new JsonWritingException();
        }

        JsonWriter writer = Json.createWriter(out);
        writer.write(jsonObj);
        writer.close();
    }
}
