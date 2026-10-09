package com.sena.catalog.domain.dto;

import java.time.LocalDateTime;

public class ErrorResponseDTO {

    private int codigoEstado;
    private String error;
    private String mensaje;
    private LocalDateTime timestamp;

    public ErrorResponseDTO(int codigoEstado, String error, String mensaje, LocalDateTime timestamp) {
        this.codigoEstado = codigoEstado;
        this.error = error;
        this.mensaje = mensaje;
        this.timestamp = timestamp;
    }

    // Getters y Setters
    public int getCodigoEstado() { return codigoEstado; }
    public String getError() { return error; }
    public String getMensaje() { return mensaje; }
    public LocalDateTime getTimestamp() { return timestamp; }
}