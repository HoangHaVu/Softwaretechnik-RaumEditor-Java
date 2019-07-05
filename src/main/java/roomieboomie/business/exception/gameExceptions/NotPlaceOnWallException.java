package roomieboomie.business.exception.gameExceptions;

import roomieboomie.business.exception.GameException;

public class NotPlaceOnWallException extends GameException {
    public NotPlaceOnWallException(){super("Objekt darf nicht an die Wand gestellt werden");}
    public NotPlaceOnWallException(String message){super(message);}
}
