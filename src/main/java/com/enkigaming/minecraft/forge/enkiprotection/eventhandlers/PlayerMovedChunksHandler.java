package com.enkigaming.minecraft.forge.enkiprotection.eventhandlers;

import com.enkigaming.minecraft.forge.enkiprotection.EnkiProtection;
import com.enkigaming.minecraft.forge.enkiprotection.registry.Claim;
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
            Chunk oldChunk = player.worldObj.getChunkFromChunkCoords(event.oldChunkX, event.newChunkZ);
            //ChunkPosition tpBackTo = new ChunkPosition(oldChunk.getBlockXAt(7), event.entity.posY, oldChunk.getBlockZAt(7))
            
            event.entity.setPosition(p_70107_1_, p_70107_3_, p_70107_5_);
        }
    }
    
    private ChunkPosition getNearestSafePlaceTo(int x, int y, int z, World world)
    {
        
    }
    
    
}