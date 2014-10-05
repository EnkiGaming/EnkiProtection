package com.enkigaming.minecraft.forge.enkiprotection;

import net.minecraftforge.common.MinecraftForge;

import com.enkigaming.minecraft.forge.enkiprotection.cmd.CommandFaction;

import cpw.mods.fml.common.*;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.*;

@Mod(modid = EnkiProtection.MODID, name = "EnkiProtection", version = "1.0", acceptableRemoteVersions = "*")
public class EnkiProtection
{
	public static final String MODID = "EnkiProtection";
	
	public static EPConfig config;
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent e)
	{
		config = new EPConfig(e);
		
		EPEventHandler eh = new EPEventHandler();
		MinecraftForge.EVENT_BUS.register(eh);
		FMLCommonHandler.instance().bus().register(eh);
	}
	
	@EventHandler
	public void serverStarting(FMLServerStartingEvent e)
	{
		e.registerServerCommand(new CommandFaction());
	}
}