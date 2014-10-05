package com.enkigaming.minecraft.forge.enkiprotection.registry.exceptions;

import com.enkigaming.minecraft.forge.enkiprotection.registry.ChunkCoOrdinate;
import com.enkigaming.minecraft.forge.enkiprotection.registry.Claim;

public class ChunkNotInClaimException extends Exception
{
    public ChunkNotInClaimException(Claim claimChunkIsNotIn, Claim claimChunkIsIn, ChunkCoOrdinate chunk)
    {
        super();
        
        notIn = claimChunkIsNotIn;
        in = claimChunkIsIn;
        this.chunk = chunk;
    }
    
    Claim notIn;
    Claim in;
    ChunkCoOrdinate chunk;
    
    public Claim getClaimChunkIsNotIn()
    { return notIn; }
    
    public Claim getClaimChunkIsIn()
    { return in; }
    
    public ChunkCoOrdinate getChunk()
    { return chunk; }
}