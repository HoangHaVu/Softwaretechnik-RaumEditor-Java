package roomieboomie.persistence.exception;

/**
 * Exception, wenn JSON-Datei nicht valide ist (Der Hashwert nicht den Daten entspricht)
 */
public class JsonValidatingException extends Exception {
    public JsonValidatingException(){
        super("JSON-Datei ist nicht valide.");
    }

    public JsonValidatingException(String message){
        super(message);
    }
}
