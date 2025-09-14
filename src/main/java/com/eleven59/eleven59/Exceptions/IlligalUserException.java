package com.eleven59.eleven59.Exceptions;

public class IlligalUserException extends Exception{
    public IlligalUserException(String email){
        super("User with "+ email + " has not registered on this platform");
    }
}
