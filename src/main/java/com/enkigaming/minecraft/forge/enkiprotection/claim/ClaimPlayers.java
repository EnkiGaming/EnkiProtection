package com.enkigaming.minecraft.forge.enkiprotection.claim;

import com.enkigaming.mcforge.enkilib.EnkiLib;
import com.enkigaming.mcforge.enkilib.exceptions.UnableToParseTreeNodeException;
import com.enkigaming.mcforge.enkilib.filehandling.TreeFileHandler.TreeNode;
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
    
    public ClaimPlayers(UUID ownerId)
    { this.ownerId = ownerId; }
    
    public ClaimPlayers()
    { this.ownerId = null; }
    
    public ClaimPlayers(TreeNode treeNode) throws UnableToParseTreeNodeException
    {
        this();
        UUID ownerId = null;
        
        SearchForOwnerId:
        for(TreeNode node : treeNode.getChildren())
            if(node.getName().equalsIgnoreCase(ownerTag))
            {
                if(node.getChildren().size() <= 0)
                    throw new UnableToParseTreeNodeException(treeNode, "Does not contain any entries for 'Owner'");
                
                try
                { ownerId = getUUIDFromNameString(node.getChildren().get(0).getName()); }
                catch(IllegalArgumentException exception)
                { throw new UnableToParseTreeNodeException(treeNode, "Owner player id does not parse to a valid UUID"); }
                
                break SearchForOwnerId;
            }
        
        if(ownerId == null)
            throw new UnableToParseTreeNodeException(treeNode, "ClaimPlayer objects must have an assigned owner.");
        
        this.ownerId = ownerId;
        UUID playerId = null;
        
        for(TreeNode node : treeNode.getChildren())
        {
            if(node.getName().equalsIgnoreCase(membersTag))
            {
                for(TreeNode memberNode : node.getChildren())
                {
                    try
                    {
                        playerId = getUUIDFromNameString(memberNode.getName());
                        players.put(playerId, PlayerRelation.member);
                    }
                    catch(IllegalArgumentException exception)
                    { }
                }
            }
            else if(node.getName().equalsIgnoreCase(alliesTag))
            {
                for(TreeNode allyNode : node.getChildren())
                {
                    try
                    {
                        playerId = getUUIDFromNameString(allyNode.getName());
                        players.put(playerId, PlayerRelation.ally);
                    }
                    catch(IllegalArgumentException exception)
                    { }
                }
            }
            else if(node.getName().equalsIgnoreCase(bannedTag))
            {
                for(TreeNode bannedNode : node.getChildren())
                {
                    try
                    {
                        playerId = getUUIDFromNameString(bannedNode.getName());
                        players.put(playerId, PlayerRelation.bannedfrom);
                    }
                    catch(IllegalArgumentException exception)
                    { }
                }
            }
        }
    }
    
    protected UUID ownerId; // Synchronise with players
    protected final Map<UUID, PlayerRelation> players = new HashMap<UUID, PlayerRelation>();
    
    protected static final String playersTag = "Players";
    protected static final String ownerTag = "Owner";
    protected static final String membersTag = "Members";
    protected static final String alliesTag = "Allies";
    protected static final String bannedTag = "Banned Players";
    protected static final String separator = ": ";
    
    public TreeNode toTreeNode()
    {
        TreeNode baseNode = new TreeNode(playersTag + separator);
        
        TreeNode ownerNode = new TreeNode(ownerTag + separator);
        baseNode.addChild(ownerNode);
        
        String name = EnkiLib.getInstance().getUsernameCache().getLastRecordedNameOf(ownerId);
        
        if(name != null)
            ownerNode.addChild(new TreeNode(name + separator + ownerId.toString()));
        else
            ownerNode.addChild(new TreeNode(ownerId.toString()));
        
        TreeNode membersNode = new TreeNode(membersTag + separator);
        TreeNode alliesNode = new TreeNode(alliesTag + separator);
        TreeNode bannedNode = new TreeNode(bannedTag + separator);
        
        for(Entry<UUID, PlayerRelation> entry : players.entrySet())
        {
            name = EnkiLib.getInstance().getUsernameCache().getLastRecordedNameOf(entry.getKey());
            String playerString = "";
            
            if(name != null)
                playerString += name + separator;
            
            playerString += entry.getKey().toString();
            TreeNode playerNode = new TreeNode(playerString);
            
            switch(entry.getValue())
            {
                case member:     membersNode.addChild(playerNode); break;
                case ally:       alliesNode .addChild(playerNode); break;
                case bannedfrom: bannedNode .addChild(playerNode); break;
            }
        }
        
        return baseNode;
    }
    
    public static ClaimPlayers fromTreeNode(TreeNode treeNode) throws UnableToParseTreeNodeException
    { return new ClaimPlayers(treeNode); }
    
    protected static UUID getUUIDFromNameString(String nameString) throws IllegalArgumentException
    {
        String uuidString = nameString;
        String[] parts = uuidString.split("\\Q" + separator + "\\E");
        
        if(parts.length > 1)
            uuidString = parts[parts.length - 1];
        
        return UUID.fromString(uuidString);
    }
    
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
    
    public Collection<UUID> getBannedPlayers()
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