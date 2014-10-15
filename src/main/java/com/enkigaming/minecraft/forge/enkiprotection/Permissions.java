package com.enkigaming.minecraft.forge.enkiprotection;

import com.enkigaming.mcforge.enkipermissions.EnkiPerms;
import com.enkigaming.minecraft.forge.enkiprotection.utils.Utils;
import cpw.mods.fml.common.Loader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.UUID;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;

public class Permissions
{
    public Permissions()
    {
        String[] defaultPerms =
        {
            "enkiprotection.power.grant",
            "enkiprotection.power.revoke",
            "enkiprotection.claim.create",
            "enkiprotection.claim.own.delete",
            "enkiprotection.claim.own.invite",
            "enkiprotection.claim.join",
            "enkiprotection.claim.ally",
            "enkiprotection.claim.own.grantownership",
            "enkiprotection.claim.chunk.own.add",
            "enkiprotection.claim.chunk.member.add",
            "enkiprotection.claim.chunk.autoadd",
            "enkiprotection.claim.chunk.member.remove",
            "enkiprotection.claim.chunk.autoremove",
            "enkiprotection.claim.setting.member.welcomemessage",
            "enkiprotection.claim.setting.member.allowexplosions",
            "enkiprotection.claim.setting.member.allowfriendlycombat",
            "enkiprotection.claim.setting.member.allownonallyentry",
            "enkiprotection.claim.setting.member.allowinteractwithblocks",
            "enkiprotection.claim.setting.member.allowinteractwithentities",
            "enkiprotection.claim.setting.member.allowbreakorplaceblocks"
        };
        
        defaultPermissions.addAll(Arrays.asList(defaultPerms));
    }
    
    final Collection<String> defaultPermissions = new ArrayList<String>();
    
    public boolean playerHasPermission(UUID playerId, String permission)
    {
        if(Loader.isModLoaded("EnkiPerms"))
            return checkPermissionsMod(playerId, permission);
        else
        {
            EntityPlayer player = null;
            
            SearchForPlayerWithId:
            for(EntityPlayer current : new ArrayList<EntityPlayer>(MinecraftServer.getServer().getConfigurationManager().playerEntityList))
            {
                if(current.getGameProfile().getId().equals(playerId))
                {
                    player = current;
                    break SearchForPlayerWithId;
                }
            }
            
            if(Utils.playerIsOp(player.getGameProfile()))
                return true;
            
            for(String defaultPermission : defaultPermissions)
                if(defaultPermission.equalsIgnoreCase(permission))
                    return true;
            
            return false;
        }
    }
    
    public boolean playerHasPermission(EntityPlayer player, String permission)
    {
        if(Loader.isModLoaded("EnkiPerms"))
            return checkPermissionsMod(player.getGameProfile().getId(), permission);
        else
        {
            if(Utils.playerIsOp(player.getGameProfile()))
                return true;
            
            for(String defaultPermission : defaultPermissions)
                if(defaultPermission.equalsIgnoreCase(permission))
                    return true;
            
            return false;
        }
    }
    
    protected boolean checkPermissionsMod(UUID playerId, String permission)
    { return EnkiPerms.hasPermission(playerId, permission); }
}
