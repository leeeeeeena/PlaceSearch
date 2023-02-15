package com.yurim.placesearch.error.handler;


import com.yurim.placesearch.common.Constants;
import com.yurim.placesearch.error.ErrorResponse;
import com.yurim.placesearch.error.ErrorResponseCode;
import com.yurim.placesearch.error.exception.BaseException;
import io.lettuce.core.RedisBusyException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.*;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.data.redis.connection.RedisPipelineException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.HttpMediaTypeException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.util.WebUtils;

import javax.servlet.ServletException;

@Slf4j
@RestControllerAdvice
public class GloabalExceptionHandler extends ResponseEntityExceptionHandler {


    /**
     * Spring MVC 관련 Exception 공용 처리 (Parent의 실제 MVC Exception 처리 Method override)
     *
     * @see ResponseEntityExceptionHandler#handleException
     */
    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, @Nullable Object body, HttpHeaders headers,
                                                             HttpStatus status, WebRequest request) {
        log.error("Spring MVC Exception Occurred", ex);

        request.setAttribute(WebUtils.ERROR_EXCEPTION_ATTRIBUTE, ex, WebRequest.SCOPE_REQUEST);
        ErrorResponseCode responseCode = ErrorResponseCode.REQUEST_UNDEFINED_ERROR;
        addErrorHttpHeader(headers, request, responseCode);
        return new ResponseEntity<>(errorResponse(responseCode, status.getReasonPhrase()), headers, status);
    }


    /**
     * 비즈니스 로직 구현에서 발생할 수 있는 runtime exception
     */
    @ExceptionHandler(BaseException.class)
    @Nullable
    public final ResponseEntity<Object> handleBizException(BaseException ex, WebRequest request) throws Exception {
        request.setAttribute(WebUtils.ERROR_EXCEPTION_ATTRIBUTE, ex, WebRequest.SCOPE_REQUEST);
        ErrorResponseCode responseCode = ex.getCode();
        HttpHeaders headers = new HttpHeaders();
        addErrorHttpHeader(headers, request, responseCode);
        log.error("Exception Occurred", ex);

        return new ResponseEntity<>(errorResponse(responseCode, ex.getMessage()), headers, responseCode.getHttpStatus());
    }


    /**
     * 외부 연동 API 호출 시 발생할 수 있는 exception
     */
    @ExceptionHandler(HttpClientErrorException.class)
    public final ResponseEntity<Object> handleHttpClientErrorException(HttpClientErrorException ex, WebRequest request) throws Exception {
        request.setAttribute(WebUtils.ERROR_EXCEPTION_ATTRIBUTE, ex, WebRequest.SCOPE_REQUEST);
        ErrorResponseCode responseCode = ErrorResponseCode.EXTERNAL_BAD_REQUEST;
        HttpHeaders headers = new HttpHeaders();
        addErrorHttpHeader(headers, request, responseCode);
        log.error("Exception Occurred", ex);

        return new ResponseEntity<>(errorResponse(responseCode, ex.getMessage()), headers, responseCode.getHttpStatus());
    }



    @ExceptionHandler(ResourceAccessException.class)
    public final ResponseEntity<Object> handleResourceAccessException(ResourceAccessException ex, WebRequest request) throws Exception {
        request.setAttribute(WebUtils.ERROR_EXCEPTION_ATTRIBUTE, ex, WebRequest.SCOPE_REQUEST);
        ErrorResponseCode responseCode = ErrorResponseCode.EXTERNAL_CONNECTION_FAIL;
        HttpHeaders headers = new HttpHeaders();
        addErrorHttpHeader(headers, request, responseCode);
        log.error("Exception Occurred", ex);

        return new ResponseEntity<>(errorResponse(responseCode, ex.getMessage()), headers, responseCode.getHttpStatus());
    }

    /**
     * requestParam exception
     **/
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
//        return super.handleMethodArgumentNotValid(ex, headers, status, request);
        request.setAttribute(WebUtils.ERROR_EXCEPTION_ATTRIBUTE, ex, WebRequest.SCOPE_REQUEST);
        ErrorResponseCode responseCode = ErrorResponseCode.REQUEST_PARAMETER_MISSING;
        addErrorHttpHeader(headers, request, responseCode);
        log.error("Exception Occurred", ex);

        StringBuilder errorMessage = new StringBuilder();
        errorMessage.append(ex.getFieldError().getField())
                .append(" ")
                .append(ex.getFieldError().getDefaultMessage());

        return new ResponseEntity<>(errorResponse(responseCode, errorMessage.toString()), headers, responseCode.getHttpStatus());
    }


    /**
     * redis exception
     **/

    @ExceptionHandler(RedisConnectionFailureException.class)
    public final ResponseEntity<Object> handleRedisConnectionException(RedisConnectionFailureException ex, WebRequest request) throws Exception {
        request.setAttribute(WebUtils.ERROR_EXCEPTION_ATTRIBUTE, ex, WebRequest.SCOPE_REQUEST);
        ErrorResponseCode responseCode = ErrorResponseCode.INTERNAL_MW_CONNECTION_FAIL;
        HttpHeaders headers = new HttpHeaders();
        addErrorHttpHeader(headers, request, responseCode);
        log.error("Exception Occurred", ex);

        return new ResponseEntity<>(errorResponse(responseCode, ex.getMessage()), headers, responseCode.getHttpStatus());
    }


    @ExceptionHandler(RedisPipelineException.class)
    public final ResponseEntity<Object> handleRedisPipelineException(RedisPipelineException ex, WebRequest request) throws Exception {
        request.setAttribute(WebUtils.ERROR_EXCEPTION_ATTRIBUTE, ex, WebRequest.SCOPE_REQUEST);
        ErrorResponseCode responseCode = ErrorResponseCode.INTERNAL_MW_EXECUTE_ERROR;
        HttpHeaders headers = new HttpHeaders();
        addErrorHttpHeader(headers, request, responseCode);
        log.error("Exception Occurred", ex);

        return new ResponseEntity<>(errorResponse(responseCode, ex.getMessage()), headers, responseCode.getHttpStatus());
    }


    @ExceptionHandler(RedisBusyException.class)
    public final ResponseEntity<Object> handleRedisBusyException(RedisBusyException ex, WebRequest request) throws Exception {
        request.setAttribute(WebUtils.ERROR_EXCEPTION_ATTRIBUTE, ex, WebRequest.SCOPE_REQUEST);
        ErrorResponseCode responseCode = ErrorResponseCode.INTERNAL_MW_EXECUTE_ERROR;
        HttpHeaders headers = new HttpHeaders();
        addErrorHttpHeader(headers, request, responseCode);
        log.error("Exception Occurred", ex);

        return new ResponseEntity<>(errorResponse(responseCode, ex.getMessage()), headers, responseCode.getHttpStatus());
    }



    /**
     * DAO SQL Exception Error 처리
     */
    @ExceptionHandler(DataAccessException.class)
    @Nullable
    public final ResponseEntity<Object> handleSQLException(Exception ex, WebRequest request) throws Exception {
        request.setAttribute(WebUtils.ERROR_EXCEPTION_ATTRIBUTE, ex, WebRequest.SCOPE_REQUEST);
        ErrorResponseCode responseCode = ErrorResponseCode.INTERNAL_UNDEFINED_ERROR; //Default Response Code

        if (ex instanceof DuplicateKeyException) {
            responseCode = ErrorResponseCode.REQUEST_RESOURCE_CONFLICT;
        } else if (ex instanceof TypeMismatchDataAccessException) { //Type 안 맞음
            responseCode = ErrorResponseCode.REQUEST_PARAMETER_INVALID;
        } else if (ex instanceof DataIntegrityViolationException) { //FK 이슈 , Size 이슈
            responseCode = ErrorResponseCode.REQUEST_PARAMETER_INVALID;
        } else if (ex instanceof QueryTimeoutException) { // Query Timeout
            responseCode = ErrorResponseCode.INTERNAL_DB_CONNECTION_TIMEOUT;
        } else if (ex instanceof DataAccessResourceFailureException) { //DB 접근 불가
            responseCode = ErrorResponseCode.INTERNAL_DB_CONNECTION_FAIL;
        }

        HttpHeaders headers = new HttpHeaders();
        addErrorHttpHeader(headers, request, responseCode);

        log.error("DAO SQLException Occurred", ex);
        return new ResponseEntity<>(errorResponse(responseCode), headers, responseCode.getHttpStatus());
    }

    /**
     * Servlet 관련 Exception 처리
     */
    @ExceptionHandler(ServletException.class)
    protected ResponseEntity<Object> handleNestedServletException(Exception ex, WebRequest request) {
        request.setAttribute(WebUtils.ERROR_EXCEPTION_ATTRIBUTE, ex, WebRequest.SCOPE_REQUEST);
        ErrorResponseCode responseCode = ErrorResponseCode.REQUEST_UNDEFINED_ERROR; //Default Response Code

        if (ex instanceof ServletRequestBindingException) {
            responseCode = ErrorResponseCode.REQUEST_BINDING_ERROR;
        } else if (ex instanceof HttpMediaTypeException) {
            responseCode = ErrorResponseCode.REQUEST_CONTENT_TYPE_ERROR;
        } else if (ex instanceof HttpRequestMethodNotSupportedException) {
            responseCode = ErrorResponseCode.REQUEST_BINDING_ERROR;
        } else if (ex instanceof MissingServletRequestPartException) {
            responseCode = ErrorResponseCode.REQUEST_PAYLOAD_NOT_READABLE;
        } else if (ex instanceof NoHandlerFoundException) {
            responseCode = ErrorResponseCode.REQUEST_BINDING_ERROR;
        }

        HttpHeaders headers = new HttpHeaders();
        addErrorHttpHeader(headers, request, responseCode);

        log.error("Servlet Exception", ex);
        return new ResponseEntity<>(errorResponse(responseCode), headers, responseCode.getHttpStatus());
    }

    /**
     * 그 외 예외 발생 공통 처리 부
     */
    @ExceptionHandler(Exception.class)
    @Nullable
    public final ResponseEntity<Object> handleOtherException(Exception ex, WebRequest request) throws Exception {
        request.setAttribute(WebUtils.ERROR_EXCEPTION_ATTRIBUTE, ex, WebRequest.SCOPE_REQUEST);
        ErrorResponseCode responseCode = ErrorResponseCode.INTERNAL_UNDEFINED_ERROR;
        HttpHeaders headers = new HttpHeaders();

        log.error("Unknown Internal Exception Occurred", ex);
        return new ResponseEntity<>(errorResponse(responseCode), headers, responseCode.getHttpStatus());
    }

    private HttpHeaders addErrorHttpHeader(HttpHeaders headers, WebRequest request, ErrorResponseCode responseCode) {
        String errorCode = responseCode.getCode() + "";
        headers.add(Constants.ERROR_HEADER, errorCode);
        return headers;
    }

    private ErrorResponse errorResponse(ErrorResponseCode responseCode) {
        return errorResponse(responseCode, null);
    }

    private ErrorResponse errorResponse(ErrorResponseCode responseCode, String description) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setCode(responseCode.getCode());
        errorResponse.setMessage(description == null ? responseCode.getReason() : description);
        return errorResponse;
    }
}
