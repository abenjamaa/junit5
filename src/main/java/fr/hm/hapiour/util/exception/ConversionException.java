package fr.hm.hapiour.util.exception;

/**
 * Exception lié à un problème de conversion de type.
 * Ex : Format de date, ...
 * Created by mortier-j on 31/05/2017.
 */
public class ConversionException extends RuntimeException {

    /**
     * Constructeur avec message
     * @param message Le message de l'exception
     */
    public ConversionException(String message) {
        super(message);
    }

    /**
     * Constructeur avec messag et exception
     * @param message Le message de l'exception
     * @param cause   L'exception qui
     */
    public ConversionException(String message, Throwable cause) {
        super(message, cause);
    }
}
