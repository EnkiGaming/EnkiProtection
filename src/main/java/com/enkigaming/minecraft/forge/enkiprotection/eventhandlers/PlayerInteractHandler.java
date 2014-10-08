package com.enkigaming.minecraft.forge.enkiprotection.eventhandlers;

import com.enkigaming.minecraft.forge.enkiprotection.EnkiProtection;
import com.enkigaming.minecraft.forge.enkiprotection.Strings;
import com.enkigaming.minecraft.forge.enkiprotection.registry.Claim;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.player.EntityInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

public class PlayerInteractHandler
{
    @SubscribeEvent
    public void onPlayerInteractBlock(PlayerInteractEvent event)
    {
        if(event.entityPlayer == null)
            return;
        
        Claim claim = EnkiProtection.getInstance().getRegistry().getClaimAtBlock(event.x, event.z, event.world);
        
        if(claim != null && !claim.canInteractWithBlocksIn(event.entityPlayer))
        {
            event.setCanceled(true);
            event.entityPlayer.addChatMessage(new ChatComponentText(Strings.getStringCantInteractWithBlocks(claim.getName())));
        }
    }
    
    @SubscribeEvent
    public void onPlayerRightClickEntity(EntityInteractEvent event)
    {
        if(event.entityPlayer == null || event.target == null || event.target instanceof EntityLivingBase)
            return;

        Claim claim = EnkiProtection.getInstance().getRegistry().getClaimAtBlock((int)(event.target.posX + 0.5), (int)(event.target.posZ), event.target.worldObj);

        if(claim != null && !claim.canInteractWithEntitiesIn(event.entityPlayer))
        {
            event.setCanceled(true);
            event.entityPlayer.addChatMessage(new ChatComponentText(Strings.getStringCantInteractWithEntities(claim.getName())));
        }
    }
    
    @SubscribeEvent
    public void onPlayerLeftClickEntity(LivingAttackEvent event)
    {
        if(event.entityLiving == null || !(event.entityLiving instanceof EntityPlayer) || event.entity == null || event.entity instanceof EntityLivingBase)
            return;
        
        Claim claim = EnkiProtection.getInstance().getRegistry().getClaimAtBlock((int)(event.entity.posX + 0.5), (int)(event.entity.posZ + 0.5), event.entity.worldObj);
        EntityPlayer player = (EntityPlayer)event.entityLiving;
        
        if(claim != null && !claim.canInteractWithEntitiesIn(player))
        {
            event.setCanceled(true);
            player.addChatMessage(new ChatComponentText(Strings.getStringCantInteractWithEntities(claim.getName())));
        }
    }
}