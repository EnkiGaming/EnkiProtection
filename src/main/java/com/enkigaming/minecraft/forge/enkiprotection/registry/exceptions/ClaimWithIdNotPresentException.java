package com.enkigaming.minecraft.forge.enkiprotection.registry.exceptions;

import com.enkigaming.minecraft.forge.enkiprotection.registry.ClaimRegistry;
import java.util.UUID;

public class ClaimWithIdNotPresentException extends IllegalArgumentException
{
    public ClaimWithIdNotPresentException(ClaimRegistry registry, UUID id)
    {
        this.registry = registry;
        this.id = id;
    }
    
    final ClaimRegistry registry;
    final UUID id;
    
    public ClaimRegistry getRegistryNameNotFoundIn()
    { return registry; }
    
    public UUID getId()
    { return id; }
}
