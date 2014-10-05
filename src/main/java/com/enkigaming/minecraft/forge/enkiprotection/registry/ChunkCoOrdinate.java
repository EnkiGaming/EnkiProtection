package com.enkigaming.minecraft.forge.enkiprotection.registry;

import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

/**
 * Representation of the information required to reference a specific chunk.
 * Immutable.
 */
public class ChunkCoOrdinate
{
	public final int posX;
	public final int posZ;
	public final int worldID;
	
	public ChunkCoOrdinate(int x, int z, int wid)
	{
		posX = x;
		posZ = z;
		worldID = wid;
	}
	
	/**
	 * Gets the highest-value X coördinate within the chunk.
	 */
	public int getMaxXBlock()
	{ return posX + 15; }
	
	/**
	 * Gets the highest-value Z coördinate within the chunk.
	 */
	public int getMaxZBlock()
	{ return posZ + 15; }
	
	/**
	 * Gets the chunk from World
	 */
	public Chunk getChunk(World w)
	{ return w.getChunkFromChunkCoords(posX, posZ); }

	public boolean equals(Object obj)
	{
		if(obj == null || !(obj instanceof ChunkCoOrdinate))
			return false;
		ChunkCoOrdinate other = (ChunkCoOrdinate) obj;
		return other.posX == posX && other.posZ == posZ && other.worldID == worldID;
	}
	
	public int hashCode()
	{
		int hash = 3;
		hash = 31 * hash + posX;
		hash = 31 * hash + posZ;
		hash = 31 * hash + worldID;
		return hash;
	}
}