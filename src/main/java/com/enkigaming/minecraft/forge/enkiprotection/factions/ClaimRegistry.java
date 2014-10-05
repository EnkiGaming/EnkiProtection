package com.enkigaming.minecraft.forge.enkiprotection.factions;

public class ClaimRegistry
{
	/* Commented everything, because of those errors :P
    public ClaimRegistry(String folderPath)
    {}
    
    String folderPath;
    
    Map<ChunkPos, UUID> chunkClaims; // The chunk coördinates mapped to the UUID of the claim it's in.
    Map<UUID, Claim> claims; // All claims, using someClaim.getID() as the key.
    Map<UUID, Integer> playerClaimPowers; // How much power every player has to grant to claims.
    
    public Collection<Claim> getClaims()
    {}
    
    public Map<ChunkPos, UUID> getChunkClaims()
    {}
    
    public Collection<ChunkPos> getClaimedChunks()
    {}
    
    public Claim getClaimAtBlock(int blockX, int blockZ, World world)
    {}
    
    public Claim getClamAtBlock(Block block)
    {}
    
    public Claim getClaimAtChunk(int chunkX, int chunkZ, World world)
    {}
    
    public Claim getClaimAtChunk(Chunk chunk)
    {}
    
    public Collection<Claim> getClaimsPlayerIsOwnerOf(UUID playerID)
    {}
    
    public Collection<Claim> getClaimsPlayerIsOwnerOf(EntityPlayer player)
    {}
    
    public Collection<Claim> getClaimsPlayerIsMemberrOf(UUID playerID)
    {}
    
    public Collection<Claim> getClaimsPlayerIsMemberOf(EntityPlayer player)
    {}
    
    public Collection<Claim> getClaimsPlayerIsAllyOf(UUID playerID)
    {}
    
    public Collection<Claim> getClaimsPlayerIsAllyOf(EntityPlayer player)
    {}
    
    public Collection<Claim> getClaimsPlayerIsBannedFrom(UUID playerID)
    {}
    
    public Collection<Claim> getClaimsPlayerIsBannedFrom(EntityPlayer player)
    {}
    
    public Collection<Claim> getClaimsPlayerHasGrantedPowerTo(UUID playerID)
    {}
    
    public Collection<Claim> getClaimsPlayerHasGrantedPowerTo(EntityPlayer player)
    {}
    
    public void claimChunk(UUID claim, ChunkPos chunk) throws NotEnoughClaimPowerException,
                                                                     ChunkAlreadyClaimedException
    {}
    
    public void claimChunk(String claim, ChunkPos chunk) throws NotEnoughClaimPowerException,
                                                                       ChunkAlreadyClaimedException
    {}
    
    public void claimChunk(Claim claim, ChunkPos chunk) throws NotEnoughClaimPowerException,
                                                                      ChunkAlreadyClaimedException
    {}
    
    public void unclaimChunk(UUID claim, ChunkPos chunk) throws ChunkNotInClaimException
    {}
    
    public void unclaimChunk(String claim, ChunkPos chunk) throws ChunkNotInClaimException
    {}
    
    public void unclaimChunk(Claim claim, ChunkPos chunk) throws ChunkNotInClaimException
    {}
    
    public void addClaimPower(UUID playerID, int amount)
    {}
    
    public void addClaimPower(EntityPlayer player, int amount)
    {}
    
    public boolean removeClaimPower(UUID playerID, int amount)
    {}
    
    public boolean removeClaimPower(EntityPlayer player, int amount)
    {}
    
    public boolean setClaimPower(UUID playerID, int amount)
    {}
    
    public boolean setClaimPower(EntityPlayer player, int amount)
    {}
     */
	
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