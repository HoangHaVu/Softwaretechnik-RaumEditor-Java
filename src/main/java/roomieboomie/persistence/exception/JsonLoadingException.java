package roomieboomie.persistence.exception;

/**
 * Exception, wenn JSON-Datei nicht geladen werden kann
 */
public class JsonLoadingException extends Exception {
    public JsonLoadingException(){
        super("JSON-Datei kann nicht geladen werden.");
    }

    public JsonLoadingException(String message){
        super(message);
    }
}
