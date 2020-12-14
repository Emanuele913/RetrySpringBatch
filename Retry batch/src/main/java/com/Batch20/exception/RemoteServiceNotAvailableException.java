package com.Batch20.exception;

public class RemoteServiceNotAvailableException extends Exception {

    private String error;


    public RemoteServiceNotAvailableException(String message) {
        super(message);

    }

}
