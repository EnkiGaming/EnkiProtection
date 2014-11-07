package com.enkigaming.minecraft.forge.enkiprotection.registry.exceptions;

import java.util.UUID;

public class NoClaimWithMatchingIdException extends Exception
{
    public NoClaimWithMatchingIdException(UUID id)
    { this.id = id; }
    
    protected final UUID id;
    
    public UUID getId()
    { return id; }
}