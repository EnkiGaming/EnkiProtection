package com.enkigaming.minecraft.forge.enkiprotection.exceptions;

public class ObjectFinishedWithException extends RuntimeException
{
    public ObjectFinishedWithException() { super(); }
    public ObjectFinishedWithException(String msg) { super(msg); }
    public ObjectFinishedWithException(Throwable cause) { super(cause); }
    public ObjectFinishedWithException(String msg, Throwable cause) { super(msg, cause); }
}