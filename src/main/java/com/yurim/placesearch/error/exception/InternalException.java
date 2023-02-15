package com.yurim.placesearch.error.exception;

import com.yurim.placesearch.error.ErrorResponseCode;

public class InternalException extends BaseException {

    private static final long serialVersionUID = 81963875134266622L;

    public InternalException(String message) {
        super(message);
    }

    public InternalException(ErrorResponseCode code) {
        super(code);
    }

    public InternalException(ErrorResponseCode code, String message) {
        super(code, message);
    }

    public InternalException(ErrorResponseCode code, Throwable cause) {
        super(code, cause);
    }

    public InternalException(ErrorResponseCode code, String message, Throwable cause) {
        super(code, message, cause);
    }
}

