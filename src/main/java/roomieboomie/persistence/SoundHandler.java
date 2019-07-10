package roomieboomie.persistence;

import javafx.scene.media.AudioClip;

import java.nio.file.Paths;

/**
 * Verwaltet abspielbare Sounds. Instanz per .get() aufrufbar. Sounds danach ueber ihren Namen in Caps.
 */
public class SoundHandler {
    private final String path;

    private final AudioClip EATSOUND;
    private final AudioClip FAILSOUND;
    private final AudioClip LOSESOUND;
    private final AudioClip SUCCESSSOUND;
    private final AudioClip WINSOUND;

    private SoundHandler(){
        path = Config.get().SOUNDPATH();
        EATSOUND = new AudioClip(Paths.get(path + Config.get().EATSOUND()).toUri().toString());
        FAILSOUND = new AudioClip(Paths.get(path + Config.get().FAILSOUND()).toUri().toString());
        LOSESOUND = new AudioClip(Paths.get(path + Config.get().LOSESOUND()).toUri().toString());
        SUCCESSSOUND = new AudioClip(Paths.get(path + Config.get().SUCCESSSOUND()).toUri().toString());
        WINSOUND = new AudioClip(Paths.get(path + Config.get().WINSOUND()).toUri().toString());
    }

    private static SoundHandler instance;

    /**
     * @return Instanz von SoundHandler, ueber die anschliessend Sounds abfragbar sind.
     */
    public static SoundHandler get() {
        if (instance == null) {
            instance = new SoundHandler();
        }
        return instance;
    }

    public AudioClip EATSOUND() {
        return EATSOUND;
    }

    public AudioClip FAILSOUND() {
        return FAILSOUND;
    }

    public AudioClip LOSESOUND() {
        return LOSESOUND;
    }

    public AudioClip SUCCESSSOUND() {
        return SUCCESSSOUND;
    }

    public AudioClip WINSOUND() {
        return WINSOUND;
    }
}
