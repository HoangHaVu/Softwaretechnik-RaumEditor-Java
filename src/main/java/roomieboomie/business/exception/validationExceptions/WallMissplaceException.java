package roomieboomie.business.exception.validationExceptions;

import roomieboomie.business.exception.EditorException;

public class WallMissplaceException extends EditorException {
    public WallMissplaceException(){super("Wand wurde falsch platziert");}
    public WallMissplaceException(String message){super(message);}
}
