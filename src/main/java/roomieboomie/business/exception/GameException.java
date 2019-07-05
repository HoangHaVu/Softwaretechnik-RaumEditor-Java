package roomieboomie.business.exception;

public class GameException extends ValidationException {
    public GameException(){super ("Fehler beim Spiel");}
    public GameException(String message){super(message);}
}
