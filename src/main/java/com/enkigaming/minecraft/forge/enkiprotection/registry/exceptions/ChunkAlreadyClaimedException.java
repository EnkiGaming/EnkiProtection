package com.enkigaming.minecraft.forge.enkiprotection.registry.exceptions;

import com.enkigaming.minecraft.forge.enkiprotection.claim.Claim;
import com.enkigaming.minecraft.forge.enkiprotection.utils.ChunkCoOrdinate;

public class ChunkAlreadyClaimedException extends Exception
{
    public ChunkAlreadyClaimedException(Claim alreadyBelongsTo, ChunkCoOrdinate chunk)
    {
        super();
        
        this.alreadyBelongsTo = alreadyBelongsTo;
        this.chunk = chunk;
    }

    final Claim alreadyBelongsTo;
    final ChunkCoOrdinate chunk;
    
    public Claim getClaimChunkAlreadyBelongsTo()
    { return alreadyBelongsTo; }
    
    public ChunkCoOrdinate getChunk()
    { return chunk; }
}