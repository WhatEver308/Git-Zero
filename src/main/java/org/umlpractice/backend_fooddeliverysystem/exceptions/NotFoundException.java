package org.umlpractice.backend_fooddeliverysystem.exceptions;

public class NotFoundException extends Exception{
    public NotFoundException(String message){super(message);}
    public NotFoundException(String message, Throwable cause){super(message, cause);}
    public NotFoundException(Throwable cause){super(cause);}
}
