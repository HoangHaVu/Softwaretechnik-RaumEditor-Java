package roomieboomie.business.exception.validationExceptions;

import roomieboomie.business.exception.EditorException;

public class MissingWallException extends EditorException {
    public MissingWallException (){super("Wand fehlt");}
    public MissingWallException(String message){super(message);}
}
