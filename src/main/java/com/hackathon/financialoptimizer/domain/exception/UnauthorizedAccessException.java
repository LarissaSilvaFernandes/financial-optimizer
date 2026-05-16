package com.hackathon.financialoptimizer.domain.exception;

public class UnauthorizedAccessException extends DomainException {
    public UnauthorizedAccessException() {
        super("Acesso não autorizado a este recurso");
    }
}
