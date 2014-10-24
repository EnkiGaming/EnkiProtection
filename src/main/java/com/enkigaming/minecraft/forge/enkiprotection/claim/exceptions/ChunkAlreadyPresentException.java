package com.enkigaming.minecraft.forge.enkiprotection.claim.exceptions;

import com.enkigaming.minecraft.forge.enkiprotection.utils.ChunkCoOrdinate;

public class ChunkAlreadyPresentException extends Exception
{
    public ChunkAlreadyPresentException(ChunkCoOrdinate chunk)
    { chunkAlreadyPresent = chunk; }
    
    final ChunkCoOrdinate chunkAlreadyPresent;
    
    public ChunkCoOrdinate getChuk()
    { return chunkAlreadyPresent; }
}