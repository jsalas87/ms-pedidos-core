package com.rfsc.pedidos.config.exception;


import com.rfsc.pedidos.config.ErrorCode;

public final class RetrievingKongObjectException extends GenericException{

    public RetrievingKongObjectException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }
}
