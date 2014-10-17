package com.enkigaming.minecraft.forge.enkiprotection.claim;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import net.minecraft.entity.player.EntityPlayer;

public class ClaimPlayers
{
    protected static enum PlayerRelation
    {
        member,
        ally,
        bannedfrom
    }
    
    public ClaimPlayers()
    {  }
    
    protected UUID ownerId; // Synchronise with players
    protected final Map<UUID, PlayerRelation> players = new HashMap<UUID, PlayerRelation>();
    
    public UUID getOwnerId()
    {
        synchronized(players)
        { return ownerId; }
    }
    
    public Collection<UUID> getMembers()
    {
        Collection<UUID> members = new HashSet<UUID>();
        
        synchronized(players)
        {
            for(Entry<UUID, PlayerRelation> entry : players.entrySet())
                if(entry.getValue().equals(PlayerRelation.member))
                    members.add(entry.getKey());
        }
        
        return members;
    }
    
    public Collection<UUID> getAllies()
    {
        Collection<UUID> allies = new HashSet<UUID>();
        
        synchronized(players)
        {
            for(Entry<UUID, PlayerRelation> entry : players.entrySet())
                if(entry.getValue().equals(PlayerRelation.ally))
                    allies.add(entry.getKey());
        }
        
        return allies;
    }
    
    public Collection<UUID> getBannedPlayes()
    {
        Collection<UUID> banned = new HashSet<UUID>();
        
        synchronized(players)
        {
            for(Entry<UUID, PlayerRelation> entry : players.entrySet())
                if(entry.getValue().equals(PlayerRelation.bannedfrom))
                    banned.add(entry.getKey());
        }
        
        return banned;
    }
    
    public boolean isOwner(UUID playerId)
    {
        synchronized(players)
        { return ownerId.equals(playerId); }
    }
    
    public boolean isOwner(EntityPlayer player)
    {
        checkNull(player);
        return isOwner(player.getGameProfile().getId());
    }
    
    public boolean isMember(UUID playerId)
    {
        PlayerRelation relation;
        
        synchronized(players)
        { relation = players.get(playerId); }
        
        return relation == PlayerRelation.member;
    }
    
    public boolean isMember(EntityPlayer player)
    {
        checkNull(player);
        return isMember(player.getGameProfile().getId());
    }
    
    public boolean isMemberOrBetter(UUID playerId)
    {
        synchronized(players)
        {
            if(isOwner(playerId))
                return true;
            
            if(isMember(playerId))
                return true;
            
            return false;
        }
    }
    
    public boolean isMemberOrBetter(EntityPlayer player)
    {
        checkNull(player);
        return isMemberOrBetter(player.getGameProfile().getId());
    }
    
    public boolean isAlly(UUID playerId)
    {
        PlayerRelation relation;
        
        synchronized(players)
        { relation = players.get(playerId); }
        
        return relation == PlayerRelation.ally;
    }
    
    public boolean isAlly(EntityPlayer player)
    {
        checkNull(player);
        return isAlly(player.getGameProfile().getId());
    }
    
    public boolean isAllyOrBetter(UUID playerId)
    {
        synchronized(players)
        {
            if(isOwner(playerId))
                return true;
            
            if(isMember(playerId))
                return true;
            
            if(isAlly(playerId))
                return true;
            
            return false;
        }
    }
    
    public boolean isAllyOrBetter(EntityPlayer player)
    {
        checkNull(player);
        return isAllyOrBetter(player.getGameProfile().getId());
    }
    
    public boolean areBothAllyOrBetter(UUID player1, UUID player2)
    {
        synchronized(players)
        { return isAllyOrBetter(player1) && isAllyOrBetter(player2); }
    }
    
    public boolean isNotRelatedTo(UUID playerId)
    {
        PlayerRelation relation;
        
        synchronized(players)
        { relation = players.get(playerId); }
        
        return relation == null;
    }
    
    public boolean isNotRelatedTo(EntityPlayer player)
    {
        checkNull(player);
        return isNotRelatedTo(player.getGameProfile().getId());
    }
    
    public boolean isNotRelatedToOrBetter(UUID playerId)
    {
        synchronized(players)
        {
            if(isOwner(playerId))
                return true;
            
            if(isMember(playerId))
                return true;
            
            if(isAlly(playerId))
                return true;
            
            if(isNotRelatedTo(playerId))
                return true;
            
            return false;
        }
    }
    
    public boolean isNotRelatedToOrBetter(EntityPlayer player)
    {
        checkNull(player);
        return isNotRelatedToOrBetter(player.getGameProfile().getId());
    }
    
    public boolean isBannedFrom(UUID playerId)
    {
        PlayerRelation relation;
        
        synchronized(players)
        { relation = players.get(playerId); }
        
        return relation == PlayerRelation.bannedfrom;
    }
    
    public boolean isBannedFrom(EntityPlayer player)
    {
        checkNull(player);
        return isBannedFrom(player.getGameProfile().getId());
    }
    
    /**
     * Sets the claim owner.
     * @param playerId The ID of the new owner.
     * @return The ID of the old owner.
     */
    public UUID setOwner(UUID playerId)
    {
        checkNull(playerId);
        
        synchronized(players)
        {
            UUID old = ownerId;
            ownerId = playerId;
            return old;
        }
    }
    
    /**
     * Sets the claim owner.
     * @param player The new owner.
     * @return The ID of the old owner.
     */
    public UUID setOwner(EntityPlayer player)
    {
        checkNull(player);
        return setOwner(player.getGameProfile().getId());
    }
    
    public void makePlayerMember(UUID playerId)
    {
        checkNull(playerId);
        
        synchronized(players)
        { players.put(playerId, PlayerRelation.member); }
    }
    
    public void makePlayerMember(EntityPlayer player)
    {
        checkNull(player);
        makePlayerMember(player.getGameProfile().getId());
    }
    
    public void makePlayerAlly(UUID playerId)
    {
        checkNull(playerId);
        
        synchronized(players)
        { players.put(playerId, PlayerRelation.member); }
    }
    
    public void makePlayerAlly(EntityPlayer player)
    {
        checkNull(player);
        makePlayerAlly(player.getGameProfile().getId());
    }
    
    public void makePlayerNonMember(UUID playerId)
    {
        checkNull(playerId);
        
        synchronized(players)
        { players.remove(playerId); }
    }
    
    public void makePlayerNonMember(EntityPlayer player)
    {
        checkNull(player);
        makePlayerNonMember(player.getGameProfile().getId());
    }
    
    public void banPlayer(UUID playerId)
    {
        checkNull(playerId);
        
        synchronized(players)
        { players.put(playerId, PlayerRelation.bannedfrom); }
    }
    
    public void banPlayer(EntityPlayer player)
    {
        checkNull(player);
        banPlayer(player.getGameProfile().getId());
    }
    
    protected void checkNull(Object obj)
    {
        if(obj == null)
            throw new IllegalArgumentException("Argument may not be null.");
    }
}