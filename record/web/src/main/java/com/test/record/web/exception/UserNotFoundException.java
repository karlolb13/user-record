package com.test.record.web.exception;

public class UserNotFoundException extends RuntimeException{
    public UserNotFoundException(Long id){
        super("Could not find user id: " + id);
    }
    
}
