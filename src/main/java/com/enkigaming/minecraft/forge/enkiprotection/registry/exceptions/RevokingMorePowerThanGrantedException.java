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
    
    final Claim claim;
    final UUID player;
    final int powerAttemptingToRevoke;
    final int powerPlayerHasGranted;
    
    public Claim getClaim()
    { return claim; }
    
    public UUID getPlayerID()
    { return player; }
    
    public int getPowerAttemptingToRevoke()
    { return powerAttemptingToRevoke; }
    
    public int getPowerPlayerHasGranted()
    { return powerPlayerHasGranted; }
}