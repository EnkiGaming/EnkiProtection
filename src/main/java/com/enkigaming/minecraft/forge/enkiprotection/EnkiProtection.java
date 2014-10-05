package com.enkigaming.minecraft.forge.enkiprotection;

import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.*;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = EnkiProtection.MODID, name = "EnkiProtection", version = "1.0", acceptableRemoteVersions = "*")
public class EnkiProtection
{
    public static final String MODID = "EnkiProtection";
    
    @EventHandler
    public void preInit(FMLPreInitializationEvent e)
    {
    	EnkiProtectionEventHandler eh = new EnkiProtectionEventHandler();
    	MinecraftForge.EVENT_BUS.register(eh);
    	FMLCommonHandler.instance().bus().register(eh);
    }
}