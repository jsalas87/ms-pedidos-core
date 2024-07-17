package com.rfsc.pedidos.config.exception;

import com.rfsc.pedidos.config.ErrorCode;

public final class CreatingKongObjectException extends GenericException {

    public CreatingKongObjectException(ErrorCode errorCode, String message){
        super(errorCode, message);
    }
}
