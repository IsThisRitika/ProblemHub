package com.problemhub.exception;

public class ProblemNotFoundException extends ResourceNotFoundException {
    public ProblemNotFoundException(Long id) {
        super("Problem statement not found with id: " + id);
    }
}
