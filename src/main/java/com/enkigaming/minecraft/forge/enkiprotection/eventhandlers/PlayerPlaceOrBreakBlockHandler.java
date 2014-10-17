package com.enkigaming.minecraft.forge.enkiprotection.eventhandlers;

import com.enkigaming.minecraft.forge.enkiprotection.EnkiProtection;
import com.enkigaming.minecraft.forge.enkiprotection.Strings;
import com.enkigaming.minecraft.forge.enkiprotection.registry.deprecated.Claim;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import java.util.HashSet;
import java.util.Set;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.util.BlockSnapshot;
import net.minecraftforge.event.world.BlockEvent;

public class PlayerPlaceOrBreakBlockHandler
{
    @SubscribeEvent
    public void onPlayerBreakBlock(BlockEvent.BreakEvent event)
    {
        // I think at this point, I've just gotten into the habit of
        // null-checking everything I use that comes from forge events.
        if(event.block == null  || event.getPlayer() == null)
            return;
        
        Claim claim = EnkiProtection.getInstance().getRegistry().getClaimAtBlock(event.x, event.z, event.world);
        
        if(claim != null && !claim.canBreakOrPlaceBlocks(event.getPlayer()))
        {
            event.setCanceled(true);
            event.getPlayer().addChatMessage(new ChatComponentText(Strings.getStringCantBreakBlocks(claim.getName())));
        }
    }
    
    @SubscribeEvent
    public void onPlayerPlaceBlock(BlockEvent.PlaceEvent event)
    {
        if(event.placedBlock == null || event.player == null)
            return;
        
        Claim claim = EnkiProtection.getInstance().getRegistry().getClaimAtBlock(event.x, event.z, event.world);
        
        if(claim != null && !claim.canBreakOrPlaceBlocks(event.player))
        {
            event.setCanceled(true);
            event.player.addChatMessage(new ChatComponentText(Strings.getStringCantPlaceBlocks(claim.getName())));
        }
        
        // Also applies to MultiPlaceEvent, which extends PlaceEvent
    }
    
    @SubscribeEvent
    public void onPlayerPlaceMultipleBlocks(BlockEvent.MultiPlaceEvent event)
    {
        Set<Chunk> chunks = new HashSet<Chunk>();
        
        for(BlockSnapshot block : event.getReplacedBlockSnapshots())
            chunks.add(event.world.getChunkFromBlockCoords(block.x, block.z));
        
        for(Chunk chunk : chunks)
        {
            Claim claim = EnkiProtection.getInstance().getRegistry().getClaimAtChunk(chunk);
            
            if(claim != null && !claim.canBreakOrPlaceBlocks(event.player))
            {
                event.setCanceled(true);
                event.player.addChatMessage(new ChatComponentText(Strings.getStringCantPlaceBlocks(claim.getName())));
                // No return; as I want players to get a message for every chunk they would be breaking blocks in in
                // this one action that they're not allowed to.
            }
        }
    }
}