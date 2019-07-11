package roomieboomie.business.exception.validationExceptions;

import roomieboomie.business.exception.EditorException;

public class LayoutItemMissplaceException extends EditorException {
    public LayoutItemMissplaceException(){super("LayoutItem darf nicht an den Rand gesetzt werden");}
    public LayoutItemMissplaceException(String message){super(message);}
}
