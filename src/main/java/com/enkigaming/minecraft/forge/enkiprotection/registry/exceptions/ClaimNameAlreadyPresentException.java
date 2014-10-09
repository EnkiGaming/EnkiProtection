package com.enkigaming.minecraft.forge.enkiprotection.registry.exceptions;

import com.enkigaming.minecraft.forge.enkiprotection.registry.Claim;

public class ClaimNameAlreadyPresentException extends Exception
{
    public ClaimNameAlreadyPresentException(Claim originalClaim, Claim claimThatCantUseName, String name)
    {
        this.claimAlreadyPresentWithName = originalClaim;
        this.claimThatCantUseName = claimThatCantUseName;
        this.name = name;
    }
    
    final Claim claimAlreadyPresentWithName;
    final Claim claimThatCantUseName;
    final String name;
    
    public Claim getOriginalClaimWithId()
    { return claimAlreadyPresentWithName; }
    
    public Claim getClaimThatCantUseId()
    { return claimThatCantUseName; }
    
    public String getName()
    { return name; }
}