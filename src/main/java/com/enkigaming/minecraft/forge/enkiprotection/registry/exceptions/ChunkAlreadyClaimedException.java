package com.enkigaming.minecraft.forge.enkiprotection.registry.exceptions;

import com.enkigaming.minecraft.forge.enkiprotection.utils.ChunkCoOrdinate;
import com.enkigaming.minecraft.forge.enkiprotection.registry.Claim;

public class ChunkAlreadyClaimedException extends Exception
{
    public ChunkAlreadyClaimedException(Claim attemptingToClaim, Claim alreadyBelongsTo, ChunkCoOrdinate chunk)
    {
        super();
        
        this.attemptingToClaim = attemptingToClaim;
        this.alreadyBelongsTo = alreadyBelongsTo;
        this.chunk = chunk;
    }
    
    final Claim attemptingToClaim;
    final Claim alreadyBelongsTo;
    final ChunkCoOrdinate chunk;
    
    public Claim getClaimChunkAlreadyBelongsTo()
    { return alreadyBelongsTo; }
    
    public Claim getAttempingToClaim()
    { return attemptingToClaim; }
    
    public ChunkCoOrdinate getChunk()
    { return chunk; }
}