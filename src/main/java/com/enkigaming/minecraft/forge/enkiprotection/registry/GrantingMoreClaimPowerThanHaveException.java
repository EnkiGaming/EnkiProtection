package com.enkigaming.minecraft.forge.enkiprotection.registry;

import java.util.UUID;
import net.minecraft.entity.player.EntityPlayer;

public class GrantingMoreClaimPowerThanHaveException extends Exception
{
    public GrantingMoreClaimPowerThanHaveException(UUID playerID, int claimPowerPlayerHasAvailable,
                                                                  int claimPowerAttmptingToGrant,
                                                                  int claimPowerPlayerHasTotal)
    {
        player = playerID;
        this.claimPowerAttemptingToGrant = claimPowerAttmptingToGrant;
        this.claimPowerPlayerHasAvailable = claimPowerPlayerHasAvailable;
        this.claimPowerPlayerHasTotal = claimPowerPlayerHasTotal;
    }
    
    UUID player;
    int claimPowerAttemptingToGrant;
    int claimPowerPlayerHasAvailable;
    int claimPowerPlayerHasTotal;
    
    public UUID getPlayerID()
    { return player; }
    
    public int getPowerAttemptingToGrant()
    { return claimPowerAttemptingToGrant; }
    
    public int getPowerPlayerHasAvailable()
    { return claimPowerPlayerHasAvailable; }
    
    public int getPowerPlayerHasTotal()
    { return claimPowerPlayerHasTotal; }
}