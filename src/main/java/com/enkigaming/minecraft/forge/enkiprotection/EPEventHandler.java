package com.enkigaming.minecraft.forge.enkiprotection;

import java.io.*;

import net.minecraftforge.event.world.WorldEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class EPEventHandler
{
	/*
	CommandListener.java
	ExplosionHandler.java
	PlayerBreakBlockHandler.java
	PlayerCombatListener.java
	PlayerDeathHandler.java
	PlayerMovedChunksHandler.java
	PlayerPlaceBlockHandler.java
	PlayerRightClickHandler.java
	*/
	
	@SubscribeEvent
	public void loadWorld(WorldEvent.Load e)
	{
		File f = new File(e.world.getSaveHandler().getWorldDirectory(), "EnkiProtection.txt");
		
		if(f.exists())
		{
			try
			{
				FileInputStream fis = new FileInputStream(f);
				byte[] b = new byte[fis.available()];
				fis.read(b); fis.close();
				
				String txt = new String(b);
				
				System.out.println(txt); //Parse text to json
			}
			catch(Exception ex)
			{ ex.printStackTrace(); }
		}
	}
	
	@SubscribeEvent
	public void saveWorld(WorldEvent.Save e)
	{
		File f = new File(e.world.getSaveHandler().getWorldDirectory(), "EnkiProtection.txt");
		
		try
		{
			if(!f.exists()) f.createNewFile();
			FileOutputStream fos = new FileOutputStream(f);
			
			String txt = "Lololo testing"; // Parse json to text
			
			byte[] b = txt.getBytes();
			fos.write(b); fos.close();
		}
		catch(Exception ex)
		{ ex.printStackTrace(); }
	}
}