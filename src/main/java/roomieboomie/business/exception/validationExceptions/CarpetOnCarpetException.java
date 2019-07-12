package roomieboomie.business.exception.validationExceptions;

public class CarpetOnCarpetException extends Exception {
    public CarpetOnCarpetException(){super("Es dürfen keine Teppiche aufeinander platziert werden");}
    public CarpetOnCarpetException(String message){super(message);}
}
