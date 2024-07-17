package com.rfsc.pedidos.adapter.kafka.exception;

import com.rfsc.pedidos.config.ErrorCode;
import com.rfsc.pedidos.config.exception.GenericException;

public class ReadMessageException extends GenericException {

    public ReadMessageException(ErrorCode errorCode) {
        super(errorCode);
    }
}
