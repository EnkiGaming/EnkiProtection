package com.enkigaming.minecraft.forge.enkiprotection.registry.exceptions;

public class RevokingMorePowerThanAvailableException extends Exception
{
    public RevokingMorePowerThanAvailableException(int availablePower, int powerAttemptingToRevoke)
    {
        this.availablePower = availablePower;
        this.powerAttemptingToRevoke = powerAttemptingToRevoke;
    }
    
    final int availablePower;
    final int powerAttemptingToRevoke;
    
    public int getAvailablePower()
    { return availablePower; }
    
    public int getPowerAttemptingToRevoke()
    { return powerAttemptingToRevoke; }
}