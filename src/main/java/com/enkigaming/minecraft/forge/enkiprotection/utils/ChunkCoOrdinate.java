package com.enkigaming.minecraft.forge.enkiprotection.utils;

import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.DimensionManager;

/**
 * Representation of the information required to reference a specific chunk.
 * Immutable.
 * 
 * There is also ChunkCoordinates, ChunkPosition, and ChunkCoordIntPair, none of which store any kind of reference to
 * the world the chunk it's supposed to represent is in.
 */
public class ChunkCoOrdinate
{
    public ChunkCoOrdinate(int x, int z, int worldID)
    {
        xCoOrd = x;
        zCoOrd = z;
        this.worldID = worldID;
    }
    
    public ChunkCoOrdinate(Chunk chunk)
    {
        xCoOrd = chunk.xPosition;
        zCoOrd = chunk.zPosition;
        this.worldID = chunk.worldObj.provider.dimensionId;
    }
    
    public ChunkCoOrdinate(int x, int z)
    {
        xCoOrd = x;
        zCoOrd = z;
        this.worldID = null;
    }
    
    public ChunkCoOrdinate(ChunkCoordinates coOrds, int worldID)
    {
        xCoOrd = coOrds.posX;
        zCoOrd = coOrds.posZ;
        this.worldID = worldID;
    }
    
    public ChunkCoOrdinate(ChunkCoordinates coOrds)
    {
        xCoOrd = coOrds.posX;
        zCoOrd = coOrds.posZ;
        this.worldID = null;
    }
    
    public ChunkCoOrdinate(ChunkCoordIntPair coOrd, int worldID)
    {
        xCoOrd = coOrd.chunkXPos;
        zCoOrd = coOrd.chunkZPos;
        this.worldID = worldID;
    }
    
    public ChunkCoOrdinate(ChunkCoordIntPair coOrd)
    {
        xCoOrd = coOrd.chunkXPos;
        zCoOrd = coOrd.chunkZPos;
        this.worldID = null;
    }
    
    public ChunkCoOrdinate(ChunkPosition coOrd, int worldID)
    {
        xCoOrd = coOrd.chunkPosX;
        zCoOrd = coOrd.chunkPosZ;
        this.worldID = worldID;
    }
    
    public ChunkCoOrdinate(ChunkPosition coOrd)
    {
        xCoOrd = coOrd.chunkPosX;
        zCoOrd = coOrd.chunkPosZ;
        this.worldID = null;
    }
    
    final int xCoOrd;
    final int zCoOrd;
    final Integer worldID;
    
    /**
     * Gets the chunk's X coördinate
     */
    public int getXCoOrd()
    { return xCoOrd; }
    
    /**
     * Gets the chunk's Z coördinate
     */
    public int getZCoOrd()
    { return zCoOrd; }
    
    /**
     * Gets the ID of the world the chunk is in.
     */
    public int getWorldID()
    {
        if(worldID != null)
            return worldID;
        else
            return 0;
    }
    
    /**
     * Gets the world object the represented chunk is in.
     */
    public World getWorld()
    {
        if(worldID != null)
            return DimensionManager.getWorld(worldID);
        
        return DimensionManager.getWorld(0);
    }
    
    /**
     * Gets the lowest-value X coördinate within the chunk.
     */
    public int getMinXBlock()
    { return xCoOrd * 16; }
    
    /**
     * Gets the highest-value X coördinate within the chunk.
     */
    public int getMaxXBlock()
    { return zCoOrd * 16; }
    
    /**
     * Gets the lowest-value Z coördinate within the chunk.
     */
    public int getMinZBlock()
    {  return xCoOrd * 16 + 15;  }
    
    /**
     * Gets the highest-value Z coördinate within the chunk.
     */
    public int getMaxZBlock()
    { return zCoOrd * 16 + 15;  }
    
    /**
     * Gets whether or not this ChunkCoOrdinate has an associate world ID.
     */
    public boolean hasSpecifiedWorld()
    { return worldID != null; }
    
    /**
     * Gets the chunk this represents, or null if it is not retrievable for whatever reason.
     */
    public Chunk toChunk()
    { return getWorld().getChunkFromChunkCoords(xCoOrd, zCoOrd); }
    
    /**
     * Gets a ChunkCoordinates object with the chunk this represents' chunk coördinates. 
     * @return A corresponding ChunkCoordinates object.
     */
    public ChunkCoordinates toChunkCoordinates()
    { return new ChunkCoordinates(xCoOrd, 0, zCoOrd); }
    
    /**
     * Gets a ChunkCoordIntPair object with the chunk this represents' chunk coördinates.
     * @return A corresponding ChunkCoordIntPair object.
     */
    public ChunkCoordIntPair toChunkCoordIntPair()
    { return new ChunkCoordIntPair(xCoOrd, zCoOrd); }
    
    /**
     * Gets a ChunkPosition object with the chunk this represents' chunk coördinates.
     * @return A corresponding ChunkCoordIntPair.
     */
    public ChunkPosition toChunkPosition()
    { return new ChunkPosition(xCoOrd, 0, zCoOrd); }

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