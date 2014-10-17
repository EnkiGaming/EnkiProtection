package com.enkigaming.minecraft.forge.enkiprotection.registry.deprecated;

import com.enkigaming.minecraft.forge.enkiprotection.EnkiProtection;
import com.enkigaming.minecraft.forge.enkiprotection.registry.exceptions.ChunkAlreadyClaimedException;
import com.enkigaming.minecraft.forge.enkiprotection.registry.exceptions.NotEnoughClaimPowerToClaimException;
import com.enkigaming.minecraft.forge.enkiprotection.registry.exceptions.NotEnoughClaimPowerToRemoveException;
import com.enkigaming.minecraft.forge.enkiprotection.utils.ChunkCoOrdinate;
import com.enkigaming.minecraft.forge.enkiprotection.registry.exceptions.GrantingMoreClaimPowerThanHaveException;
import com.enkigaming.minecraft.forge.enkiprotection.registry.exceptions.RevokingMorePowerThanAvailableException;
import com.enkigaming.minecraft.forge.enkiprotection.registry.exceptions.RevokingMorePowerThanGrantedException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.chunk.Chunk;

public class Claim
{
    public static class PowerRevocation
    {
        public PowerRevocation(int amount, Date timeToForceRevoke)
        {
            this.amount = amount;
            this.timeStarted = new Date();
            this.timeToForceRevoke = timeToForceRevoke;
        }
        
        int amount;
        final Date timeStarted;
        Date timeToForceRevoke;
        
        public int setAmount(int newAmount)
        {
            int old = amount;
            amount = newAmount;
            return old;
        }
        
        public Date setTimeToForceRevoke(Date newTime)
        {
            Date old = timeToForceRevoke;
            timeToForceRevoke = new Date(newTime.getTime());
            return old;
        }
        
        public int getAmount()
        { return amount; }
        
        public Date getTimeStarted()
        { return new Date(timeStarted.getTime()); }
        
        public Date getTimeToForceRevoke()
        { return new Date(timeToForceRevoke.getTime()); }
    }
    
    public Claim(ClaimRegistry registry, String name, UUID id)
    {}
    
    public static Claim fromString(ClaimRegistry registry, String encodedClaim)
    {}
    
    final UUID claimId;
    String claimName;
    
    final ClaimRegistry registry;
    final Set<ChunkCoOrdinate> chunks = new HashSet<ChunkCoOrdinate>(); // doubly-referenced to optimise and avoid deadlocks.
    
    UUID owner;
    final Collection<UUID> members = new ArrayList<UUID>(); // Able to manage the claim, modify it, add to it, etc. but can't add new members.
    final Collection<UUID> allies = new ArrayList<UUID>(); // Able to enter, break blocks, right-click, etc.
    final Collection<UUID> banned = new ArrayList<UUID>(); // Not able to enter the area
    
    String welcomeMessage;
    
    boolean allowExplosions;
    boolean allowFriendlyCombat;
    boolean allowPlayerCombat;
    boolean allowMobEntry; // Should allow friendly mobs regardless.
    boolean allowNonAllyEntry;
    boolean allowNonAllyInteractWithBlocks;
    boolean allowNonAllyInteractWithEntities;
    boolean allowNonAllyBreakOrPlaceBlocks;
    
    /**
     * Map of player UUIDs to how much power they have granted the claim. 
     */
    Map<UUID, Integer> claimPowerGrants;
    Map<UUID, PowerRevocation> currentlyRevoking;
    // in-progress revokations, caused by players revoking from claims that would result in their remaining power being
    // below 0.
    
    final protected Object nameLock = new Object();
    final protected Object membersAlliesOwnerBannedLock = new Object();
    final protected Object welcomeMessageLock = new Object();
    final protected Object powerLock = new Object();
    
    public UUID getId()
    { return claimId; }
    
    public String getName()
    {
        synchronized(nameLock)
        { return claimName; }
    }
    
    public UUID getOwner()
    {
        synchronized(membersAlliesOwnerBannedLock)
        { return owner; }
    }
    
    public Collection<UUID> getMembers()
    {
        synchronized(membersAlliesOwnerBannedLock)
        { return new ArrayList<UUID>(members); }
    }
    
    public Collection<UUID> getAllies()
    {
        synchronized(membersAlliesOwnerBannedLock)
        { return new ArrayList<UUID>(allies); }
    }
    
    public Collection<UUID> getBannedPlayers()
    {
        synchronized(membersAlliesOwnerBannedLock)
        { return new ArrayList<UUID>(banned); }
    }
    
    public boolean isOwner(UUID player)
    {
        synchronized(membersAlliesOwnerBannedLock)
        { return owner.equals(player); }
    }
    
    public boolean isOwner(EntityPlayer player)
    { return isOwner(player.getGameProfile().getId()); }
    
    public boolean isMember(UUID player)
    {
        synchronized(membersAlliesOwnerBannedLock)
        { return members.contains(player); }
    }
    
    public boolean isMember(EntityPlayer player)
    { return isMember(player.getGameProfile().getId()); }
    
    public boolean isMemberOrBetter(UUID player)
    {
        synchronized(membersAlliesOwnerBannedLock)
        {
            if(owner.equals(player))
                return true;
            
            if(members.contains(player))
                return true;
            
            return false;
        }
    }
    
    public boolean isMemberOrBetter(EntityPlayer player)
    { return isMemberOrBetter(player.getGameProfile().getId()); }
    
    public boolean isAlly(UUID player)
    {
        synchronized(membersAlliesOwnerBannedLock)
        { return allies.contains(player); }
    }
    
    public boolean isAlly(EntityPlayer player)
    { return isAlly(player.getGameProfile().getId()); }
    
    public boolean isAllyOrBetter(UUID player)
    {
        synchronized(membersAlliesOwnerBannedLock)
        {
            if(owner.equals(player))
                return true;
            
            if(members.contains(player))
                return true;
            
            if(allies.contains(player))
                return true;
            
            return false;
        }
    }
    
    public boolean isAllyOrBetter(EntityPlayer player)
    { return isAllyOrBetter(player.getGameProfile().getId()); }
    
    public boolean isBannedFrom(UUID player)
    {
        synchronized(membersAlliesOwnerBannedLock)
        { return banned.contains(player); }
    }
    
    public boolean isBannedFrom(EntityPlayer player)
    { return isBannedFrom(player.getGameProfile().getId()); }
    
    public String getWelcomeMessage()
    {
        synchronized(welcomeMessageLock)
        { return welcomeMessage; }
    }
    
    public ClaimRegistry getRegistry()
    { return registry; }
    
    public Collection<ChunkCoOrdinate> getChunks()
    {
        synchronized(chunks)
        { return new ArrayList<ChunkCoOrdinate>(chunks); }
    }
    
    public void addChunk(ChunkCoOrdinate chunk) throws NotEnoughClaimPowerToClaimException, ChunkAlreadyClaimedException
    { registry.claimChunk(this, chunk); }
    
    public void addChunk(Chunk chunk) throws NotEnoughClaimPowerToClaimException, ChunkAlreadyClaimedException
    { addChunk(new ChunkCoOrdinate(chunk)); }
    
    boolean addChunkOnlyInClaim(ChunkCoOrdinate chunk)
    {
        synchronized(chunks)
        { return chunks.add(chunk); }
    }
    
    boolean addChunkOnlyInClaim(Chunk chunk)
    { return addChunkOnlyInClaim(new ChunkCoOrdinate(chunk)); }
    
    boolean removeChunkOnlyInClaim(ChunkCoOrdinate chunk)
    {
        synchronized(chunks)
        { return chunks.remove(chunk); }
    }
    
    boolean removechunkOnlyInClaim(Chunk chunk)
    { return removeChunkOnlyInClaim(new ChunkCoOrdinate(chunk)); }
    
    public boolean removeChunk(ChunkCoOrdinate chunk)
    { return registry.unclaimChunk(this, chunk); }
    
    public boolean removeChunk(Chunk chunk)
    { return removeChunk(new ChunkCoOrdinate(chunk)); }
    
    public int getTotalPower()
    {
        synchronized(powerLock)
        {
            int power = 0;
            
            for(Integer i : claimPowerGrants.values())
                power += i;
            
            return power;
        }
    }
    
    public int getRemainingPower()
    {
        int powerUsed;
        
        synchronized(chunks)
        { powerUsed = chunks.size(); }
        
        return getTotalPower() - powerUsed;
    }
    
    public int getPowerPlayerHasGranted(UUID playerId)
    {
        synchronized(powerLock)
        { Integer powerGranted = claimPowerGrants.get(playerId); 
        
        if(powerGranted == null)
            powerGranted = 0;
            
        return powerGranted;}
    }
    
    public Map<UUID, Integer> getPowerPlayersHaveGranted()
    {
        synchronized(powerLock)
        { return new HashMap<UUID, Integer>(claimPowerGrants); }
    }
    
    public void grantPower(UUID playerGranting, int amount) throws GrantingMoreClaimPowerThanHaveException
    {
        try
        {
            EnkiProtection.getInstance().getPowerRegistry().removePower(playerGranting, amount);
        }
        catch(NotEnoughClaimPowerToRemoveException ex)
        {
            throw new GrantingMoreClaimPowerThanHaveException(playerGranting, ex.getAmountPlayerHadAvailable(), amount, )
        }
    }
    
    public void grantPower(EntityPlayer playerGranting, int amount) throws GrantingMoreClaimPowerThanHaveException
    {}
    
    // Where a player revokes power in a way that would leave a claim with less than 0 remaining power, the player
    // revoking power and all members should be notified. Until the power is successfully revoked, new chunks can not be
    // added to the claim and every time chunks are removed from the claim, the power will be successfully revoked if
    // getRemainingPower returns 0 or above. If not enough claims are removed removing the power used that the player
    // wants to revoke after 5 days, the player will get the power back and the equivalent number of chunks will be
    // unclaimed from the claim.
    // 
    // I think the ideal way to establish which chunk is removed each iteration until enough have been removed is to
    // average out the coördinates of all claims in a world and remove the chunk furthest away from the average/middle.
    // If claims don't have to be in consecutive chunks, then start with a chunk that's the furthest away from its
    // world's average for chunks of that claim, regardless of world.
    //
    // Players may change the amount they revoke. If they increase it, the increase is added as a second revoke that
    // then takes the full 5 days.
    //
    // Checks for whether the time has run out should run on server start-up, and then once every hour thenceforth.
    
    public boolean revokePower(UUID playerRevoking, int amount) throws RevokingMorePowerThanGrantedException,
                                                                       RevokingMorePowerThanAvailableException
    {}
    
    public boolean revokePower(EntityPlayer playerRevoking, int amount) throws RevokingMorePowerThanGrantedException,
                                                                               RevokingMorePowerThanAvailableException
    {}
    
    public boolean queueRevocation(UUID playerRevoking, int amount) // default to 120 hours (5 days)
    {}
    
    public boolean queueRevocation(EntityPlayer playerRevoking, int amount) // default to 120 hours (5 days)
    {}
    
    public boolean queueRevocation(UUID playerRevoking, int amount, int inNumberOfHours)
    {}
    
    public boolean queueRevocation(EntityPlayer playerRevoking, int amount, int inNumberOfHours)
    {}
    
    public boolean changeRevokingAmount(UUID playerRevoking, int amount)
    {}
    
    public boolean changeRevokingAmount(EntityPlayer playerRevoking, int amount)
    {}
    
    public boolean explosionsAreAllowed()
    {}
    
    public boolean mobsCanEnter()
    {}
    
    public boolean membersAndAlliesAreAllowedToFight()
    {}
    
    public boolean playersAreAllowedToFight()
    {}
    
    public boolean nonAlliesAllowedEntry()
    {}
    
    public boolean nonAlliesCanInteractWithBlocks()
    {}
    
    public boolean nonAlliesCanInteractWithEntities()
    {}
    
    // public boolean mobCanEnter(???) < < < Implement this. Need to check what I need to check for.
    // {}
    
    public boolean canFight(UUID attackingPlayer, UUID playerBeingAttacked)
    {}
    
    public boolean canFight(EntityPlayer attackingPlayer, EntityPlayer playerBeingAttacked)
    { return canFight(attackingPlayer.getGameProfile().getId(), playerBeingAttacked.getGameProfile().getId()); }
    
    public boolean canEnter(UUID enteringPlayer)
    {}
    
    public boolean canEnter(EntityPlayer enteringPlayer)
    { return canEnter(enteringPlayer.getGameProfile().getId()); }
    
    public boolean canInteractWithBlocksIn(UUID rightClickingPlayer)
    {}
    
    public boolean canInteractWithBlocksIn(EntityPlayer rightClickingPlayer)
    {}
    
    public boolean canInteractWithEntitiesIn(UUID rightClickingPlayer)
    {}
    
    public boolean canInteractWithEntitiesIn(EntityPlayer rightClickingPlayer)
    {}
    
    public boolean canBreakOrPlaceBlocks(UUID placingOrBreakingPlayer)
    {}
    
    public boolean canBreakOrPlaceBlocks(EntityPlayer placingOrBreakingPlayer)
    {}
    
    public String setName(String newName)
    {}
    
    public UUID setOwner(UUID newOwner)
    {}
    
    public boolean addMember(UUID newMember)
    {}
    
    public boolean addAlly(UUID newAlly)
    {}
    
    public boolean banPlayer(UUID newlyBanned)
    {}
    
    public boolean removeMember(UUID member)
    {}
    
    public boolean removeAlly(UUID ally)
    {}
    
    public boolean unbanPlayer(UUID bannedPlayer)
    {}
    
    public String setWelcomeMessage(String newMessage)
    {}
    
    public boolean setAllowExplosions(boolean newValue)
    {}
    
    public boolean setAllowFriendlyCombat(boolean newValue)
    {}
    
    public boolean setAllowPlayerCombat(boolean newValue)
    {}
    
    public boolean setAllowCombat(boolean newValue)
    {}
    
    public boolean setAllowNonAllyEntry(boolean newValue)
    {}
    
    public boolean setAllowNonAllyRightClick(boolean newValue)
    {}
    
    public boolean setAllowNonAllyPlaceOrBreakBlocks(boolean newValue)
    {}
    
    @Override
    public String toString()
    {}
}