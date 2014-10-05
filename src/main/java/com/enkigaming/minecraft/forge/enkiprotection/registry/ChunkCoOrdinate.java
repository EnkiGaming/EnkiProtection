package com.enkigaming.minecraft.forge.enkiprotection.registry;

import net.minecraft.world.chunk.Chunk;

/**
 * Representation of the information required to reference a specific chunk.
 * Immutable.
 */
public class ChunkCoOrdinate
{
    public ChunkCoOrdinate(int x, int z, int worldID)
    {
        xCoOrd = x;
        zCoOrd = z;
        this.worldID = worldID;
    }
    
    final int xCoOrd;
    final int zCoOrd;
    final int worldID;
    
    /**
     * Gets the chunk's X coördinate
     */
    public int getXCoOrd()
    {}
    
    /**
     * Gets the chunk's Z coördinate
     */
    public int getZCoOrd()
    {}
    
    /**
     * Gets the ID of the world the chunk is in.
     */
    public int getWorldID()
    {}
    
    /**
     * Gets the name of the world the chunk is in.
     */
    public String getWorldName() // Is this possible?
    {}
    
    /**
     * Gets the lowest-value X coördinate within the chunk.
     */
    public int getMinXBlock()
    {}
    
    /**
     * Gets the highest-value X coördinate within the chunk.
     */
    public int getMaxXBlock()
    {}
    
    /**
     * Gets the lowest-value Z coördinate within the chunk.
     */
    public int getMinZBlock()
    {}
    
    /**
     * Gets the highest-value Z coördinate within the chunk.
     */
    public int getMaxZBlock()
    {}
    
    /**
     * Gets the chunk this represents, or null if it is not retrievable for whatever reason.
     */
    public Chunk getChunk()
    {}

    @Override
    public boolean equals(Object obj)
    {
        if(obj == null)
            return false;
        if(getClass() != obj.getClass())
            return false;
        final ChunkCoOrdinate other = (ChunkCoOrdinate) obj;
        if(this.xCoOrd != other.xCoOrd)
            return false;
        if(this.zCoOrd != other.zCoOrd)
            return false;
        if(this.worldID != other.worldID)
            return false;
        return true;
    }

    @Override
    public int hashCode()
    {
        int hash = 3;
        hash = 31 * hash + this.xCoOrd;
        hash = 31 * hash + this.zCoOrd;
        hash = 31 * hash + this.worldID;
        return hash;
    }
}