package fr.hm.hapiour.util.exception;

import java.time.LocalDateTime;

/**
 * dto d'information concernant une erreur rencontrée lors d'un appel d'api
 */
public class ApiError {

    public static final String INTERNAL_ERROR = "internalError";
    public static final String BAD_PARAM = "badParameter";
    public static final String TIMEOUT = "timeout";
    public static final String NOT_IMPLEMENTED = "notImpl";
    public static final String MAIL_ERROR = "mailError";

    private int status;
    private LocalDateTime timestamp;
    private String error;
    private String message;
    private String path;

    /**
     * constructeur
     * @param statusHttp statut http de l'erreur
     * @param error      code d'erreur
     * @param message    message
     * @param path       chemin
     */
    public ApiError(int statusHttp, String error, String message, String path) {
        this.timestamp = LocalDateTime.now();
        this.error = error;
        this.message = message;
        this.status = statusHttp;
        this.path = path;
    }

    /**
     * status getter
     * @return status
     */
    public int getStatus() {
        return status;
    }


    /**
     * error getter
     * @return error
     */
    public String getError() {
        return error;
    }

    /**
     * message getter
     * @return message
     */
    public String getMessage() {
        return message;
    }

    /**
     * path getter
     * @return path
     */
    public String getPath() {
        return path;
    }

    /**
     * timestamp getter
     * @return timestamp
     */
    public String getTimestamp() {
        return timestamp.toString();
    }
}
