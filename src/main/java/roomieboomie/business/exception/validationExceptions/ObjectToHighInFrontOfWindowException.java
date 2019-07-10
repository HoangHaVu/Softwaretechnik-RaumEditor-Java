package roomieboomie.business.exception.validationExceptions;

public class ObjectToHighInFrontOfWindowException extends Exception {
    public ObjectToHighInFrontOfWindowException(){ super("Objekt ist zu hoch um vor das Fenster zu stellen");}
    public ObjectToHighInFrontOfWindowException(String message){super(message);}
}
