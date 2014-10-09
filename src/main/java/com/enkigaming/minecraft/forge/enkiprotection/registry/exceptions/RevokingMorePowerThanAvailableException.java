package com.enkigaming.minecraft.forge.enkiprotection.registry.exceptions;

import com.enkigaming.minecraft.forge.enkiprotection.registry.Claim;

public class RevokingMorePowerThanAvailableException extends Exception
{
    public RevokingMorePowerThanAvailableException(Claim claim, int availablePower, int powerAttemptingToRevoke)
    {
        this.claim = claim;
        this.availablePower = availablePower;
        this.powerAttemptingToRevoke = powerAttemptingToRevoke;
    }
    
    final Claim claim;
    final int availablePower;
    final int powerAttemptingToRevoke;
    
    public Claim getClaim()
    { return claim; }
    
    public int getAvailablePower()
    { return availablePower; }
    
    public int getPowerAttemptingToRevoke()
    { return powerAttemptingToRevoke; }
}