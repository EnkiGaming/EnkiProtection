package com.enkigaming.minecraft.forge.enkiprotection.registry;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.FileHandler;
import net.minecraft.entity.player.EntityPlayer;

public class PlayerPowerRegistry
{
    public PlayerPowerRegistry(File saveFolder)
    { fileHandler = makeFileHandler(saveFolder); }
    
    protected final Map<UUID, PlayerPower> playerPowers = new HashMap<UUID, PlayerPower>();
    
    FileHandler fileHandler;
    // Filehandler should load after the claim registry
    
    protected FileHandler makeFileHandler(File saveFolder)
    {
        
    }
    
    public PlayerPower getForPlayer(UUID playerId)
    {}
    
    public PlayerPower getForPlayer(EntityPlayer player)
    {}
}