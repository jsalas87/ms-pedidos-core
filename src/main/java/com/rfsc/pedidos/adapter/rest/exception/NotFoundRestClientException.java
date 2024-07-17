package com.rfsc.pedidos.adapter.rest.exception;

import com.rfsc.pedidos.config.ErrorCode;
import com.rfsc.pedidos.config.exception.GenericException;

public final class NotFoundRestClientException extends GenericException {

    public NotFoundRestClientException(ErrorCode errorCode) {
        super(errorCode);
    }
}
