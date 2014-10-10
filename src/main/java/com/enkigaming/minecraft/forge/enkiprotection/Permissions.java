package com.enkigaming.minecraft.forge.enkiprotection;

import java.util.UUID;
import net.minecraft.entity.player.EntityPlayer;

public class Permissions
{
    public Permissions()
    {}
    
    public boolean playerHasPermission(UUID playerID, String permission)
    {}
    
    public boolean playerHasPermission(EntityPlayer player, String permission)
    {}
    
    public boolean givePlayerPermission(UUID playerID, String permission)
    {}
    
    public boolean givePlayerPermission(EntityPlayer player, String permission)
    {}
    
    public boolean removePlayerPermission(UUID playerID, String permission)
    {}
    
    public boolean removePlayerPermission(EntityPlayer player, String permission)
    {}
}
