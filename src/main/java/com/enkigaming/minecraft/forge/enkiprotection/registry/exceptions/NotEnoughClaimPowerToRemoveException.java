package com.enkigaming.minecraft.forge.enkiprotection.registry.exceptions;

import java.util.UUID;

public class NotEnoughClaimPowerToRemoveException extends Exception
{
    public NotEnoughClaimPowerToRemoveException(UUID playerId, int amountAttemptedToRemove, int amountPlayerHadAvailable, int amountPlayerHadTotal)
    {
        this.playerId = playerId;
        this.amountAttemptedToRemove = amountAttemptedToRemove;
        this.amountPlayerHadAvailable = amountPlayerHadAvailable;
        this.amountPlayerHadTotal = amountPlayerHadTotal;
    }
    
    final UUID playerId;
    final int amountAttemptedToRemove;
    final int amountPlayerHadAvailable;
    final int amountPlayerHadTotal;
    
    public UUID getPlayerId()
    { return playerId; }
    
    public int getAmountAttemptedToRemove()
    { return amountAttemptedToRemove; }
    
    public int getAmountPlayerHadAvailable()
    { return amountPlayerHadAvailable; }
    
    public int getAmountPlayerHadTotal()
    { return amountPlayerHadTotal; }
}