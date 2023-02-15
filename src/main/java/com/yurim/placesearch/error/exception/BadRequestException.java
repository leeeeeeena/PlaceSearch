
package com.yurim.placesearch.error.exception;

import com.yurim.placesearch.error.ErrorResponseCode;

public class BadRequestException extends BaseException {

    private static final long serialVersionUID = 81963875134266622L;

    public BadRequestException(String message) {
        super(message);
    }

    public BadRequestException(ErrorResponseCode code) {
        super(code);
    }

    public BadRequestException(ErrorResponseCode code, String message) {
        super(code, message);
    }

    public BadRequestException(ErrorResponseCode code, Throwable cause) {
        super(code, cause);
    }

    public BadRequestException(ErrorResponseCode code, String message, Throwable cause) {
        super(code, message, cause);
    }
}
