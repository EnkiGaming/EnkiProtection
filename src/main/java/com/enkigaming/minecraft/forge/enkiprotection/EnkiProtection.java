package com.enkigaming.minecraft.forge.enkiprotection;

import com.enkigaming.minecraft.forge.enkiprotection.registry.ClaimRegistry;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import java.io.File;

@Mod(modid = EnkiProtection.MODID, name = EnkiProtection.NAME, version = EnkiProtection.VERSION, acceptableRemoteVersions = "*")
public class EnkiProtection
{
    public static final String NAME = "EnkiProtection";
    public static final String MODID = "EnkiProtection";
    public static final String VERSION = "1.0";
    
    @Mod.Instance("EnkiProtection")
    static EnkiProtection instance;
    File saveFolder;
    
    ClaimRegistry claimRegistry;
    Permissions permissions;
    
    @EventHandler
    public void init(FMLPreInitializationEvent event)
    {
        EnkiProtection.instance = this;
        saveFolder = new File(event.getModConfigurationDirectory().getParentFile(), "Plugins/EnkiProtection");
        
        initialiseRegistries();
        loadData();
        registerEvents();
        registerCommands();
    }
    
    private void initialiseRegistries()
    {
        permissions = new Permissions(saveFolder);
        claimRegistry = new ClaimRegistry(saveFolder);
    }
    
    private void loadData()
    {
        permissions.load();
        claimRegistry.load();
    }
    
    private void saveData()
    {
        permissions.save();
        claimRegistry.save();
    }
    
    private void registerEvents()
    {
        
    }
    
    private void registerCommands()
    {
        
    }
    
    public static EnkiProtection getInstance()
    { return instance; }
    
    public ClaimRegistry getRegistry()
    { return claimRegistry; }
    
    public Permissions getPermissions()
    { return permissions; }
}
