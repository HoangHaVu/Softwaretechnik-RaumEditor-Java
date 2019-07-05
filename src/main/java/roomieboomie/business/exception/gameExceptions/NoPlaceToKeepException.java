package roomieboomie.business.exception.gameExceptions;

import roomieboomie.business.exception.GameException;

public class NoPlaceToKeepException  extends GameException {
    public NoPlaceToKeepException(){super("Objekt hat nicht genug Platz um da platziert zu werden");}
    public NoPlaceToKeepException(String message){super(message);}
}
