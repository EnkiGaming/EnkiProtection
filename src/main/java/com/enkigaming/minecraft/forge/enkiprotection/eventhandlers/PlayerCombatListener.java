package com.enkigaming.minecraft.forge.enkiprotection.eventhandlers;

import com.enkigaming.minecraft.forge.enkiprotection.EnkiProtection;
import com.enkigaming.minecraft.forge.enkiprotection.claim.Claim;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.living.LivingAttackEvent;

public class PlayerCombatListener
{
    @SubscribeEvent
    public void onPlayerCombat(LivingAttackEvent event)
    {
        if(event.entity == null || event.entityLiving == null || !(event.entity instanceof EntityPlayer) || !(event.entityLiving instanceof EntityPlayer))
            return;
        
        Claim claim = EnkiProtection.getInstance().getRegistry().getClaimAtBlock(event.entity.serverPosX, event.entity.serverPosZ, event.entity.worldObj);
        
        if(claim != null && !claim.canFight((EntityPlayer)event.entityLiving, (EntityPlayer)event.entity))
            event.setCanceled(true);
    }
}