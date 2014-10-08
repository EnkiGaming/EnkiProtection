package com.enkigaming.minecraft.forge.enkiprotection.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.ChunkPosition;

public class Utils
{
    public static boolean stringParsesToTrue(String string)
    {
        string = string.trim();
        
        return string.equalsIgnoreCase("true")
            || string.equalsIgnoreCase("yes")
            || string.equalsIgnoreCase("positive")
            || string.equalsIgnoreCase("pos")
            || string.equalsIgnoreCase("t")
            || string.equalsIgnoreCase("y")
            || string.equalsIgnoreCase("p")
            || string.equalsIgnoreCase("1");
    }
    
    public static boolean stringParsesToFalse(String string)
    {
        string = string.trim();
        
        return string.equalsIgnoreCase("false")
            || string.equalsIgnoreCase("no")
            || string.equalsIgnoreCase("negative")
            || string.equalsIgnoreCase("neg")
            || string.equalsIgnoreCase("f")
            || string.equalsIgnoreCase("n")
            || string.equalsIgnoreCase("0");
    }
    
    /**
     * Gets the block position in the chunk at the corner with the lowest X, Y, and Z values.
     * @param chunkXCoOrd The X coördinate of the chunk the returned block position will be in.
     * @param chunkZCoOrd The Z coördinate of the chunk the returned block position will be in.
     * @return A ChunkPosition containing the block in the corner of the chunk with the lowest X, Y, and Z values.
     */
    public static ChunkPosition getLowerBoundBlockBorderOfChunk(int chunkXCoOrd, int chunkZCoOrd)
    { return new ChunkPosition(chunkXCoOrd * 16, 0, chunkXCoOrd * 16); }
    
    /**
     * Gets the block position in the chunk at the corner with the highest X, Y, and Z values.
     * @param chunkXCoOrd The X coördinate of the chunk the returned block position will be in.
     * @param chunkZCoOrd The Z coördinate of the chunk the returned block position will be in.
     * @return A ChunkPosition containing the block in the corner of the chunk with the highest X, Y, and Z values.
     */
    public static ChunkPosition getUpperBoundBlockBorderOfChunk(int chunkXCoOrd, int chunkZCoOrd)
    { return new ChunkPosition(chunkXCoOrd * 16 + 15, 255, chunkXCoOrd * 16 + 15); }
    
    /**
     * Gets the dimension ID of the world the passed player will spawn in if they die.
     * @param player The player being checked.
     * @return The dimension ID the player will spawn in.
     */
    public static int getSpawnDimensionID(EntityPlayer player)
    {
        if(player.worldObj.provider.canRespawnHere())
            return player.worldObj.provider.dimensionId;
        else
            return player.worldObj.provider.getRespawnDimension((EntityPlayerMP)player);
    }
    
    
    
    private static Collection<UUID> respawningPlayers = new ArrayList<UUID>();
    
    public static boolean playerIsCurrentlyRespawning(UUID playerUUID)
    { return respawningPlayers.contains(playerUUID); }
    
    public static void markPlayerAsRespawning(UUID playerUUID)
    { respawningPlayers.add(playerUUID); }
    
    public static void unmarkPlayerAsRespawning(UUID playerUUID)
    { respawningPlayers.remove(playerUUID); }
}