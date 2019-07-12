package roomieboomie.business.exception.validationExceptions;

public class ObjectIsNotStorableException extends Exception {
    public ObjectIsNotStorableException(){super("Objekt kann nur auf Ablage oder Boden platziert werden");}
    public ObjectIsNotStorableException(String message){super(message);}

}
