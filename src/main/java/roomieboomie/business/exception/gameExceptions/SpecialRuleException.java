package roomieboomie.business.exception.gameExceptions;

import roomieboomie.business.exception.GameException;

public class SpecialRuleException extends GameException {
    public SpecialRuleException(){super("Spezielle Regel passt nicht");}
    public SpecialRuleException(String message){super(message);}
}
