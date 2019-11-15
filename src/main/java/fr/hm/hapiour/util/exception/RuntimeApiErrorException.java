package fr.hm.hapiour.util.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

/**
 * Classe de base pour les runtime exceptions qui peuvent fournir un objet ApiError
 */
public class RuntimeApiErrorException extends RuntimeException {

    private static final String DEFAULT_ERROR = ApiError.INTERNAL_ERROR;
    private static final int DEFAULT_HTTP_STATUS = HttpStatus.INTERNAL_SERVER_ERROR.value();

    private final int status;
    private final String error;

    /**
     * constructeur avec juste un status
     * @param httpStatus status http
     */
    public RuntimeApiErrorException(int httpStatus) {
        this(httpStatus, DEFAULT_ERROR);
    }

    /**
     * constructeur avec juste un status et un code d'errueur
     * @param httpStatus status http
     * @param error      code d'erreur
     */
    public RuntimeApiErrorException(int httpStatus, String error) {
        this.status = httpStatus;
        this.error = error;
    }

    /**
     * constructeur par defaut
     */
    public RuntimeApiErrorException() {
        this(DEFAULT_HTTP_STATUS, DEFAULT_ERROR);
    }

    /**
     * constructeur avec juste un message
     * @param message message de l'erreur
     */
    public RuntimeApiErrorException(String message) {
        this(DEFAULT_HTTP_STATUS, DEFAULT_ERROR, message);
    }

    /**
     * constructeur avec message et cause
     * @param message message de l'erreur
     * @param cause   cause de l'exception
     */
    public RuntimeApiErrorException(String message, Throwable cause) {
        this((cause instanceof RuntimeApiErrorException ? ((RuntimeApiErrorException) cause).status : DEFAULT_HTTP_STATUS),
                (cause instanceof RuntimeApiErrorException ? ((RuntimeApiErrorException) cause).error : DEFAULT_ERROR),
                message, cause);
    }

    /**
     * constructeur avec message, cause et statut http
     * @param message    message de l'erreur
     * @param cause      cause de l'exception
     * @param httpStatus status http
     */
    public RuntimeApiErrorException(String message, Throwable cause, int httpStatus) {
        this(httpStatus, DEFAULT_ERROR, message, cause);
    }

    /**
     * constructeur avec statut, code erreur et message
     * @param httpStatus status http
     * @param error      code d'erreur
     * @param message    message de l'erreur
     */
    public RuntimeApiErrorException(int httpStatus, String error, String message) {
        super(message);
        this.status = httpStatus;
        this.error = error;
    }

    /**
     * constructeur avec statut, code erreur, message et cause
     * @param httpStatus status http
     * @param error      code d'erreur
     * @param message    message de l'erreur
     * @param cause      cause de l'erreur
     */
    public RuntimeApiErrorException(int httpStatus, String error, String message, Throwable cause) {
        super(message, cause);
        this.status = httpStatus;
        this.error = error;
    }

    /**
     * constructeur avec statut, code erreur et cause
     * @param httpStatus status http
     * @param error      code d'erreur
     * @param cause      cause de l'erreur
     */
    public RuntimeApiErrorException(int httpStatus, String error, Throwable cause) {
        this(httpStatus, error,
                (cause instanceof RuntimeApiErrorException ? cause.getMessage() : cause.toString()),
                cause);
    }

    /**
     * constructeur avec statut et cause
     * @param httpStatus status http
     * @param cause      cause de l'erreur
     */
    public RuntimeApiErrorException(int httpStatus, Throwable cause) {
        this(httpStatus,
                (cause instanceof RuntimeApiErrorException ? ((RuntimeApiErrorException) cause).error : DEFAULT_ERROR),
                (cause instanceof RuntimeApiErrorException ? cause.getMessage() : cause.toString()),
                cause);
    }

    /**
     * constructeur avec cause
     * @param cause cause de l'erreur
     */
    public RuntimeApiErrorException(Throwable cause) {
        this((cause instanceof RuntimeApiErrorException ? ((RuntimeApiErrorException) cause).status : DEFAULT_HTTP_STATUS),
                (cause instanceof RuntimeApiErrorException ? ((RuntimeApiErrorException) cause).error : DEFAULT_ERROR),
                (cause instanceof RuntimeApiErrorException ? cause.getMessage() : cause.toString()),
                cause);
    }

    /**
     * extrait le path d'une request
     * @param request request en cours
     * @return path de la requete
     */
    private String extractPath(WebRequest request) {
        if (request instanceof ServletWebRequest) {
            ServletWebRequest servletWebRequest = (ServletWebRequest) request;
            return servletWebRequest.getRequest().getContextPath() + servletWebRequest.getRequest().getServletPath();
        }
        return request.getDescription(false);
    }

    /**
     * construit un objet ApiError correspondant à l'exception
     * @param request requete http
     * @return objet ApiError construit
     */
    public ApiError buildApiError(WebRequest request) {
        return buildApiError(extractPath(request));
    }

    /**
     * construit un objet ApiError avec un path
     * @param path chemin de la requete http
     * @return objet ApiError construit
     */
    public ApiError buildApiError(String path) {
        return new ApiError(this.status, this.error, this.getMessage(), path);
    }

    /**
     * Code error
     * @return le code erreur
     */
    public String getError() {
        return error;
    }
}
