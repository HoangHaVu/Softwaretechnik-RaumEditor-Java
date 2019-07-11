package roomieboomie.business.exception.validationExceptions;

public class ItemIsTooCloseToDoorException extends Exception {
    public ItemIsTooCloseToDoorException(){super("Objekt ist zu nah an der Tür platziert");}
    public ItemIsTooCloseToDoorException(String message){super(message);}
}
