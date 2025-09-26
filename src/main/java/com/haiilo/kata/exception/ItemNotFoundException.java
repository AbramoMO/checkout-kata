package com.haiilo.kata.exception;

public class ItemNotFoundException extends RuntimeException
{
    public ItemNotFoundException(String message)
    {
        super(message);
    }
}
