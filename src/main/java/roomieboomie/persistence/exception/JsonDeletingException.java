package roomieboomie.persistence.exception;

/**
 * Exception, wenn JSON-Datei nicht geloescht werden kann
 */
public class JsonDeletingException extends Exception {
    public JsonDeletingException(){
        super("JSON-Datei kann nicht geloescht werden.");
    }

    public JsonDeletingException(String message){
        super(message);
    }
}
