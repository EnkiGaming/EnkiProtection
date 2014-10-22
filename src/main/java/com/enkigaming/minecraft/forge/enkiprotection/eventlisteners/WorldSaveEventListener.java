package com.enkigaming.minecraft.forge.enkiprotection.eventlisteners;

import com.enkigaming.mcforge.enkilib.EnkiLib;
import com.enkigaming.minecraft.forge.enkiprotection.EnkiProtection;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.event.world.WorldEvent;

public class WorldSaveEventListener
{
    @SubscribeEvent
    public void onWorldSave(WorldEvent.Save event)
    { EnkiProtection.getInstance().getFileHandling().save(); }
}