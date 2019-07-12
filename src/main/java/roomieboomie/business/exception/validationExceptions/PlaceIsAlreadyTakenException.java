package roomieboomie.business.exception.validationExceptions;

public class PlaceIsAlreadyTakenException extends Exception {
    public PlaceIsAlreadyTakenException(){super(" Objekt kann nicht gesetzt werden da der der Platz schon für ein anderes Objekt reserviert ist");}
    public PlaceIsAlreadyTakenException(String message){super(message);}
}
