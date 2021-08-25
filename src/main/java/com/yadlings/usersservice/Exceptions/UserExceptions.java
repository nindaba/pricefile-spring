package com.yadlings.usersservice.Exceptions;

import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice
public class UserExceptions {
    public static class UserNotFound extends RuntimeException{
        public UserNotFound(String message){
            super(message);
        }
    }
    public static class UsersNotFound extends RuntimeException{
        public UsersNotFound(String message){
            super(message);
        }
    }
    public static class BadCredentials extends RuntimeException{
        public BadCredentials(String message){
            super(message);
        }
    }
    public static class AlreadyExist extends RuntimeException{
        public AlreadyExist(String message){
            super(message);
        }
    }


}
