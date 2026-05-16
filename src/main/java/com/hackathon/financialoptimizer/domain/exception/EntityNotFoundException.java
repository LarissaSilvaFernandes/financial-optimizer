package com.hackathon.financialoptimizer.domain.exception;

public class EntityNotFoundException extends DomainException {
    public EntityNotFoundException(String entity, Object id) {
        super(entity + " não encontrado: " + id);
    }
}
