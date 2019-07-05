package roomieboomie.business.exception;

public class EditorException extends ValidationException {
    public EditorException(){super("Fehler im Editor");}
    public EditorException(String message){super(message);}
}
