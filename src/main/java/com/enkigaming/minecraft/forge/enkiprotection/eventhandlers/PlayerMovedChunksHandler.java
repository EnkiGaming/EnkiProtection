package com.enkigaming.minecraft.forge.enkiprotection.eventhandlers;

import com.enkigaming.minecraft.forge.enkiprotection.EnkiProtection;
import com.enkigaming.minecraft.forge.enkiprotection.registry.Claim;
import com.enkigaming.minecraft.forge.enkiprotection.utils.Utils;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.event.entity.EntityEvent;

public class PlayerMovedChunksHandler
{
    public void onPlayerMovedChunks(EntityEvent.EnteringChunk event)
    {
        if(event.entity == null || !(event.entity instanceof EntityPlayer))
            return;
        
        Claim claim = EnkiProtection.getInstance().getRegistry().getClaimAtChunk(event.newChunkX, event.newChunkZ, event.entity.worldObj);
        EntityPlayer player = (EntityPlayer)event.entity;
        
        if(!claim.canEnter((EntityPlayer)event.entity))
        {
            ChunkPosition oldChunkLowerCorner = Utils.getLowerBoundBlockBorderOfChunk(event.oldChunkX, event.oldChunkZ);
            ChunkPosition idealTpBackLocation = new ChunkPosition(oldChunkLowerCorner.chunkPosX + 7, player.serverPosY, oldChunkLowerCorner.chunkPosZ + 7);
            ChunkPosition tpBackLocation = getNearestSafePlaceToTpToInChunk(idealTpBackLocation.chunkPosX, idealTpBackLocation.chunkPosY, idealTpBackLocation.chunkPosZ, player.worldObj);
            
            event.entity.setPosition(tpBackLocation.chunkPosX, tpBackLocation.chunkPosY, tpBackLocation.chunkPosZ);
            
            // This may fire infinitely if the player is somehow already in a chunk that they're not supposed to be in,
            // and try walking into another chunk they're not supposed to be in.
            // TO DO: Add safe-guard against this.
        }
    }
    
    // Move this + the methods it relies on into Utils at some point?
    private ChunkPosition getNearestSafePlaceToTpToInChunk(int x, int y, int z, World world)
    {
        int currentYAscending = y;
        int currentYDescending = y;
        
        // It is generally safer to teleport a player above the layer they were originally in than below it. Thus, the
        // layers being checked above the original Y value should ascend at twice the rate that the layers being checked
        // below the original Y value should descend.
        
        int ascentAttempts = 2;
        int descentAttempts = 1;
        
        int currentAscentAttemptsLeft = ascentAttempts;
        int currentDescentAttemptsLeft = descentAttempts;
        
        while(currentYDescending >= 0 || currentYAscending <= 255 )
        {
            if(currentAscentAttemptsLeft > 0)
            {
                ChunkPosition safeBlock = checkAllBlocksInLayerInChunk(currentYAscending, x, z, world);
                
                if(safeBlock != null) // if a safe block was found
                    return safeBlock;
            }
            else if(currentDescentAttemptsLeft > 0)
            {
                ChunkPosition safeBlock = checkAllBlocksInLayerInChunk(currentYDescending, x, z, world);
                
                if(safeBlock != null) // if a safe block was found
                    return safeBlock;
            }
            else
            {
                currentAscentAttemptsLeft = ascentAttempts;
                currentDescentAttemptsLeft = descentAttempts;
            }
        }
        
        return null; // No safe block was found in the entire chunk.
    }
    
    private ChunkPosition checkAllBlocksInLayerInChunk(int yLevel, int x, int z, World world)
    {
        Chunk chunk = world.getChunkFromBlockCoords(x, z);
        
        ChunkPosition minCorner = Utils.getLowerBoundBlockBorderOfChunk(chunk.xPosition, chunk.zPosition);
        ChunkPosition maxCorner = Utils.getUpperBoundBlockBorderOfChunk(chunk.xPosition, chunk.zPosition);
        
        /*
         * Checking the blocks in each expanding ring in the following loop looks like this.
         * No block is checked twice.
         * 
         * 
         *        lowBoundZ    highBoundZ
         *             v           v
         * highBoundX ># # # # # # #
         *             # . . . . . #
         *             # . . . . . #
         *             # . . . . . #
         *             # . . . . . #
         *             # . . . . . #
         * lowBoundX  ># # # # # # #
         * 
         * 
         *     step 1      step 2      step 3      step 4
         * 
         *    ^ # # # #   # > > > >   # # # # #   # # # # #
         *    ^ . . . #   # . . . #   # . . . v   # . . . #
         *    ^ . . . #   # . . . #   # . . . v   # . . . #
         *    ^ . . . #   # . . . #   # . . . v   # . . . #
         *    ^ # # # #   # # # # #   # # # # v   # < < < #
         */
        
        for(int checkingRingRadius = 0;; checkingRingRadius++)
        {
            boolean blocksHaveBeenChecked = false;
            
            int lowBoundX = x - checkingRingRadius;
            int highBoundX = x + checkingRingRadius;
            int lowBoundZ = z - checkingRingRadius;
            int highBoundZ = z + checkingRingRadius;
            
            for(int currentX = lowBoundX, currentZ = lowBoundZ; currentX <= highBoundX; currentX++)
            {
                if(currentX >= minCorner.chunkPosX && currentX <= maxCorner.chunkPosX
                && currentZ >= minCorner.chunkPosZ && currentZ <= maxCorner.chunkPosZ)
                {
                    if(blockIsSafeToTpTo(currentX, yLevel, currentZ, world))
                        return new ChunkPosition(currentX, yLevel, currentZ);
                    
                    blocksHaveBeenChecked = true;
                }
            }
            
            for(int currentX = highBoundX, currentZ = lowBoundZ + 1; currentZ <= highBoundZ; currentZ++)
            {
                if(currentX >= minCorner.chunkPosX && currentX <= maxCorner.chunkPosX
                && currentZ >= minCorner.chunkPosZ && currentZ <= maxCorner.chunkPosZ)
                {
                    if(blockIsSafeToTpTo(currentX, yLevel, currentZ, world))
                        return new ChunkPosition(currentX, yLevel, currentZ);
                    
                    blocksHaveBeenChecked = true;
                }
            }
            
            for(int currentX = highBoundX - 1, currentZ = highBoundZ; currentX >= lowBoundX; currentX--)
            {
                if(currentX >= minCorner.chunkPosX && currentX <= maxCorner.chunkPosX
                && currentZ >= minCorner.chunkPosZ && currentZ <= maxCorner.chunkPosZ)
                {
                    if(blockIsSafeToTpTo(currentX, yLevel, currentZ, world))
                        return new ChunkPosition(currentX, yLevel, currentZ);
                    
                    blocksHaveBeenChecked = true;
                }
            }
            
            for(int currentX = lowBoundX, currentZ = highBoundZ - 1; currentZ > lowBoundZ /*not a typo*/; currentZ--)
            {
                if(currentX >= minCorner.chunkPosX && currentX <= maxCorner.chunkPosX
                && currentZ >= minCorner.chunkPosZ && currentZ <= maxCorner.chunkPosZ)
                {
                    if(blockIsSafeToTpTo(currentX, yLevel, currentZ, world))
                        return new ChunkPosition(currentX, yLevel, currentZ);
                    
                    blocksHaveBeenChecked = true;
                }
            }
            
            if(!blocksHaveBeenChecked) // if the radius of the ring being checked has expanded to the point where it is no longer actually checking any of the blocks in the chunk
                return null; // No safe blocks to tp to were found in the checked Y layer.
        }
    }
    
    private boolean blockIsSafeToTpTo(int x, int y, int z, World world)
    {
        Block checkingBlock;
        
        // Make sure player isn't teleported into bottom two layers, which would result in the player falling out of
        // the world.
        if(y < 2)
            return false;
        
        // Make sure block the player is being tp'd to is empty.
        checkingBlock = world.getBlock(x, y, z);
        if(checkingBlock.isAir(world, x, y, z))
            return false;
        
        // Why does .isAir() need the block world + coörds passed in when it's called on an instance of Block that
        // already has that information?
        
        // Make sure there is enough room to tp to. The block below the tp block should be air as well.
        if(y > 0)
        {
            checkingBlock = world.getBlock(x, y - 1, z);
            if(checkingBlock.isAir(world, x, y, z))
                return false;
        }
        
        // Add extra checks to see if the block attempting to tp to is safe or not here. This may include in, in the
        // future, checks to make sure the player doesn't get teleported into lava or another harmful liquid, next to
        // a cactus, etc.
        //
        // Remember to make sure you don't check blocks below 0 or above the max build height (255).
        
        return true;
    }
}