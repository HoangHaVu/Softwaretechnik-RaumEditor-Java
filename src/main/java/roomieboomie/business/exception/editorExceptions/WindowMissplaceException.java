package roomieboomie.business.exception.editorExceptions;

import roomieboomie.business.exception.EditorException;

public class WindowMissplaceException extends EditorException {
    public WindowMissplaceException(){super("Fenster falsch platziert");}
    public WindowMissplaceException(String message) {super(message);}
}
