package com.github.why168.http;

/**
 * HttpException
 *
 * @author Edwin.Wu
 * @version 2017/6/19 18:11
 * @since JDK1.8
 */
public class HttpException extends Exception {
    public int statusCode;
    public String message;

    public enum ErrorType {CANCEL, TIMEOUT, SERVER, JSON, IO, FILE_NOT_FOUND, UPLOAD, MANUAL}

    public ErrorType type;

    public HttpException(int status, String message) {
        super(message);
        this.type = ErrorType.SERVER;
        this.statusCode = status;
        this.message = message;
    }

    public HttpException(ErrorType type, String message) {
        super(message);
        this.type = type;
    }
}
