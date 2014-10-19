package com.enkigaming.minecraft.forge.enkiprotection.claim;

import com.enkigaming.minecraft.forge.enkiprotection.EnkiProtection;
import com.enkigaming.minecraft.forge.enkiprotection.Permissions;
import com.enkigaming.minecraft.forge.enkiprotection.registry.ClaimRegistry;
import com.enkigaming.minecraft.forge.enkiprotection.registry.exceptions.ChunkAlreadyClaimedException;
import com.enkigaming.minecraft.forge.enkiprotection.registry.exceptions.NotEnoughClaimPowerToClaimException;
import com.enkigaming.minecraft.forge.enkiprotection.utils.ChunkCoOrdinate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.chunk.Chunk;

public class Claim
{
    public Claim(UUID id, String name, ClaimRegistry registry)
    {
        claimId = id;
        this.name = name;
        this.registry = registry;
    }
    
    protected final UUID claimId;
    protected String name;
    
    protected final ClaimRegistry registry;
    
    protected final ClaimSettings settings = new ClaimSettings();
    protected final ClaimPlayers players = new ClaimPlayers();
    protected final ClaimPower power = makeClaimPower();
    protected final Set<ChunkCoOrdinate> chunks = new HashSet<ChunkCoOrdinate>();
    
    protected final Object nameLock = new Object();
    
    public ClaimPower makeClaimPower()
    {
        return new ClaimPower()
        {
            @Override
            public void onForceRevoke(UUID playerRevoking, int amountBeingRevoked, int amountMoreThanAvailablePowerBeingRevoked)
            {
                for(int i = 0; i < amountMoreThanAvailablePowerBeingRevoked; i++)
                {
                    synchronized(chunks)
                    {
                        if(chunks.isEmpty())
                            throw new IllegalStateException("Something strange happened. There was more power to give" +
                                    "back by unclaiming chunks and giving the power back than there were chunks.");
                        
                        ChunkCoOrdinate toLose = getChunkFurthestAwayFromMiddle();
                        chunks.remove(toLose);
                    }
                }
            }

            @Override
            public int getPowerUsed()
            {
                synchronized(chunks)
                { return chunks.size(); }
            }

            @Override
            public void notifyPlayerPowerOfRevocation(UUID playerId, int amount)
            { EnkiProtection.getInstance().getPowerRegistry().getForPlayer(playerId).notifyOfPowerGrantReturn(Claim.this, amount); }
        };
    }
    
    public UUID getId()
    { return claimId; }
    
    public String getName()
    {
        synchronized(nameLock)
        { return name; }
    }
    
    public ClaimRegistry getRelatedRegistry()
    { return registry; }
    
    public ClaimSettings getSettings()
    { return settings; }
    
    public ClaimPlayers getPlayerManager()
    { return players; }
    
    public ClaimPower getPowerManager()
    { return power; }
    
    public Collection<ChunkCoOrdinate> getChunks()
    {
        synchronized(chunks)
        { return new ArrayList<ChunkCoOrdinate>(chunks); }
    }
    
    public boolean hasChunk(ChunkCoOrdinate chunk)
    {
        synchronized(chunks)
        { return chunks.contains(chunk); }
    }
    
    public boolean chunkIsInClaim(ChunkCoOrdinate chunk)
    {
        synchronized(chunks)
        { return chunks.contains(chunk); }
    }
    
    public boolean chunkIsInClaim(Chunk chunk)
    { return chunkIsInClaim(new ChunkCoOrdinate(chunk)); }
    
    public ChunkCoOrdinate getChunkFurthestAwayFromMiddle()
    {
        synchronized(chunks)
        {
            if(chunks.isEmpty())
                return null;
            
            double chunkAverageX = 0;
            double chunkAverageZ = 0;
            int i = 1;
            
            for(ChunkCoOrdinate chunk : chunks)
            {
                chunkAverageX += (chunk.getXCoOrd() - chunkAverageX) / i;
                chunkAverageZ += (chunk.getZCoOrd() - chunkAverageZ) / i;
            }
            
            ChunkCoOrdinate currentFurthest = null;
            double currentDistanceSq = 0;
            
            for(ChunkCoOrdinate chunk : chunks)
            {
                double xPart = chunk.getXCoOrd() - chunkAverageX;
                double zPart = chunk.getZCoOrd() - chunkAverageZ;
                
                double distanceSq = (xPart * xPart) + (zPart * zPart);
                
                if(distanceSq > currentDistanceSq)
                {
                    currentFurthest = chunk;
                    currentDistanceSq = distanceSq;
                }
            }
            
            return currentFurthest;
        }
    }
    
    public String setName(String newName)
    {
        synchronized(nameLock)
        {
            String old = name;
            name = newName;
            return old;
        }
    }
    
    public void claimChunk(ChunkCoOrdinate chunk) throws ChunkAlreadyClaimedException, NotEnoughClaimPowerToClaimException
    {
        int availablePower = power.getAvailablePower();
        
        if(availablePower < 1)
            throw new NotEnoughClaimPowerToClaimException(availablePower, 1, chunk);
        
        Claim alreadyBelongsTo = registry.getClaimAtChunk(chunk);
        
        if(alreadyBelongsTo != null)
            throw new ChunkAlreadyClaimedException(alreadyBelongsTo, chunk);
        
        synchronized(chunks)
        { chunks.add(chunk); }
    }
    
    public void claimChunk(Chunk chunk) throws ChunkAlreadyClaimedException, NotEnoughClaimPowerToClaimException
    { claimChunk(new ChunkCoOrdinate(chunk)); }
    
    public boolean unclaimChunk(ChunkCoOrdinate chunk)
    {
        synchronized(chunks)
        { return chunks.remove(chunk); }
    }
    
    public boolean unclaimChunk(Chunk chunk)
    { return unclaimChunk(new ChunkCoOrdinate(chunk)); }
    
    // ========== Convenience methods ==========
    
    public boolean canFight(UUID attackingPlayerId, UUID playerBeingAttackedId)
    {
        if(!(settings.playerCombatIsAllowed()
          || Permissions.hasPermission(attackingPlayerId, "enkiprotection.claim.bypass.allowplayercombat")))
            return false;
        
        if(!((settings.friendlyCombatIsAllowed() && players.areBothAllyOrBetter(attackingPlayerId, playerBeingAttackedId))
          || Permissions.hasPermission(attackingPlayerId, "enkiprotection.claim.bypass.allowfriendlycombat")))
            return false;
        
        return true;
    }
    
    public boolean canFight(EntityPlayer attackingPlayer, EntityPlayer playerBeingAttacked)
    { return canFight(attackingPlayer.getGameProfile().getId(), playerBeingAttacked.getGameProfile().getId()); }
    
    public boolean canEnter(UUID playerId)
    {
        if(Permissions.hasPermission(playerId, "enkiprotection.claim.bypass.allownonallyentry"))
            return true;
        
        if(players.isBannedFrom(playerId))
            return false;
                
        if(settings.nonAlliesAreAllowedIn())
            return true;
        
        if(players.isAllyOrBetter(playerId))
            return true;
        
        return false;
    }
    
    public boolean canEnter(EntityPlayer player)
    { return canEnter(player.getGameProfile().getId()); }
    
    public boolean canInteractWithBlocksIn(UUID playerId)
    {
        if(Permissions.hasPermission(playerId, "enkiprotection.claim.bypass.allowinteractwithblocks"))
            return true;
        
        if(players.isAllyOrBetter(playerId))
            return true;
        
        if(players.isBannedFrom(playerId))
            return false;
        
        if(settings.nonAlliesCanInteractWithBlocks())
            return true;
        
        return false;
    }
    
    public boolean canInteractWithBlocksIn(EntityPlayer player)
    { return canInteractWithBlocksIn(player.getGameProfile().getId()); }
    
    public boolean canInteractWithEntitiesIn(UUID playerId)
    {
        if(Permissions.hasPermission(playerId, "enkiprotection.claim.bypass.allowinteractwithentities"))
            return true;
        
        if(players.isAllyOrBetter(playerId))
            return true;
        
        if(players.isBannedFrom(playerId))
            return false;
        
        if(settings.nonAlliesCanInteractWithEntities())
            return true;
        
        return false;
    }
    
    public boolean canInteractWithEntitiesIn(EntityPlayer player)
    { return canInteractWithBlocksIn(player.getGameProfile().getId()); }
    
    public boolean canBreakOrPlaceBlocksIn(UUID playerId)
    {
        if(Permissions.hasPermission(playerId, "enkiprotection.claim.bypass.allowbreakorplaceblocks"))
            return true;
        
        if(players.isAllyOrBetter(playerId))
            return true;
        
        if(players.isBannedFrom(playerId))
            return false;
        
        if(settings.nonAlliesCanBreakOrPlaceBlocks())
            return true;
        
        return false;
    }
    
    public boolean canBreakOrPlaceBlocksIn(EntityPlayer player)
    { return canBreakOrPlaceBlocksIn(player.getGameProfile().getId()); }
}