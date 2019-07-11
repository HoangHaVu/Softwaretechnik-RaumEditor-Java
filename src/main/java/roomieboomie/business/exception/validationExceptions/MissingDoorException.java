package roomieboomie.business.exception.validationExceptions;

import roomieboomie.business.exception.EditorException;

public class MissingDoorException extends EditorException {
    public MissingDoorException (){super("Tür wurde noch nicht gesetzt");}
    public MissingDoorException(String message){super(message);}
}
