package com.enkigaming.minecraft.forge.enkiprotection;

import java.io.File;

import net.minecraftforge.common.config.Configuration;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

public class EPConfig
{
	private Configuration config;
	
	public Factions factions;
	
	public EPConfig(FMLPreInitializationEvent e)
	{
		config = new Configuration(new File(e.getModConfigurationDirectory(), "EnkiProtection.cfg"));
		
		factions = new Factions();
		
		config.save();
	}
	
	public class General extends Category
	{
		public final String motd;
		
		public General()
		{
			super("general");
			
			motd = getString("motd", "Wellcome to the EnkiGaming TDT server!");
		}
	}
	
	public class Factions extends Category
	{
		public final boolean allowExplosions;
		public final boolean allowAllyCombat;
		public final boolean allowNonAllyCombat;
		public final boolean allowNonAllyEntry;
		public final boolean allowNonAllyRightClick;
		public final boolean allowNonAllyBreakOrPlaceBlocks;
		
		public Factions()
		{
			super("factions");
			
			allowExplosions = getBool("allowExplosions", true);
			allowAllyCombat = getBool("allowAllyCombat", false);
			allowNonAllyCombat = getBool("allowCombat", true);
			allowNonAllyEntry = getBool("allowNonAllyEntry", true);
			allowNonAllyRightClick = getBool("allowNonAllyRightClick", false);
			allowNonAllyBreakOrPlaceBlocks = getBool("allowNonAllyBreakOrPlaceBlocks", false);
		}
	}
	
	protected class Category
	{
		public final String ID;
		
		public Category(String s)
		{ ID = s; }
		
		public String getString(String key, String def, String... comment)
		{ return config.getString(key, ID, def, getComment(comment)); }
		
		public boolean getBool(String key, boolean def, String... comment)
		{ return config.getBoolean(key, ID, def, getComment(comment)); }
		
		public int getInt(String key, int def, int min, int max, String... comment)
		{ return config.getInt(key, ID, def, min, max, getComment(comment)); }
		
		private String getComment(String... comment)
		{
			String s = "";
			
			for(int i = 0; i < comment.length; i++)
			{
				s += comment[i];
				if(i != comment.length - 1)
					s += "\n";
			}
			
			return s;
		}
	}
}