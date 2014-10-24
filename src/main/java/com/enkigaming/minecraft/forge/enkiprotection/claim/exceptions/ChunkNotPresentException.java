package com.enkigaming.minecraft.forge.enkiprotection.claim.exceptions;

import com.enkigaming.minecraft.forge.enkiprotection.utils.ChunkCoOrdinate;

public class ChunkNotPresentException extends Exception
{
    public ChunkNotPresentException(ChunkCoOrdinate chunk)
    { chunkNotPresent = chunk; }
    
    final ChunkCoOrdinate chunkNotPresent;
    
    public ChunkCoOrdinate getChuk()
    { return chunkNotPresent; }
}