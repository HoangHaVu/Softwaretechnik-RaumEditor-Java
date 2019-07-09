package user;

import org.junit.Before;
import org.junit.Test;
import roomieboomie.business.user.User;

public class UserTest {
    User joendhardt;

    @Before
    public void init(){
        joendhardt = new User("Joendhardt",1);
    }

    @Test
    /**
     * Testet, ob nach levelUp() das erreichte Level korrekt erhoeht wird
     */
    public void levelUp(){
        int oldLevel = joendhardt.getReachedLevel();
        joendhardt.levelUp();
        int newLevel = joendhardt.getReachedLevel();
        assert (newLevel == (++oldLevel));
    }
}
