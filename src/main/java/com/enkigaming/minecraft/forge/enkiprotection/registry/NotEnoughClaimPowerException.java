package com.enkigaming.minecraft.forge.enkiprotection.registry;

public class NotEnoughClaimPowerException extends Exception
{
    public NotEnoughClaimPowerException(Claim claim, int claimPower, int claimPowerRequired, ChunkCoOrdinate chunk)
    {
        super();
        
        this.claim = claim;
        this.claimPower = claimPower;
        this.claimPowerRequired = claimPowerRequired;
        this.chunk = chunk;
    }
    
    Claim claim;
    int claimPower;
    int claimPowerRequired;
    ChunkCoOrdinate chunk;
    
    public Claim getClaim()
    { return claim; }
    
    public int getClaimPower()
    { return claimPower; }
    
    public int getClaimPowerRequired()
    { return claimPowerRequired; }
    
    public ChunkCoOrdinate getChunk()
    { return chunk; }
}