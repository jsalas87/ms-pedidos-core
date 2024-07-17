package com.rfsc.pedidos.config.exception;

import com.rfsc.pedidos.config.ErrorCode;
import org.apache.commons.lang3.StringUtils;

public abstract class GenericException extends RuntimeException {

    private static final String SPACE = StringUtils.SPACE;
    private static final String COMMA = ",";
    private final ErrorCode errorCode;

    protected GenericException(ErrorCode errorCode) {
        super(errorCode.getReasonPhrase());
        this.errorCode = errorCode;
    }

    protected GenericException(ErrorCode errorCode, String message) {
        super(buildMessage(message, errorCode));
        this.errorCode = errorCode;
    }

    protected GenericException(ErrorCode errorCode, String message, Throwable cause) {
        super(buildMessage(message, errorCode), cause);
        this.errorCode = errorCode;
    }

    public ErrorCode getCode() {
        return this.errorCode;
    }

    private static String buildMessage(String message, ErrorCode errorCode) {
        return errorCode.getReasonPhrase() + COMMA + SPACE + message;
    }

}
