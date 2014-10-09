package com.enkigaming.minecraft.forge.enkiprotection.registry.exceptions;

import java.util.UUID;

public class NotEnoughClaimPowerToRemoveException extends Exception
{
    public NotEnoughClaimPowerToRemoveException(UUID playerId, int amountAttemptedToRemove, int amountPlayerHad)
    {
        this.playerId = playerId;
        this.amountAttemptedToRemove = amountAttemptedToRemove;
        this.amountPlayerHad = amountPlayerHad;
    }
    
    final UUID playerId;
    final int amountAttemptedToRemove;
    final int amountPlayerHad;
    
    public UUID getPlayerId()
    { return playerId; }
    
    public int getAmountAttemptedToRemove()
    { return amountAttemptedToRemove; }
    
    public int getAmountPlayerHad()
    { return amountPlayerHad; }
}