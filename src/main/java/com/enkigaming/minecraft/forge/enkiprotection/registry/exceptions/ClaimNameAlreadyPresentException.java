package com.enkigaming.minecraft.forge.enkiprotection.registry.exceptions;

import com.enkigaming.minecraft.forge.enkiprotection.claim.Claim;

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
    
    public Claim getOriginalClaimWithName()
    { return claimAlreadyPresentWithName; }
    
    /**
     * Returns the claim that was to be added to the registry, but can't because there's already a claim with that name.
     *
     * @return The specified claim, or null if this exception was thrown in the process of creating a claim, such as by using ClaimRegistry.createClaim(String);
     */
    public Claim getClaimThatCantUseName()
    { return claimThatCantUseName; }
    
    public String getName()
    { return name; }
}