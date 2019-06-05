package it.uniroma2.dicii.sdcc.didatticamobile.error;

public class UnexpectedServerResponseException extends Exception {

    public UnexpectedServerResponseException() {}

    public UnexpectedServerResponseException(String message, Throwable cause) {
        super(message, cause);
    }
}
