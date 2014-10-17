package com.enkigaming.minecraft.forge.enkiprotection;

import com.enkigaming.minecraft.forge.enkiprotection.registry.ClaimRegistry;
import com.enkigaming.minecraft.forge.enkiprotection.registry.PlayerPowerRegistry;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import java.io.File;

@Mod(modid = EnkiProtection.MODID, name = EnkiProtection.NAME, version = EnkiProtection.VERSION, acceptableRemoteVersions = "*")
public class EnkiProtection
{
    public static final String NAME = "EnkiProtection";
    public static final String MODID = "EnkiProtection";
    public static final String VERSION = "1.0";
    
    @Instance("EnkiProtection")
    static EnkiProtection instance;
    File saveFolder;
    
    ClaimRegistry claimRegistry;
    PlayerPowerRegistry claimPowerRegistry;
    Settings settings;
    
    @EventHandler
    public void init(FMLPreInitializationEvent event)
    {
        EnkiProtection.instance = this;
        saveFolder = new File(event.getModConfigurationDirectory().getParentFile(), "plugins/EnkiProtection");
        
        initialiseRegistries();
        loadData();
        registerEvents();
    }
    
    @EventHandler
    public void initServerStart(FMLServerStartingEvent event)
    {

    }
    
    @EventHandler
    private void registerCommands(FMLServerStartingEvent event)
    {
        
    }
    
    private void initialiseRegistries()
    {
        claimRegistry = new ClaimRegistry(saveFolder);
        settings = new Settings();
    }
    
    private void loadData()
    {
        
    }
    
    private void saveData()
    {
        
    }
    
    private void registerEvents()
    {
        
    }
    
    public static EnkiProtection getInstance()
    { return instance; }
    
    public ClaimRegistry getRegistry()
    { return claimRegistry; }
    
    public PlayerPowerRegistry getPowerRegistry()
    { return claimPowerRegistry; }
    
    public Settings getSettings()
    { return settings; }
}
