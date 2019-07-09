package persitence;

import org.junit.Before;
import org.junit.Test;
import roomieboomie.business.user.User;
import roomieboomie.business.user.UserMap;
import roomieboomie.persistence.JsonHandler;
import roomieboomie.persistence.exception.JsonLoadingException;
import roomieboomie.persistence.exception.JsonValidatingException;
import roomieboomie.persistence.exception.JsonWritingException;

public class UserSaveLoadTest {
    UserMap userMap;
    JsonHandler jsonHandler;
    int saveHash;
    final int reachedLevel = 3;
    final String name = "Joendhardt";
    User user;

    @Before
    public void init(){
        jsonHandler = new JsonHandler();
        user = new User(name, reachedLevel);
        saveHash = user.hashCode();
    }

    @Test
    /**
     * Testet, ob ein User problemlos gespeichert werden kann
     */
    public void saveUser(){
        boolean success = true;

        try {
            jsonHandler.saveUser(user);
        } catch (JsonWritingException e) {
            e.printStackTrace();
            success = false;
        }
        assert success;
    }

    @Test
    /**
     * Testet, ob ein User korrekt geladen wird und die Attribute mit dem zuvor gespeicherten uebereinstimmen
     */
    public void loadUser(){
        boolean success = true;
        User loadedUser = null;

        try {
            loadedUser = jsonHandler.getUser(name);
        } catch (JsonValidatingException e) {
            success = false;
        } catch (JsonLoadingException e) {
            success = false;
        }

        assert (reachedLevel == loadedUser.getReachedLevel());
        assert (name.equals(loadedUser.getName()));
        assert (saveHash == loadedUser.hashCode());
        assert success;
    }

}
