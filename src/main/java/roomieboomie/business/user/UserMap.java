package roomieboomie.business.user;

import java.util.HashMap;

/**
 * Verwaltet alle User. User-Objekte sind ueber ihren Namen abrufbar
 */
public class UserMap{
    private HashMap<String, User> userMap = new HashMap<>();

    /**
     * Neue UserMap
     * @param userMap HashMap mit allen Usern, die verwaltet werden sollen
     */
    public UserMap(HashMap<String, User> userMap) {
        this.userMap = userMap;
    }

    /**
     * @return HashMap mit allen Usern
     */
    public HashMap<String, User> getUserMap() { //MAYBE Klasse Iterable machen
        return userMap;
    }

    /**
     * Gibt einen User anhand seines Namens zurueck
     * @param name Name des Users
     * @return Userobjekt
     */
    public User getUser(String name){
        return userMap.get(name);
    }

    /**
     * Entfernt einen User aus der UserMap
     * @param name Name des Users
     */
    public void delUser(String name){
        userMap.remove(name);
    }

    /**
     * Fuegt einen User hinzu
     * @param user User-Objekt
     * @throws UserException wenn der Username bereits existiert
     */
    public void addUser(User user) throws UserException{
        if (userMap.containsKey(user.getName())){
            throw new UserException();
        } else{
            userMap.put(user.getName(), user);
        }
    }

}
