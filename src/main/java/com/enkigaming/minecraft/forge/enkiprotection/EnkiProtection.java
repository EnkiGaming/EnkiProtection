package com.enkigaming.minecraft.forge.enkiprotection;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.chunk.Chunk;

@Mod(modid = EnkiProtection.MODID, name = EnkiProtection.NAME, version = EnkiProtection.VERSION, acceptableRemoteVersions = "*")
public class EnkiProtection
{
    public static final String NAME = "EnkiProtection";
    public static final String MODID = "EnkiProtection";
    public static final String VERSION = "1.0";
    
    static EnkiProtection instance;
    
    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        EnkiProtection.instance = this;
    }
    
    public static EnkiProtection getInstance()
    { return instance; }
}
