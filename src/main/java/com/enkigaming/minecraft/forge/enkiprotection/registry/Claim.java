package com.enkigaming.minecraft.forge.enkiprotection.registry;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.chunk.Chunk;

public class Claim
{
    public Claim(ClaimRegistry registry, String name)
    {}
    
    public static Claim fromString(ClaimRegistry registry, String encodedClaim)
    {}
    
    UUID claimID;
    String claimName;
    
    ClaimRegistry registry;
    
    UUID owner;
    Collection<UUID> members; // Able to manage the claim, modify it, add to it, etc. but can't add new members.
    Collection<UUID> allies; // Able to enter, break blocks, right-click, etc.
    Collection<UUID> banned; // Not able to enter the area
    
    String welcomeMessage;
    
    boolean allowExplosions;
    boolean allowFriendlyCombat;
    boolean allowPlayerCombat;
    boolean allowCombat;
    boolean allowNonAllyEntry;
    boolean allowNonAllyRightClick;
    boolean allowNonAllyBreakOrPlaceBlocks;
    
    /**
     * Map of player UUIDs to how much power they have granted the claim. 
     */
    Map<UUID, Integer> claimPowerGrants;
    
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
    
    public boolean canRightClickIn(UUID rightClickingPlayer)
    {}
    
    public boolean canRightClickIn(EntityPlayer rightClickingPlayer)
    {}
    
    public boolean canBreakOrPlaceBlocks(UUID placingOrBreakingPlayer)
    {}
    
    public boolean canBreakOrPlaceBlocks(EntityPlayer placingOrBreakingPlayer)
    {}
    
    public ClaimRegistry getRegistry()
    {}
    
    public Collection<ChunkCoOrdinate> getChunks()
    {}
    
    @Override
    public String toString()
    {}
}