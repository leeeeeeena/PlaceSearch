package com.yurim.placesearch.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorResponseCode {


    /** 외부 API 연동 오류 */
    EXTERNAL_UNDEFINED_ERROR("0000", "알 수 없는 오류가 발생하였습니다.", HttpStatus.BAD_REQUEST),
    EXTERNAL_AUTHENTICATION_ERROR("0001", "허가 되지 않은 요청입니다.", HttpStatus.BAD_REQUEST),
    EXTERNAL_REQUEST_ASSEMBLE_ERROR("0002", "외부 요청 생성에 실패하였습니다.", HttpStatus.BAD_REQUEST),
    EXTERNAL_TARGET_NOT_FOUND("0003", "외부 요청 대상이 존재하지 않습니다.", HttpStatus.BAD_REQUEST),
    EXTERNAL_NOT_SUPPORTED("0004", "지원이 되지 않는 외부 요청입니다.", HttpStatus.BAD_REQUEST),
    EXTERNAL_BAD_REQUEST("0005", "요청 값이 올바르지 않습니다.", HttpStatus.BAD_REQUEST),

    EXTERNAL_CONNECTION_FAIL("0100", "연동 API 연결 실패하였습니다.", HttpStatus.SERVICE_UNAVAILABLE),
    EXTERNAL_CONNECTION_TIMEOUT("0101", "연동 API 연결이 오래 걸립니다.", HttpStatus.SERVICE_UNAVAILABLE),



    /** 사용자 요청 오류 */
    REQUEST_UNDEFINED_ERROR("4000", "잘못 된 요청입니다.", HttpStatus.BAD_REQUEST),
    REQUEST_NOT_AUTHENTICATE("4100", "인증 되지 않은 요청입니다.", HttpStatus.UNAUTHORIZED),

    REQUEST_BINDING_ERROR("4200", "지원하지 않는 요청입니다.", HttpStatus.BAD_REQUEST),
    REQUEST_CONTENT_TYPE_ERROR("4201", "지원하지 않는 요청입니다.", HttpStatus.BAD_REQUEST),
    REQUEST_PAYLOAD_NOT_READABLE("4202", "잘못 된 요청입니다.", HttpStatus.BAD_REQUEST),

    REQUEST_PARAMETER_MISSING("4300", "필수 값이 누락되었습니다.", HttpStatus.BAD_REQUEST),
    REQUEST_PARAMETER_INVALID("4301", "요청 값이 유효하지 않습니다.", HttpStatus.BAD_REQUEST),
    REQUEST_PARAMETER_NOT_ALLOWED("4302", "요청 값이 허용되지 않습니다.", HttpStatus.BAD_REQUEST),
    REQUEST_RESOURCE_NOT_FOUND("4400", "요청 대상을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    REQUEST_RESOURCE_CONFLICT("4401", "요청 대상이 중복 되었습니다.", HttpStatus.CONFLICT),
    REQUEST_RESOURCE_STATUS_INVALID("4402", "요청을 처리할 수 없습니다.", HttpStatus.BAD_REQUEST),




    /** 서버 내부 오류 */
    INTERNAL_UNDEFINED_ERROR("5000", "알 수 없는 오류가 발생하였습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    INTERNAL_AUTHENTICATION_ERROR("5001", "허가 되지 않은 요청입니다.", HttpStatus.BAD_REQUEST),

    INTERNAL_DB_CONNECTION_FAIL("5100", "DB 연결 실패하였습니다.", HttpStatus.SERVICE_UNAVAILABLE),
    INTERNAL_DB_CONNECTION_TIMEOUT("5101", "DB 연결이 오래 걸립니다.", HttpStatus.SERVICE_UNAVAILABLE),

    INTERNAL_MW_CONNECTION_FAIL("5200", "MW 연결 실패하였습니다.", HttpStatus.SERVICE_UNAVAILABLE),
    INTERNAL_MW_CONNECTION_TIMEOUT("5201", "MW 연결이 오래 걸립니다.", HttpStatus.SERVICE_UNAVAILABLE),

    INTERNAL_MW_EXECUTE_ERROR("5201", "MW의 실행 중 문제가 발생하였습니다.", HttpStatus.SERVICE_UNAVAILABLE),

    ;
    private String code;
    private String reason;
    private HttpStatus httpStatus;

}
