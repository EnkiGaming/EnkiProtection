package com.enkigaming.minecraft.forge.enkiprotection.registry;

import com.enkigaming.minecraft.forge.enkiprotection.claim.Claim;
import com.enkigaming.mcforge.enkilib.filehandling.FileHandler;
import com.enkigaming.minecraft.forge.enkiprotection.registry.exceptions.ClaimIdAlreadyPresentException;
import com.enkigaming.minecraft.forge.enkiprotection.registry.exceptions.ClaimNameAlreadyPresentException;
import com.enkigaming.minecraft.forge.enkiprotection.utils.ChunkCoOrdinate;
import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import net.minecraft.entity.player.EntityPlayer;
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
        
    }
    
    public Claim getClaim(UUID claimId)
    {
        
    }
    
    public Claim getClaim(String claimName)
    {
        
    }
    
    public Collection<Claim> getClaims()
    {
        
    }
    
    public Claim getClaimAtBlock(int x, int z, World world)
    {
        
    }
    
    public Claim getClaimAtBlock(ChunkPosition block, World world)
    {
        
    }
    
    public Claim getClaimAtChunk(int chunkX, int chunkZ, World world)
    {
        
    }
    
    public Claim getClaimAtChunk(ChunkPosition chunk, World world)
    {
        
    }
    
    public Claim getClaimAtChunk(ChunkCoOrdinate chunk)
    {
        
    }
    
    public Collection<Claim> getClaimsPlayerIsOwnerOf(UUID playerId)
    {
        
    }
    
    public Collection<Claim> getClaimsPlayerIsOwnerOf(EntityPlayer player)
    {
        
    }
    
    public Collection<Claim> getClaimsPlayerIsMemberOf(UUID playerId)
    {
        
    }
    
    public Collection<Claim> getClaimsPlayerIsMemberOf(EntityPlayer player)
    {
        
    }
    
    public Collection<Claim> getClaimsPlayerIsAllyOf(UUID playerId)
    {
        
    }
    
    public Collection<Claim> getClaimsPlayerIsAllyOf(EntityPlayer player)
    {
        
    }
    
    public Collection<Claim> getClaimsPlayerIsBannedFrom(UUID playerId)
    {
        
    }
    
    public Collection<Claim> getClaimsPlayerIsBannedFrom(EntityPlayer player)
    {
        
    }
    
    public boolean chunkIsInClaim(ChunkCoOrdinate chunk)
    {  }
    
    public boolean chunkIsInClaim(Chunk chunk)
    {  }
    
    protected UUID getAvailableClaimId()
    {
        
    }
    
    public boolean addClaim(Claim claim) throws ClaimIdAlreadyPresentException, ClaimNameAlreadyPresentException
    {
        
    }
    
    public Claim createClaim(String claimName) throws ClaimNameAlreadyPresentException
    {
        
    }
}