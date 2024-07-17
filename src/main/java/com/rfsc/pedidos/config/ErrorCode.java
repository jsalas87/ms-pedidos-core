package com.rfsc.pedidos.config;

public enum ErrorCode {

    INTERNAL_ERROR(100, "Error interno del servidor"),
    CLIENT_NOT_FOUND(101, "No se encontro cliente"),
    PRODUCT_NOT_FOUND(102, "No se encontro producto"),
    TIMEOUT(103, "El llamado a Ability devolvio error"),
    ABILITY_BAD_REQUEST(104, "El llamado a Ability devolvio una peticion invalida"),
    WEB_CLIENT_GENERIC(105, "Error del Web Client");

    private final int value;
    private final String reasonPhrase;

    ErrorCode(int value, String reasonPhrase) {
        this.value = value;
        this.reasonPhrase = reasonPhrase;
    }

    public int value() {
        return this.value;
    }

    public String getReasonPhrase() {
        return this.reasonPhrase;
    }
}
