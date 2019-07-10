package roomieboomie.business.exception.gameExceptions;

import roomieboomie.business.exception.GameException;

public class TooHighForPosition extends GameException {
    public TooHighForPosition() {
        super("Gegenstand ist zu hoch");
    }

    public TooHighForPosition(String message) {
        super(message);
    }
}
