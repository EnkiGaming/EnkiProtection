package com.enkigaming.minecraft.forge.enkiprotection.registry.exceptions;

import com.enkigaming.minecraft.forge.enkiprotection.utils.ChunkCoOrdinate;
import com.enkigaming.minecraft.forge.enkiprotection.registry.deprecated.Claim;

public class NotEnoughClaimPowerToClaimException extends Exception
{
    public NotEnoughClaimPowerToClaimException(int claimPower, int claimPowerRequired, ChunkCoOrdinate chunk)
    {
        super();
        
        this.claimPower = claimPower;
        this.claimPowerRequired = claimPowerRequired;
        this.chunk = chunk;
    }
    
    final int claimPower;
    final int claimPowerRequired;
    final ChunkCoOrdinate chunk;
    
    public int getClaimPower()
    { return claimPower; }
    
    public int getClaimPowerRequired()
    { return claimPowerRequired; }
    
    public ChunkCoOrdinate getChunk()
    { return chunk; }
}