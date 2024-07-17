package com.rfsc.pedidos.adapter.rest.exception;

import com.rfsc.pedidos.config.ErrorCode;
import com.rfsc.pedidos.config.exception.GenericException;

public class BadRequestRestClientException extends GenericException {

    public BadRequestRestClientException(ErrorCode errorCode) {
        super(errorCode);
    }
}
