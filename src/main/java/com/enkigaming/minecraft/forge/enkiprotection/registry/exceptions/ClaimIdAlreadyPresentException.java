package com.enkigaming.minecraft.forge.enkiprotection.registry.exceptions;

import com.enkigaming.minecraft.forge.enkiprotection.registry.deprecated.Claim;
import java.util.UUID;

public class ClaimIdAlreadyPresentException extends Exception
{
    public ClaimIdAlreadyPresentException(Claim originalClaim, Claim claimThatCantUseId, UUID id)
    {
        this.claimAlreadyPresentWithId = originalClaim;
        this.claimThatCantUseId = claimThatCantUseId;
        this.id = id;
    }
    
    final Claim claimAlreadyPresentWithId;
    final Claim claimThatCantUseId;
    final UUID id;
    
    public Claim getOriginalClaimWithId()
    { return claimAlreadyPresentWithId; }
    
    public Claim getClaimThatCantUseId()
    { return claimThatCantUseId; }
    
    public UUID getId()
    { return id; }
}