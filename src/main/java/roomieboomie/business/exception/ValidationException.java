package roomieboomie.business.exception;

public class ValidationException extends Exception{
    public ValidationException (){super("ValidierungsProblem");}
    public ValidationException(String message){super(message);}
}
