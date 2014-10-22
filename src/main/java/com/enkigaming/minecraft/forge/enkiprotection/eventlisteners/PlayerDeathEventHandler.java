package com.enkigaming.minecraft.forge.enkiprotection.eventlisteners;

import com.enkigaming.minecraft.forge.enkiprotection.EnkiProtection;
import com.enkigaming.minecraft.forge.enkiprotection.Strings;
import com.enkigaming.minecraft.forge.enkiprotection.claim.Claim;
import com.enkigaming.minecraft.forge.enkiprotection.utils.Utils;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChunkCoordinates;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.event.entity.living.LivingDeathEvent;

public class PlayerDeathEventHandler
{
    // If a player is banned from a claim that contains the chunk in which their bed is located, they will instead
    // spawn at the server spawn.
    
    public void onPlayerDeath(LivingDeathEvent event)
    {
        if(event.entityLiving instanceof EntityPlayer)
            Utils.markPlayerAsRespawning(((EntityPlayer)event.entityLiving).getGameProfile().getId());
    }
    
    public void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event)
    {
        // Happens immediately after the player respawns. Not before, like some documentation would have you believe.
        // See
        
        if(event.player == null) // How could this happen?
            return;
        
        int playerRespawnDimensionID = Utils.getSpawnDimensionID(event.player);
        ChunkCoordinates bedLocation = event.player.getBedLocation(playerRespawnDimensionID);
        Claim claim = EnkiProtection.getInstance().getRegistry().getClaimAtBlock(bedLocation.posX, bedLocation.posZ, DimensionManager.getWorld(playerRespawnDimensionID));
        
        if(claim != null && !claim.canEnter(event.player))
        {
            ChunkCoordinates serverSpawnLocation = DimensionManager.getWorld(0).getSpawnPoint();
            event.player.setPosition(serverSpawnLocation.posX + 0.5, serverSpawnLocation.posY + 0.5, serverSpawnLocation.posZ + 0.5);
            
            event.player.addChatMessage(new ChatComponentText(Strings.getStringPlayerSpawnedInClaimedNotAllowedChunk(claim.getName())));
        }
        
        Utils.unmarkPlayerAsRespawning(event.player.getGameProfile().getId());
    }
}