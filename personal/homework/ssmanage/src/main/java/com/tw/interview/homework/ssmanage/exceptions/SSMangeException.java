package com.tw.interview.homework.ssmanage.exceptions;

/**
 * we use exception to handle all the error that we think we should print tips to the user
 * in production environment we can use different exception subclass to handle all kinds of errors
 * the class is just simplify what my every day programing
 */
public class SSMangeException extends RuntimeException {

    public SSMangeException(String message) {
        super(message);
    }
}
