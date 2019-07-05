package roomieboomie.business.exception.gameExceptions;

import roomieboomie.business.exception.GameException;

public class InfrontOfDoorException extends GameException {
    public InfrontOfDoorException(){super("Objekt darf nicht vor der TÜr platziert werden");}
    public InfrontOfDoorException(String message){super(message);}
}
