
package com.yurim.placesearch.error.exception;

import com.yurim.placesearch.error.ErrorResponseCode;

public class ExternalRequestException extends BaseException {

    private static final long serialVersionUID = 81963875134266622L;

    public ExternalRequestException(String message) {
        super(message);
    }

    public ExternalRequestException(ErrorResponseCode code) {
        super(code);
    }

    public ExternalRequestException(ErrorResponseCode code, String message) {
        super(code, message);
    }

    public ExternalRequestException(ErrorResponseCode code, Throwable cause) {
        super(code, cause);
    }

    public ExternalRequestException(ErrorResponseCode code, String message, Throwable cause) {
        super(code, message, cause);
    }
}
