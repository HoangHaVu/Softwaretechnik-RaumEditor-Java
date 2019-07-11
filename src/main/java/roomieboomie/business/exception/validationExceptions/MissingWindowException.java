package roomieboomie.business.exception.validationExceptions;

import roomieboomie.business.exception.EditorException;

public class MissingWindowException extends EditorException {
    public MissingWindowException(){super("Fenster wurde noch nicht gesetzt");}
    public MissingWindowException(String message){super(message);}
}
