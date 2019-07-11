package roomieboomie.business.exception.validationExceptions;

public class PlaceItemIsNotInInteriorException extends Exception {
    public PlaceItemIsNotInInteriorException(){super("Objekt muss in dem Raum platziert werden");}
    public PlaceItemIsNotInInteriorException(String message){super(message);}
}
