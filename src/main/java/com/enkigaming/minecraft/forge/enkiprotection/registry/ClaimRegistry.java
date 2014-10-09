package com.enkigaming.minecraft.forge.enkiprotection.registry;

import com.enkigaming.minecraft.forge.enkiprotection.utils.ChunkCoOrdinate;
import com.enkigaming.minecraft.forge.enkiprotection.registry.exceptions.ChunkAlreadyClaimedException;
import com.enkigaming.minecraft.forge.enkiprotection.registry.exceptions.NotEnoughClaimPowerToClaimException;
import com.enkigaming.minecraft.forge.enkiprotection.registry.exceptions.ChunkNotInClaimException;
import com.enkigaming.minecraft.forge.enkiprotection.registry.exceptions.ClaimIdAlreadyPresentException;
import com.enkigaming.minecraft.forge.enkiprotection.registry.exceptions.ClaimNameAlreadyPresentException;
import com.enkigaming.minecraft.forge.enkiprotection.registry.exceptions.ClaimWithIdNotPresentException;
import com.enkigaming.minecraft.forge.enkiprotection.registry.exceptions.ClaimWithNameNotPresentException;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.UUID;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

public class ClaimRegistry
{
    public ClaimRegistry(File saveFolder)
    {
        this.saveFolder = saveFolder;
    }
    
    // To do: create caches for fast-lookup of claims by name (after being looked up once) and create event for updating
    // a claim's cache entry when it changes name, when a claim is removed from the registry and anything else that
    // may invalidate a cache entry.
    
    File saveFolder;
    
    final Map<ChunkCoOrdinate, UUID> chunkClaims = new HashMap<ChunkCoOrdinate, UUID>(); // The chunk coördinates mapped to the UUID of the claim it's in.
    final Map<UUID, Claim> claims = new HashMap<UUID, Claim>(); // All claims, using someClaim.getId() as the key.
    
    final PlayerClaimPowerRegistry playerPowerRegistry = new PlayerClaimPowerRegistry(saveFolder);
    
    public PlayerClaimPowerRegistry getPlayerPowerRegistry()
    { return playerPowerRegistry; }
    
    public Collection<Claim> getClaims()
    {
        synchronized(claims)
        { return new ArrayList<Claim>(claims.values()); }
    }
    
    public Claim getClaim(UUID id)
    {
        synchronized(claims)
        { return claims.get(id); }
    }
    
    public Claim getClaim(String name)
    {
        Collection<Claim> checkingClaims;
        
        synchronized(claims)
        { checkingClaims = new ArrayList<Claim>(claims.values()); }
        
        for(Claim claim : checkingClaims)
            if(claim.getName().equalsIgnoreCase(name))
                return claim;
        
        return null;
    }
    
    public UUID getAvailableClaimId()
    {
        synchronized(claims)
        {
            for(int i = 0; i < 100; i++)
            {
                UUID id = UUID.randomUUID();
                
                if(claims.get(id) == null)
                    return id;
            }
        }
        
        throw new RuntimeException("This message should only appear if there is something seriously wrong with the generation of UUIDs.");
    }

    public boolean addClaim(Claim claim) throws ClaimIdAlreadyPresentException, ClaimNameAlreadyPresentException
    {
        synchronized(claims)
        {
            Claim matchingClaim = getClaim(claim.getId());
            
            if(matchingClaim != null)
                throw new ClaimIdAlreadyPresentException(matchingClaim, claim, matchingClaim.getId());
            
            matchingClaim = getClaim(claim.getName());
            
            if(matchingClaim != null)
                throw new ClaimNameAlreadyPresentException(matchingClaim, claim, matchingClaim.getName());
            
            claims.put(claim.getId(), claim);
            return true;
        }
    }
    
    public Claim createClaim(String claimName) throws ClaimNameAlreadyPresentException
    {
        Claim claim;
        
        synchronized(claims)
        {
            claim = getClaim(claimName);
            
            if(claim != null)
                throw new ClaimNameAlreadyPresentException(claim, null, claimName);
            
            UUID id = getAvailableClaimId();
            claim = new Claim(this, claimName, id);
            
            claims.put(id, claim);
        }
        
        return claim;
    }
    
    public Map<ChunkCoOrdinate, UUID> getChunkClaims()
    {
        synchronized(chunkClaims)
        { return new HashMap<ChunkCoOrdinate, UUID>(chunkClaims); }
    }
    
    public Collection<ChunkCoOrdinate> getClaimedChunks()
    {
        synchronized(chunkClaims)
        { return new ArrayList<ChunkCoOrdinate>(chunkClaims.keySet()); }
    }
    
    public Claim getClaimAtBlock(int blockX, int blockZ, World world)
    { return getClaimAtChunk(new ChunkCoOrdinate(blockX * 16, blockZ * 16, world.provider.dimensionId)); }
    
    public Claim getClaimAtBlock(ChunkPosition block, World world)
    { return getClaimAtBlock(block.chunkPosX, block.chunkPosZ, world); }
    
    public Claim getClaimAtChunk(int chunkX, int chunkZ, World world)
    { return getClaimAtChunk(new ChunkCoOrdinate(chunkX, chunkZ, world.provider.dimensionId)); }
    
    public Claim getClaimAtChunk(ChunkCoOrdinate chunk)
    {
        UUID claimId;
        
        synchronized(chunkClaims)
        { claimId = chunkClaims.get(chunk); }
        
        synchronized(claims)
        { return claims.get(claimId); }
    }
    
    public Claim getClaimAtChunk(Chunk chunk)
    { return getClaimAtChunk(new ChunkCoOrdinate(chunk)); }
    
    public Collection<Claim> getClaimsPlayerIsOwnerOf(UUID playerID)
    {
        Collection<Claim> checkingClaims;
        Collection<Claim> claimsPlayerIsOwnerOf = new HashSet<Claim>();
        
        synchronized(claims)
        { checkingClaims = new ArrayList<Claim>(claims.values()); }
        
        for(Claim claim : checkingClaims)
        {
            if(claim.isOwner(playerID))
                claimsPlayerIsOwnerOf.add(claim);
        }
        
        return claimsPlayerIsOwnerOf;
    }
    
    public Collection<Claim> getClaimsPlayerIsOwnerOf(EntityPlayer player)
    { return getClaimsPlayerIsOwnerOf(player.getGameProfile().getId()); }
    
    public Collection<Claim> getClaimsPlayerIsMemberOf(UUID playerID)
    {
        Collection<Claim> checkingClaims;
        Collection<Claim> claimsPlayerIsMemberOf = new HashSet<Claim>();
        
        synchronized(claims)
        { checkingClaims = new ArrayList<Claim>(claims.values()); }
        
        for(Claim claim : checkingClaims)
        {
            if(claim.isMember(playerID))
                claimsPlayerIsMemberOf.add(claim);
        }
        
        return claimsPlayerIsMemberOf;
    }
    
    public Collection<Claim> getClaimsPlayerIsMemberOf(EntityPlayer player)
    { return getClaimsPlayerIsMemberOf(player.getGameProfile().getId()); }
    
    public Collection<Claim> getClaimsPlayerIsAllyOf(UUID playerID)
    {
        Collection<Claim> checkingClaims;
        Collection<Claim> claimsPlayerIsAllyOf = new HashSet<Claim>();
        
        synchronized(claims)
        { checkingClaims = new ArrayList<Claim>(claims.values()); }
        
        for(Claim claim : checkingClaims)
        {
            if(claim.isAlly(playerID))
                claimsPlayerIsAllyOf.add(claim);
        }
        
        return claimsPlayerIsAllyOf;
    }
    
    public Collection<Claim> getClaimsPlayerIsAllyOf(EntityPlayer player)
    { return getClaimsPlayerIsAllyOf(player.getGameProfile().getId()); }
    
    public Collection<Claim> getClaimsPlayerIsBannedFrom(UUID playerID)
    {
        Collection<Claim> checkingClaims;
        Collection<Claim> claimsPlayerIsBannedFrom = new HashSet<Claim>();
        
        synchronized(claims)
        { checkingClaims = new ArrayList<Claim>(claims.values()); }
        
        for(Claim claim : checkingClaims)
        {
            if(claim.isBannedFrom(playerID))
                claimsPlayerIsBannedFrom.add(claim);
        }
        
        return claimsPlayerIsBannedFrom;
    }
    
    public Collection<Claim> getClaimsPlayerIsBannedFrom(EntityPlayer player)
    { return getClaimsPlayerIsBannedFrom(player.getGameProfile().getId()); }
    
    public Collection<Claim> getClaimsPlayerHasGrantedPowerTo(UUID playerID)
    {
        Collection<Claim> checkingClaims;
        Collection<Claim> claimsPlayerHasGrantedPower = new HashSet<Claim>();
        
        synchronized(claims)
        { checkingClaims = new ArrayList<Claim>(claims.values()); }
        
        for(Claim claim : checkingClaims)
        {
            if(claim.getPowerPlayerHasGranted(playerID) > 0)
                claimsPlayerHasGrantedPower.add(claim);
        }
        
        return claimsPlayerHasGrantedPower;
   }
    
    public Collection<Claim> getClaimsPlayerHasGrantedPowerTo(EntityPlayer player)
    { return getClaimsPlayerHasGrantedPowerTo(player.getGameProfile().getId()); }
    
    public void claimChunk(UUID claimId, ChunkCoOrdinate chunk) throws NotEnoughClaimPowerToClaimException,
                                                                     ChunkAlreadyClaimedException
    {
        Claim claim = getClaim(claimId);
        
        if(claim == null)
            throw new ClaimWithIdNotPresentException(this, claimId);
        
        claimChunk(claim, chunk);
    }
    
    public void claimChunk(String claimName, ChunkCoOrdinate chunk) throws NotEnoughClaimPowerToClaimException,
                                                                           ChunkAlreadyClaimedException
    {
        Claim claim = getClaim(claimName);
        
        if(claim == null)
            throw new ClaimWithNameNotPresentException(this, claimName);
        
        claimChunk(claim, chunk);
    }
    
    public void claimChunk(Claim claim, ChunkCoOrdinate chunk) throws NotEnoughClaimPowerToClaimException,
                                                                      ChunkAlreadyClaimedException
    {
        UUID alreadyPresentClaim;
        
        synchronized(chunkClaims)
        {
            alreadyPresentClaim = chunkClaims.get(chunk);
            
            if(alreadyPresentClaim == null)
            {
                int claimPower = claim.getRemainingPower();
                
                if(claimPower >= 1)
                {
                    chunkClaims.put(chunk, claim.getId());
                    claim.addChunkOnlyInClaim(chunk);
                }
                else
                    throw new NotEnoughClaimPowerToClaimException(claim, claimPower, 1, chunk);
            }
        }
        
        if(alreadyPresentClaim != null)
            throw new ChunkAlreadyClaimedException(claim, getClaim(alreadyPresentClaim), chunk);
    }
    
    public void unclaimChunk(UUID claimId, ChunkCoOrdinate chunk) throws ChunkNotInClaimException
    {
        Claim claim = getClaim(claimId);
        
        if(claim == null)
            throw new ClaimWithIdNotPresentException(this, claimId);
        
        unclaimChunk(claim, chunk);
    }
    
    public void unclaimChunk(String claimName, ChunkCoOrdinate chunk) throws ChunkNotInClaimException
    {
        Claim claim = getClaim(claimName);
        
        if(claim == null)
            throw new ClaimWithNameNotPresentException(this, claimName);
        
        unclaimChunk(claim, chunk);
    }
    
    public void unclaimChunk(Claim claim, ChunkCoOrdinate chunk) throws ChunkNotInClaimException
    {
        UUID claimChunkIsIn;
        
        synchronized(chunkClaims)
        {
            claimChunkIsIn = chunkClaims.get(chunk);
            
            if(claimChunkIsIn.equals(claim.getId()))
            {
                chunkClaims.remove(chunk);
                claim.removeChunkOnlyInClaim(chunk);
            }
        }
        
        synchronized(claims)
        {
            if(!claim.getId().equals(claimChunkIsIn))
                throw new ChunkNotInClaimException(claim, claims.get(claimChunkIsIn), chunk);
        }
    }
    
    /**
     * Saves the contents of the registry to files in the folder at the stored folder path, over-writing them if
     * already present.
     * 
     * The files should be:
     *     claims.csv
     *     chunkclaims.csv
     * 
     * claims.csv follows the CSV file format, providing a column for each object stored in the Claim class. Where an
     * object is a collection, the members of that collection will be separated by the plus ("+") character. Strings
     * will be wrapped in quotation marks.
     * 
     * chunkclaims.csv follows the CSV file format, providing four columns: WorldID, XCoOrd, ZCoOrd, and ClaimID.
     */
    public void save()
    {}
    
    /**
     * Loads the contents of the files located in the folder at the stored folder path, or loads default values if not
     * present, or corrupt. Prints a notification to the console if the file is not loadable.
     */
    public void load()
    {}
    
    protected void loadDefaultChunkClaims()
    {}
    
    protected void loadDefaultClaims()
    {}
}