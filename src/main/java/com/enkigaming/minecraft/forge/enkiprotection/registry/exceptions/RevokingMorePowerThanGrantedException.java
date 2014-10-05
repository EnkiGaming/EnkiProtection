package com.enkigaming.minecraft.forge.enkiprotection.registry.exceptions;

import com.enkigaming.minecraft.forge.enkiprotection.registry.Claim;
import java.util.UUID;

public class RevokingMorePowerThanGrantedException extends Exception
{
    public RevokingMorePowerThanGrantedException(Claim claim, UUID playerID, int powerAttemptingToRevoke, int powerPlayerHasGranted)
    {
        this.claim = claim;
        player = playerID;
        this.powerAttemptingToRevoke = powerAttemptingToRevoke;
        this.powerPlayerHasGranted = powerPlayerHasGranted;
    }
    
    Claim claim;
    UUID player;
    int powerAttemptingToRevoke;
    int powerPlayerHasGranted;
    
    public Claim getClaim()
    { return claim; }
    
    public UUID getPlayerID()
    { return player; }
    
    public int getPowerAttemptingToRevoke()
    { return powerAttemptingToRevoke; }
    
    public int getPowerPlayerHasGranted()
    { return powerPlayerHasGranted; }
}