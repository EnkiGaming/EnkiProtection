package com.enkigaming.minecraft.forge.enkiprotection.registry;

import java.util.Collection;
import java.util.UUID;
import net.minecraft.entity.player.EntityPlayer;

public class Claim
{
    UUID claimID;
    String claimName;
    UUID owner;
    Collection<UUID> members; // Able to manage the claim, modify it, add to it, etc. but can't add new members.
    Collection<UUID> allies; // Able to enter, break blocks, etc.
    Collection<UUID> banned; // Not able to enter the area
    
    String welcomeMessage;
    
    boolean allowExplosions;
    boolean allowFriendlyCombat;
    boolean allowPlayerCombat;
    boolean allowCombat;
    boolean allowNonAllyEntry;
    
    public UUID getID()
    {}
    
    public String getName()
    {}
    
    public UUID getOwner()
    {}
    
    public Collection<UUID> getMembers()
    {}
    
    public Collection<UUID> getAllies()
    {}
    
    public Collection<UUID> getBanned()
    {}
    
    public String getWelcomeMessage()
    {}
    
    public boolean explosionsAreAllowed()
    {}
    
    public boolean membersAndAlliesAreAllowedToFight()
    {}
    
    public boolean playersAreAllowedToFight()
    {}
    
    public boolean anythingIsAllowedToFight()
    {}
    
    public boolean nonAlliesAllowedEntry()
    {}
    
    public boolean canFight(UUID attackingPlayer, UUID playerBeingAttacked)
    {}
    
    public boolean canFight(EntityPlayer attackingPlayer, EntityPlayer playerBeingAttacked)
    { return canFight(attackingPlayer.getGameProfile().getId(), playerBeingAttacked.getGameProfile().getId()); }
    
    public boolean canEnter(UUID enteringPlayer)
    {}
    
    public boolean canEnter(EntityPlayer enteringPlayer)
    { return canEnter(enteringPlayer.getGameProfile().getId()); }
}