package roomieboomie.business.user;

public class UserException extends Exception {
    public UserException(){
        super("User kann nicht erstellt werden.");
    }

    public UserException(String message){
        super(message);
    }
}
