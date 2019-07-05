package roomieboomie.business.exception.editorExceptions;

import roomieboomie.business.exception.EditorException;

public class DoorMissplaceException extends EditorException {
    public DoorMissplaceException(){super("Tür wurde falsch gesetzt");}
    public DoorMissplaceException(String message){super(message);}
}
