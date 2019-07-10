package singleFunctions;

import javafx.scene.media.AudioClip;
import org.junit.Before;
import org.junit.Test;

import java.nio.file.Paths;

public class soundTest {

    AudioClip ac;

    @Before
    public void init() {
        ac = new AudioClip(Paths.get("src/main/resources/sounds/winRoom.wav").toUri().toString());
    }

    @Test
    public void play(){
        System.out.println("hallo");
        ac = new AudioClip(Paths.get("src/main/resources/sounds/winRoom.wav").toUri().toString());
        ac.play();
    }

}