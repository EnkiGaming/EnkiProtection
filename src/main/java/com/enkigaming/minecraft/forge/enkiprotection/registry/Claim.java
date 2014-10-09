package com.enkigaming.minecraft.forge.enkiprotection.registry;

import com.enkigaming.minecraft.forge.enkiprotection.utils.ChunkCoOrdinate;
import com.enkigaming.minecraft.forge.enkiprotection.registry.exceptions.GrantingMoreClaimPowerThanHaveException;
import com.enkigaming.minecraft.forge.enkiprotection.registry.exceptions.RevokingMorePowerThanAvailableException;
import com.enkigaming.minecraft.forge.enkiprotection.registry.exceptions.RevokingMorePowerThanGrantedException;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.UUID;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.chunk.Chunk;

public class Claim
{
    public static class PowerRevocation
    {
        public PowerRevocation(int amount, Date timeToForceRevoke)
        {}
        
        final int amount;
        final Date timeStarted;
        final Date timeToForceRevoke;
        
        public int setAmount(int newAmount)
        {}
        
        public int getAmount()
        {}
        
        public Date getTimeStarted()
        {}
        
        public Date getTimeToForceRevoke()
        {}
    }
    
    public Claim(ClaimRegistry registry, String name, UUID id)
    {}
    
    public static Claim fromString(ClaimRegistry registry, String encodedClaim)
    {}
    
    UUID claimID;
    String claimName;
    
    ClaimRegistry registry;
    Collection<ChunkCoOrdinate> chunks; // doubly-referenced to optimise and avoid deadlocks.
    
    UUID owner;
    Collection<UUID> members; // Able to manage the claim, modify it, add to it, etc. but can't add new members.
    Collection<UUID> allies; // Able to enter, break blocks, right-click, etc.
    Collection<UUID> banned; // Not able to enter the area
    
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
    
    public UUID getId()
    {}
    
    public String getName()
    {}
    
    public UUID getOwner()
    {}
    
    public Collection<UUID> getMembers()
    {}
    
    public Collection<UUID> getAllies()
    {}
    
    public Collection<UUID> getBannedPlayers()
    {}
    
    public boolean isOwner(UUID player)
    {}
    
    public boolean isOwner(EntityPlayer player)
    {}
    
    public boolean isMember(UUID player)
    {}
    
    public boolean isMember(EntityPlayer player)
    {}
    
    public boolean isMemberOrBetter(UUID player)
    {}
    
    public boolean isMemberOrBetter(EntityPlayer player)
    {}
    
    public boolean isAlly(UUID player)
    {}
    
    public boolean isAlly(EntityPlayer player)
    {}
    
    public boolean isAllyOrBetter(UUID player)
    {}
    
    public boolean isAllyOrBetter(EntityPlayer player)
    {}
    
    public boolean isBannedFrom(UUID player)
    {}
    
    public boolean isBannedFrom(EntityPlayer player)
    {}
    
    public String getWelcomeMessage()
    {}
    
    public ClaimRegistry getRegistry()
    {}
    
    public Collection<ChunkCoOrdinate> getChunks()
    {}
    
    public boolean addChunk(ChunkCoOrdinate chunk)
    {}
    
    public boolean addChunk(Chunk chunk)
    {}
    
    boolean addChunkOnlyInClaim(ChunkCoOrdinate chunk)
    {}
    
    boolean addChunkOnlyInClaim(Chunk chunk)
    {}
    
    boolean removeChunkOnlyInClaim(ChunkCoOrdinate chunk)
    {}
    
    boolean removechunkOnlyInClaim(Chunk chunk)
    {}
    
    public boolean removeChunk(ChunkCoOrdinate chunk)
    {}
    
    public boolean removeChunk(Chunk chunk)
    {}
    
    public int getTotalPower()
    {}
    
    public int getRemainingPower()
    {}
    
    public int getPowerPlayerHasGranted(UUID playerID)
    {}
    
    public Map<UUID, Integer> getPowerPlayersHaveGranted()
    {}
    
    public void grantPower(UUID playerGranting, int amount) throws GrantingMoreClaimPowerThanHaveException
    {}
    
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