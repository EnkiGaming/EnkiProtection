package com.enkigaming.minecraft.forge.enkiprotection.registry;

import com.enkigaming.mcforge.enkilib.exceptions.NullArgumentException;
import com.enkigaming.mcforge.enkilib.exceptions.UnableToParseTreeNodeException;
import com.enkigaming.mcforge.enkilib.filehandling.TreeFileHandler.TreeNode;
import com.enkigaming.minecraft.forge.enkiprotection.claim.Claim;
import com.enkigaming.mcforge.enkilib.filehandling.FileHandler;
import com.enkigaming.mcforge.enkilib.filehandling.TreeFileHandler;
import com.enkigaming.minecraft.forge.enkiprotection.registry.exceptions.ChunkAlreadyClaimedException;
import com.enkigaming.minecraft.forge.enkiprotection.registry.exceptions.ChunkNotInClaimException;
import com.enkigaming.minecraft.forge.enkiprotection.registry.exceptions.ClaimIdAlreadyPresentException;
import com.enkigaming.minecraft.forge.enkiprotection.registry.exceptions.ClaimNameAlreadyPresentException;
import com.enkigaming.minecraft.forge.enkiprotection.registry.exceptions.NoClaimWithMatchingIdException;
import com.enkigaming.minecraft.forge.enkiprotection.registry.exceptions.NoClaimWithMatchingNameException;
import com.enkigaming.minecraft.forge.enkiprotection.registry.exceptions.NotEnoughClaimPowerToClaimException;
import com.enkigaming.minecraft.forge.enkiprotection.utils.ChunkCoOrdinate;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

public class ClaimRegistry
{
    public ClaimRegistry(File saveFolder)
    { fileHandler = makeFileHandler(saveFolder); }
    
    protected final Map<UUID, Claim> claims = new HashMap<UUID, Claim>();
    protected final FileHandler fileHandler;
    
    protected final Lock claimsLock = new ReentrantLock();
    
    protected FileHandler makeFileHandler(File saveFolder)
    {
        return new TreeFileHandler("ClaimRegistry", new File(saveFolder, "Claims.txt"), "Not all claims could be read. Those that could be, were.")
        {
            @Override
            protected void preSave()
            { claimsLock.lock(); }

            @Override
            protected List<TreeNode> getTreeStructureOfSaveData()
            {
                List<TreeNode> tree = new ArrayList<TreeNode>();
                
                for(Claim claim : claims.values())
                {
                    tree.add(claim.toTreeNode());
                    tree.add(new TreeNode(""));
                }
                
                return tree;
            }

            @Override
            protected void postSave()
            { claimsLock.unlock(); }

            @Override
            protected void preInterpretation()
            {
                claimsLock.lock();
                claims.clear();
            }

            @Override
            protected boolean interpretTree(List<TreeNode> list)
            {
                boolean loadedSuccessfully = true;
                
                for(TreeNode node : list)
                {
                    Claim claim;
                    
                    try
                    { claim = new Claim(node, ClaimRegistry.this); }
                    catch(UnableToParseTreeNodeException exception)
                    {
                        System.out.println("Unable to parse claim: \"" + node.getName() + "\"");
                        loadedSuccessfully = false;
                        continue;
                    }
                    
                    claims.put(claim.getId(), claim);
                }
                
                return loadedSuccessfully;
            }

            @Override
            protected void postInterpretation()
            { claimsLock.unlock(); }

            @Override
            protected void onNoFileToInterpret()
            { }
        };
    }
    
    public Claim getClaim(UUID claimId)
    {
        claimsLock.lock();
        
        try
        { return claims.get(claimId); }
        finally
        { claimsLock.unlock(); }
    }
    
    public Claim getClaim(String claimName)
    {
        claimsLock.lock();
        
        try
        {
            for(Claim claim : claims.values())
                if(claim.getName().equalsIgnoreCase(claimName))
                    return claim;
            
            return null;
        }
        finally
        { claimsLock.unlock(); }
    }
    
    public Collection<Claim> getClaims()
    {
        claimsLock.lock();
        
        try
        { return new ArrayList<Claim>(claims.values()); }
        finally
        { claimsLock.unlock(); }
    }
    
    public Claim getClaimAtBlock(int x, int z, World world)
    { return getClaimAtChunk(new ChunkCoOrdinate(x * 16, z * 16, world.provider.dimensionId)); }
    
    public Claim getClaimAtBlock(ChunkPosition block, World world)
    { return getClaimAtChunk(new ChunkCoOrdinate(block.chunkPosX * 16, block.chunkPosZ * 16, world.provider.dimensionId)); }
    
    public Claim getClaimAtBlock(int x, int z, int worldId)
    { return getClaimAtChunk(new ChunkCoOrdinate(x * 16, z * 16, worldId)); }
    
    public Claim getClaimAtBlock(ChunkPosition block, int worldId)
    { return getClaimAtChunk(new ChunkCoOrdinate(block.chunkPosX * 16, block.chunkPosZ * 16, worldId)); }
    
    public Claim getClaimAtChunk(int chunkX, int chunkZ, World world)
    { return getClaimAtChunk(new ChunkCoOrdinate(chunkX, chunkZ, world.provider.dimensionId)); }
    
    public Claim getClaimAtChunk(ChunkPosition chunk, World world)
    { return getClaimAtChunk(new ChunkCoOrdinate(chunk.chunkPosX, chunk.chunkPosZ, world.provider.dimensionId)); }
    
    public Claim getClaimAtChunk(ChunkCoOrdinate chunk)
    {
        claimsLock.lock();
        
        try
        {
            for(Claim claim : claims.values())
                if(claim.hasChunk(chunk))
                    return claim;
            
            return null;
        }
        finally
        { claimsLock.unlock(); }
    }
    
    public Claim getClaimAtChunk(Chunk chunk)
    { return getClaimAtChunk(new ChunkCoOrdinate(chunk)); }
    
    public Collection<Claim> getClaimsPlayerIsOwnerOf(UUID playerId)
    {
        claimsLock.lock();
        
        try
        {
            Collection<Claim> claimsOwnerOf = new ArrayList<Claim>();
            
            for(Claim claim : claims.values())
                if(claim.getPlayerManager().getOwnerId().equals(playerId))
                    claimsOwnerOf.add(claim);
            
            return claimsOwnerOf;
        }
        finally
        { claimsLock.unlock(); }
    }
    
    public Collection<Claim> getClaimsPlayerIsOwnerOf(EntityPlayer player)
    { return getClaimsPlayerIsOwnerOf(player.getGameProfile().getId()); }
    
    public Collection<Claim> getClaimsPlayerIsMemberOf(UUID playerId)
    {
        claimsLock.lock();
        
        try
        {
            Collection<Claim> claimsOwnerOf = new ArrayList<Claim>();
            
            for(Claim claim : claims.values())
                if(claim.getPlayerManager().getMembers().contains(playerId))
                    claimsOwnerOf.add(claim);
            
            return claimsOwnerOf;
        }
        finally
        { claimsLock.unlock(); }
    }
    
    public Collection<Claim> getClaimsPlayerIsMemberOf(EntityPlayer player)
    { return getClaimsPlayerIsMemberOf(player.getGameProfile().getId()); }
    
    public Collection<Claim> getClaimsPlayerIsAllyOf(UUID playerId)
    {
        claimsLock.lock();
        
        try
        {
            Collection<Claim> claimsOwnerOf = new ArrayList<Claim>();
            
            for(Claim claim : claims.values())
                if(claim.getPlayerManager().getAllies().contains(playerId))
                    claimsOwnerOf.add(claim);
            
            return claimsOwnerOf;
        }
        finally
        { claimsLock.unlock(); }
    }
    
    public Collection<Claim> getClaimsPlayerIsAllyOf(EntityPlayer player)
    { return getClaimsPlayerIsAllyOf(player.getGameProfile().getId()); }
    
    public Collection<Claim> getClaimsPlayerIsBannedFrom(UUID playerId)
    {
        claimsLock.lock();
        
        try
        {
            Collection<Claim> claimsOwnerOf = new ArrayList<Claim>();
            
            for(Claim claim : claims.values())
                if(claim.getPlayerManager().getBannedPlayers().contains(playerId))
                    claimsOwnerOf.add(claim);
            
            return claimsOwnerOf;
        }
        finally
        { claimsLock.unlock(); }
    }
    
    public Collection<Claim> getClaimsPlayerIsBannedFrom(EntityPlayer player)
    { return getClaimsPlayerIsBannedFrom(player.getGameProfile().getId()); }
    
    public boolean chunkIsInClaim(ChunkCoOrdinate chunk)
    {
        claimsLock.lock();
        
        try
        {
            for(Claim claim : claims.values())
                if(claim.hasChunk(chunk))
                    return true;
            
            return false;
        }
        finally
        { claimsLock.unlock(); }
    }
    
    public boolean chunkIsInClaim(Chunk chunk)
    { return chunkIsInClaim(new ChunkCoOrdinate(chunk)); }
    
    protected UUID getAvailableClaimId()
    {
        claimsLock.lock();
        
        try
        {
            Claim matchingClaim = null;
            UUID current;
            
            do
            {
                current = UUID.randomUUID();
                matchingClaim = claims.get(current);
            }
            while(matchingClaim != null);
            
            return current;
            
            // Overly defencive paranoia ftw.
        }
        finally
        { claimsLock.unlock(); }
    }
    
    public void addClaim(Claim claim) throws ClaimIdAlreadyPresentException, ClaimNameAlreadyPresentException
    {
        claimsLock.lock();
        
        try
        {
            Claim matchingClaim = claims.get(claim.getId());
            
            if(matchingClaim != null)
                throw new ClaimIdAlreadyPresentException(matchingClaim, claim, claim.getId());
            
            matchingClaim = getClaim(claim.getName());
            
            if(matchingClaim != null)
                throw new ClaimNameAlreadyPresentException(matchingClaim, claim, matchingClaim.getName());
            
            claims.put(claim.getId(), claim);
        }
        finally
        { claimsLock.unlock(); }
    }
    
    public Claim createClaim(String claimName) throws ClaimNameAlreadyPresentException
    {
        claimsLock.lock();
        
        try
        {
            Claim matchingClaim = getClaim(claimName);
            
            if(matchingClaim != null)
                throw new ClaimNameAlreadyPresentException(matchingClaim, matchingClaim, matchingClaim.getName());
            
            Claim claim = new Claim(getAvailableClaimId(), claimName, this);
            
            claims.put(claim.getId(), claim);
            
            return claim;
        }
        finally
        { claimsLock.unlock(); }
    }
    
    public boolean removeClaim(Claim claim)
    {
        claimsLock.lock();
        
        try
        {
            Claim removed = claims.remove(claim.getId());
            
            if(removed == null)
                return false;
            
            removed.cleanUp();
            return true;
        }
        finally
        { claimsLock.unlock(); }
    }
    
    public boolean removeClaim(String claimName)
    {
        claimsLock.lock();
        
        try
        {
            Claim removed = getClaim(claimName);
            
            if(removed == null)
                return false;
            
            claims.remove(removed);
            removed.cleanUp();
            return true;
        }
        finally
        { claimsLock.unlock(); }
    }
    
    public boolean removeClaim(UUID claimId)
    {
        claimsLock.lock();
        
        try
        {
            Claim removed = getClaim(claimId);
            
            if(removed == null)
                return false;
            
            claims.remove(removed);
            removed.cleanUp();
            return true;
        }
        finally
        { claimsLock.unlock(); }
    }
    
    public void addChunkToClaim(Claim claim, ChunkCoOrdinate chunk) throws ChunkAlreadyClaimedException,
                                                                           NotEnoughClaimPowerToClaimException
    {
        if(claim == null)
            throw new NullArgumentException("claim");
        
        if(chunk == null)
            throw new NullArgumentException("chunk");
        
        claimsLock.lock();
        
        try
        {
            if(!claims.containsKey(claim.getId()))
                throw new IllegalArgumentException("Claim not in registry: " + claim.getName() + " - " + claim.getId().toString());
            
            for(Claim currentClaim : claims.values())
                if(currentClaim.hasChunk(chunk))
                    throw new ChunkAlreadyClaimedException(currentClaim, chunk);
            
            claim.claimChunk(chunk);
        }
        finally
        { claimsLock.unlock(); }
    }
    
    public void addChunkToClaim(UUID claimId, ChunkCoOrdinate chunk) throws ChunkAlreadyClaimedException,
                                                                            NotEnoughClaimPowerToClaimException,
                                                                            NoClaimWithMatchingIdException
    {
        if(claimId == null)
            throw new NullArgumentException("claimId");
        
        claimsLock.lock();
        
        try
        {
            Claim claim = getClaim(claimId);

            if(claim == null)
                throw new NoClaimWithMatchingIdException(claimId);

            addChunkToClaim(claim, chunk);
        }
        finally
        { claimsLock.unlock(); }
    }
    
    public void addChunkToClaim(String claimName, ChunkCoOrdinate chunk) throws ChunkAlreadyClaimedException,
                                                                                NotEnoughClaimPowerToClaimException,
                                                                                NoClaimWithMatchingNameException
    {
        if(claimName == null)
            throw new NullArgumentException("claimName");
        
        claimsLock.lock();
        
        try
        {
            Claim claim = getClaim(claimName);

            if(claim == null)
                throw new NoClaimWithMatchingNameException(claimName);

            addChunkToClaim(claim, chunk);
        }
        finally
        { claimsLock.unlock(); }
    }
    
    public void removeChunkFromClaim(Claim claim, ChunkCoOrdinate chunk) throws ChunkNotInClaimException
    {
        if(claim == null)
            throw new NullArgumentException("claim");
        
        if(chunk == null)
            throw new NullArgumentException("chunk");
        
        claimsLock.lock();
        
        try
        {
            if(!claims.containsKey(claim.getId()))
                throw new IllegalArgumentException("Claim not in registry: " + claim.getName() + " - " + claim.getId().toString());
            
            if(!claim.unclaimChunk(chunk))
                throw new ChunkNotInClaimException(chunk, claim);
        }
        finally
        { claimsLock.unlock(); }
    }
    
    public void removeChunkFromClaim(UUID claimId, ChunkCoOrdinate chunk) throws ChunkNotInClaimException,
                                                                                 NoClaimWithMatchingIdException
    {
        if(claimId == null)
            throw new NullArgumentException("claimId");
        
        claimsLock.lock();
        
        try
        {
            Claim claim = getClaim(claimId);

            if(claim == null)
                throw new NoClaimWithMatchingIdException(claimId);

            removeChunkFromClaim(claim, chunk);
        }
        finally
        { claimsLock.unlock(); }
    }
    
    public void removeChunkFromClaim(String claimName, ChunkCoOrdinate chunk) throws ChunkNotInClaimException,
                                                                                     NoClaimWithMatchingNameException
    {
        if(claimName == null)
            throw new NullArgumentException("claimName");
        
        claimsLock.lock();
        
        try
        {
            Claim claim = getClaim(claimName);

            if(claim == null)
                throw new NoClaimWithMatchingNameException(claimName);

            removeChunkFromClaim(claim, chunk);
        }
        finally
        { claimsLock.unlock(); }
    }
    
    public void removeChunkFromClaim(ChunkCoOrdinate chunk) throws ChunkNotInClaimException
    {
        if(chunk == null)
            throw new NullArgumentException("chunk");
        
        claimsLock.lock();
        
        try
        {
            Claim claim = getClaimAtChunk(chunk);
            
            if(claim == null)
                throw new ChunkNotInClaimException(chunk, null);
            
            removeChunkFromClaim(claim, chunk);
        }
        finally
        { claimsLock.unlock(); }
    }
}