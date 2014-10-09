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
    
    final File saveFolder;
    
    final Map<UUID, Integer> playerPowers = new HashMap<UUID, Integer>();
    ClaimRegistry claimRegistry; 
    
    public int getPower(UUID player)
    {
        synchronized(playerPowers)
        { return playerPowers.get(player); }
    }
    
    public int getPower(EntityPlayer player)
    { return getPower(player.getGameProfile().getId()); }
    
    public int setPower(UUID player, int newPower)
    {
        synchronized(playerPowers)
        { return playerPowers.put(player, newPower); }
    }
    
    public int setPower(EntityPlayer player, int newPower)
    { return setPower(player.getGameProfile().getId(), newPower); }
    
    /**
     * Increases the player's claim power by the specified amount.
     * @param player The player having their claim power increased.
     * @param amount The amount the player's claim power should be increased by.
     * @return The claim power the player had prior to increasing.
     */
    public int givePower(UUID player, int amount)
    {
        synchronized(playerPowers)
        {
            int oldPower = playerPowers.get(player);
            return playerPowers.put(player, oldPower + amount);
        }
    }
    
    public int givePower(EntityPlayer player, int amount)
    { return givePower(player.getGameProfile().getId(), amount); }
    
    public int removePower(UUID player, int amount) throws NotEnoughClaimPowerToRemoveException
    {
        synchronized(playerPowers)
        {
             int oldPower = playerPowers.get(player);
             
             if(amount > oldPower)
                 throw new NotEnoughClaimPowerToRemoveException(player, amount, oldPower);
             
             return playerPowers.put(player, oldPower - amount);
        }
    }
    
    public int removePower(EntityPlayer player, int amount) throws NotEnoughClaimPowerToRemoveException
    { return removePower(player.getGameProfile().getId(), amount); }
    
    public int forceRemovePower(UUID player, int amount)
    {
        synchronized(playerPowers)
        {
            int oldPower = playerPowers.get(player);
            return playerPowers.put(player, oldPower - amount);
        }
    }
    
    public int forceRemovePower(EntityPlayer player, int amount)
    { return forceRemovePower(player.getGameProfile().getId(), amount); }
    
    public int transferPower(UUID fromPlayer, UUID toPlayer, int amount)
    {}
    
    public int transferPower(EntityPlayer fromPlayer, EntityPlayer toPlayer, int amount)
    {}
    
    public int save()
    {}
    
    public int load()
    {}
}