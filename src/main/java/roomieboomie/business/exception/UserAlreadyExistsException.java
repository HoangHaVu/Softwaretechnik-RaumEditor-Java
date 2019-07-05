package roomieboomie.business.exception;

public class UserAlreadyExistsException extends Exception {
    public UserAlreadyExistsException(){super("Username wurde nicht gefunden");}
    public UserAlreadyExistsException(String message){super(message);}
}
