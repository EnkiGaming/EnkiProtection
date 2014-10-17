package com.enkigaming.minecraft.forge.enkiprotection.registry.exceptions;

import com.enkigaming.minecraft.forge.enkiprotection.utils.ChunkCoOrdinate;
import com.enkigaming.minecraft.forge.enkiprotection.registry.deprecated.Claim;

public class ChunkNotInClaimException extends Exception
{
    public ChunkNotInClaimException(Claim claimChunkIsNotIn, Claim claimChunkIsIn, ChunkCoOrdinate chunk)
    {
        super();
        
        notIn = claimChunkIsNotIn;
        in = claimChunkIsIn;
        this.chunk = chunk;
    }
    
    final Claim notIn;
    final Claim in;
    final ChunkCoOrdinate chunk;
    
    public Claim getClaimChunkIsNotIn()
    { return notIn; }
    
    public Claim getClaimChunkIsIn()
    { return in; }
    
    public ChunkCoOrdinate getChunk()
    { return chunk; }
}