package roomieboomie.persistence;

/**
 * Exception, wenn JSON-Datei nicht geschrieben werden kann
 */
public class JsonWritingException extends Exception {
    public JsonWritingException(){
        super("JSON-Datei kann nicht gespeichert werden.");
    }

    public JsonWritingException(String message){
        super(message);
    }
}
