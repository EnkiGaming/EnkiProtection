package com.enkigaming.minecraft.forge.enkiprotection.registry.exceptions;

import com.enkigaming.minecraft.forge.enkiprotection.registry.deprecated.ClaimRegistry;

public class ClaimWithNameNotPresentException extends IllegalArgumentException
{
    public ClaimWithNameNotPresentException(ClaimRegistry registry, String name)
    {
        this.registry = registry;
        this.name = name;
    }
    
    final ClaimRegistry registry;
    final String name;
    
    public ClaimRegistry getRegistryNameNotFoundIn()
    { return registry; }
    
    public String getName()
    { return name; }
}