package com.enkigaming.minecraft.forge.enkiprotection.registry.exceptions;

import com.enkigaming.minecraft.forge.enkiprotection.claim.Claim;
import com.enkigaming.minecraft.forge.enkiprotection.utils.ChunkCoOrdinate;

public class ChunkNotInClaimException extends Exception
{
    public ChunkNotInClaimException(ChunkCoOrdinate chunk, Claim claim)
    {
        this.chunk = chunk;
        this.claim = claim;
    }
    
    protected final ChunkCoOrdinate chunk;
    protected final Claim claim;
    
    public ChunkCoOrdinate getChunk()
    { return chunk; }
    
    public Claim getClaim()
    { return claim; }
}