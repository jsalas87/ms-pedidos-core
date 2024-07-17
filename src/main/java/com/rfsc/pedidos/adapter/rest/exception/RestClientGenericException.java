package com.rfsc.pedidos.adapter.rest.exception;

import com.rfsc.pedidos.config.ErrorCode;
import com.rfsc.pedidos.config.exception.GenericException;

public final class RestClientGenericException extends GenericException {

    public RestClientGenericException(ErrorCode errorCode) {
        super(errorCode);
    }

}
