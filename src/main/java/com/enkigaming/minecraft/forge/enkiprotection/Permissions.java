package com.enkigaming.minecraft.forge.enkiprotection;

import com.google.common.collect.Multimap;
import java.io.File;
import java.util.UUID;
import net.minecraft.entity.player.EntityPlayer;

public class Permissions
{
    public Permissions(File saveFolder)
    {}
    
    File saveFolder;
    Multimap<UUID, String> permissionsMap;
    
    public void save()
    {}
    
    public void load()
    {}
    
    public boolean playerHasPermission(UUID playerID, String permission)
    {}
    
    public boolean playerHasPermission(EntityPlayer player, String permission)
    {}
    
    public boolean givePlayerPermission(UUID playerID, String permission)
    {}
    
    public boolean givePlayerPermission(EntityPlayer player, String permission)
    {}
}
