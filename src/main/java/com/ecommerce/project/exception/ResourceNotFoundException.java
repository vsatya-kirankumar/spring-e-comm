package com.ecommerce.project.exception;

public class ResourceNotFoundException extends RuntimeException{

    private String fieldName;
    private String resourceName;
    private Long fieldId;
    private String field;

    public ResourceNotFoundException() {

    }

    public ResourceNotFoundException(String resourceName, String field, String fieldName) {
        super(String.format("%s not found with %s: %s", resourceName, field, fieldName));
        this.fieldName = fieldName;
        this.resourceName = resourceName;
        this.field = field;
    }

    public ResourceNotFoundException(String field, Long fieldId) {
        super(String.format("%s with %d not found", field, fieldId));
        this.field = field;
        this.fieldId = fieldId;
    }
}
