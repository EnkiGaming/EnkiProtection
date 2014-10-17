package com.enkigaming.minecraft.forge.enkiprotection.registry.exceptions;

import com.enkigaming.minecraft.forge.enkiprotection.registry.deprecated.Claim;
import java.util.UUID;

public class RevokingMorePowerThanGrantedException extends Exception
{
    public RevokingMorePowerThanGrantedException(UUID playerID, int powerAttemptingToRevoke, int powerPlayerHasGranted)
    {
        player = playerID;
        this.powerAttemptingToRevoke = powerAttemptingToRevoke;
        this.powerPlayerHasGranted = powerPlayerHasGranted;
    }
    
    final UUID player;
    final int powerAttemptingToRevoke;
    final int powerPlayerHasGranted;
    
    public UUID getPlayerID()
    { return player; }
    
    public int getPowerAttemptingToRevoke()
    { return powerAttemptingToRevoke; }
    
    public int getPowerPlayerHasGranted()
    { return powerPlayerHasGranted; }
}