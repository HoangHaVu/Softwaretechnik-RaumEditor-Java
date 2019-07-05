package roomieboomie.business.exception.editorExceptions;

import roomieboomie.business.exception.EditorException;

public class MissingWindowException extends EditorException {
    public MissingWindowException(){super("Fenster wurde noch nicht gesetzt");}
    public MissingWindowException(String message){super(message);}
}
