package com.enkigaming.minecraft.forge.enkiprotection.registry.exceptions;

import com.enkigaming.minecraft.forge.enkiprotection.utils.ChunkCoOrdinate;
import com.enkigaming.minecraft.forge.enkiprotection.registry.Claim;

public class NotEnoughClaimPowerToClaimException extends Exception
{
    public NotEnoughClaimPowerToClaimException(Claim claim, int claimPower, int claimPowerRequired, ChunkCoOrdinate chunk)
    {
        super();
        
        this.claim = claim;
        this.claimPower = claimPower;
        this.claimPowerRequired = claimPowerRequired;
        this.chunk = chunk;
    }
    
    final Claim claim;
    final int claimPower;
    final int claimPowerRequired;
    final ChunkCoOrdinate chunk;
    
    public Claim getClaim()
    { return claim; }
    
    public int getClaimPower()
    { return claimPower; }
    
    public int getClaimPowerRequired()
    { return claimPowerRequired; }
    
    public ChunkCoOrdinate getChunk()
    { return chunk; }
}