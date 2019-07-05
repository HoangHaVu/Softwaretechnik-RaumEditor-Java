package roomieboomie.business.exception.gameExceptions;

import roomieboomie.business.exception.GameException;

public class TooWidthException extends GameException {
    public TooWidthException(){super("Objekt ist zu weit");}
    public TooWidthException(String message){super(message);}
}
