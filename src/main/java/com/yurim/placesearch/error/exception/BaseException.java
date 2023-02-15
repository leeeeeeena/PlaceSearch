package com.yurim.placesearch.error.exception;


import com.yurim.placesearch.error.ErrorResponseCode;

public abstract class BaseException extends RuntimeException {

    private static final long serialVersionUID = 7977074515633412631L;

    private ErrorResponseCode code;

    public BaseException(String message) {
        super(message);
    }

    public BaseException(ErrorResponseCode code) {
        super(code.getReason());
        this.code = code;
    }

    public BaseException(ErrorResponseCode code, String message) {
        super(message);
        this.code = code;
    }

    public BaseException(ErrorResponseCode code, Throwable cause) {
        super(cause);
        this.code = code;
    }

    public BaseException(ErrorResponseCode code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    public ErrorResponseCode getCode() {
        return code;
    }
}