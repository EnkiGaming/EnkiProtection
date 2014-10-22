package com.enkigaming.minecraft.forge.enkiprotection;

import com.enkigaming.mcforge.enkilib.filehandling.FileHandlerRegistry;
import com.enkigaming.minecraft.forge.enkiprotection.commandlisteners.CmdClaim;
import com.enkigaming.minecraft.forge.enkiprotection.commandlisteners.CmdEnkiprotection;
import com.enkigaming.minecraft.forge.enkiprotection.commandlisteners.CmdPower;
import com.enkigaming.minecraft.forge.enkiprotection.eventlisteners.PlayerCombatEventListener;
import com.enkigaming.minecraft.forge.enkiprotection.eventlisteners.PlayerDeathEventListener;
import com.enkigaming.minecraft.forge.enkiprotection.eventlisteners.PlayerInteractEventListener;
import com.enkigaming.minecraft.forge.enkiprotection.eventlisteners.PlayerMovedChunksEventListener;
import com.enkigaming.minecraft.forge.enkiprotection.eventlisteners.PlayerPlaceOrBreakBlockEventListener;
import com.enkigaming.minecraft.forge.enkiprotection.eventlisteners.WorldSaveEventListener;
import com.enkigaming.minecraft.forge.enkiprotection.registry.ClaimRegistry;
import com.enkigaming.minecraft.forge.enkiprotection.registry.PendingInvitationRegistry;
import com.enkigaming.minecraft.forge.enkiprotection.registry.PlayerAutoclaimRegistry;
import com.enkigaming.minecraft.forge.enkiprotection.registry.PlayerPowerRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import java.io.File;
import net.minecraftforge.common.MinecraftForge;

@Mod(modid = EnkiProtection.MODID, name = EnkiProtection.NAME, version = EnkiProtection.VERSION, acceptableRemoteVersions = "*")
public class EnkiProtection
{
    public static final String NAME = "EnkiProtection";
    public static final String MODID = "EnkiProtection";
    public static final String VERSION = "1.0";
    
    @Instance("EnkiProtection")
    static EnkiProtection instance;
    
    Settings settings;
    ClaimRegistry claimRegistry;
    PlayerPowerRegistry claimPowerRegistry;
    PendingInvitationRegistry invitationRegistry;
    PlayerAutoclaimRegistry autoclaimRegistry;
    FileHandlerRegistry fileHandling;
    File saveFolder;
    
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
    private void registerCommands(FMLServerStartingEvent event)
    {
        event.registerServerCommand(new CmdClaim());
        event.registerServerCommand(new CmdPower());
        event.registerServerCommand(new CmdEnkiprotection());
    }
    
    private void initialiseRegistries()
    {
        fileHandling = new FileHandlerRegistry();
        settings = new Settings();
        
        claimRegistry = new ClaimRegistry(saveFolder);
        claimPowerRegistry = new PlayerPowerRegistry(saveFolder);
        invitationRegistry = new PendingInvitationRegistry(saveFolder);
        autoclaimRegistry = new PlayerAutoclaimRegistry();
    }
    
    private void loadData()
    { fileHandling.load(); }
    
    private void saveData()
    { fileHandling.save(); }
    
    private void registerEvents()
    {
        // register explosion event listener
        MinecraftForge.EVENT_BUS.register(new PlayerCombatEventListener());
        
        PlayerDeathEventListener deathHandler = new PlayerDeathEventListener();
        FMLCommonHandler.instance().bus().register(deathHandler);
        MinecraftForge.EVENT_BUS.register(deathHandler);
        
        MinecraftForge.EVENT_BUS.register(new PlayerInteractEventListener());
        MinecraftForge.EVENT_BUS.register(new PlayerMovedChunksEventListener());
        MinecraftForge.EVENT_BUS.register(new PlayerPlaceOrBreakBlockEventListener());
        MinecraftForge.EVENT_BUS.register(new WorldSaveEventListener());
    }
    
    public static EnkiProtection getInstance()
    { return instance; }
    
    public ClaimRegistry getClaims()
    { return claimRegistry; }
    
    public PlayerPowerRegistry getClaimPowers()
    { return claimPowerRegistry; }
    
    public PendingInvitationRegistry getPendingInvitations()
    { return invitationRegistry; }
    
    public PlayerAutoclaimRegistry getAutoclaimStates()
    { return autoclaimRegistry; }
    
    public Settings getSettings()
    { return settings; }
    
    public FileHandlerRegistry getFileHandling()
    { return fileHandling; }
}
