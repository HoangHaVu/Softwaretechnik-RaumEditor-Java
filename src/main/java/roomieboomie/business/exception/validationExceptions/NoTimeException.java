package roomieboomie.business.exception.validationExceptions;

import roomieboomie.business.exception.GameException;

public class NoTimeException extends GameException {
    public NoTimeException(){super("Zeit ist abgelaufen");}
    public NoTimeException(String message){super(message);}
}
