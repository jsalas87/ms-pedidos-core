package com.rfsc.pedidos.adapter.rest.exception;

import com.rfsc.pedidos.config.ErrorCode;
import com.rfsc.pedidos.config.exception.GenericException;

public final class NonTargetRestClientException extends GenericException {

    public NonTargetRestClientException(ErrorCode errorCode) {
        super(errorCode);
    }

}
