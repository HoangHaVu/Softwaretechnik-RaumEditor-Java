package roomieboomie.business.exception.validationExceptions;

import roomieboomie.business.exception.EditorException;

public class getIntoRoomException extends EditorException {
    public getIntoRoomException(){super("Tür muss von außen zugänglich sein");}
    public getIntoRoomException(String message){super(message);}
}
