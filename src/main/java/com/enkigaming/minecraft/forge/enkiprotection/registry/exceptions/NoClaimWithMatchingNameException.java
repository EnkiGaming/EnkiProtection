package com.enkigaming.minecraft.forge.enkiprotection.registry.exceptions;

public class NoClaimWithMatchingNameException extends Exception
{
    public NoClaimWithMatchingNameException(String name)
    { this.name = name; }
    
    protected final String name;
    
    public String getName()
    { return name; }
}