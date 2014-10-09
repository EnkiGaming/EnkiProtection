package com.enkigaming.minecraft.forge.enkiprotection.registry;

import com.enkigaming.minecraft.forge.enkiprotection.registry.exceptions.NotEnoughClaimPowerToRemoveException;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import net.minecraft.entity.player.EntityPlayer;

public class PlayerClaimPowerRegistry
{
    public PlayerClaimPowerRegistry(File saveFolder)
    { this.saveFolder = saveFolder; }
    
    File saveFolder;
    
    Map<UUID, Integer> playerPowers = new HashMap<UUID, Integer>();
    ClaimRegistry claimRegistry; 
    
    public int getPower(UUID player)
    {}
    
    public int getPower(EntityPlayer player)
    {}
    
    public int setPower(UUID player, int newPower)
    {}
    
    public int setPower(EntityPlayer player)
    {}
    
    public int givePower(UUID player, int amount)
    {}
    
    public int givePower(EntityPlayer player, int amount)
    {}
    
    public int removePower(UUID player, int amount) throws NotEnoughClaimPowerToRemoveException
    {}
    
    public int removePower(EntityPlayer player, int amount) throws NotEnoughClaimPowerToRemoveException
    {}
    
    public int forceRemovePower(UUID player, int amount)
    {}
    
    public int forceRemovePower(EntityPlayer player, int amount)
    {}
    
    public int transferPower(UUID fromPlayer, UUID toPlayer, int amount)
    {}
    
    public int transferPower(EntityPlayer fromPlayer, EntityPlayer toPlayer, int amount)
    {}
}