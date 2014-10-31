package com.enkigaming.minecraft.forge.enkiprotection.claim;

import com.enkigaming.mcforge.enkilib.exceptions.UnableToParseTreeNodeException;
import com.enkigaming.mcforge.enkilib.filehandling.TreeFileHandler.TreeNode;
import com.enkigaming.minecraft.forge.enkiprotection.EnkiProtection;
import com.enkigaming.minecraft.forge.enkiprotection.Permissions;
import com.enkigaming.minecraft.forge.enkiprotection.claim.exceptions.ChunkAlreadyPresentException;
import com.enkigaming.minecraft.forge.enkiprotection.claim.exceptions.ChunkNotPresentException;
import com.enkigaming.minecraft.forge.enkiprotection.playerpower.PlayerPower;
import com.enkigaming.minecraft.forge.enkiprotection.registry.ClaimRegistry;
import com.enkigaming.minecraft.forge.enkiprotection.registry.exceptions.ChunkAlreadyClaimedException;
import com.enkigaming.minecraft.forge.enkiprotection.registry.exceptions.NotEnoughClaimPowerToClaimException;
import com.enkigaming.minecraft.forge.enkiprotection.utils.ChunkCoOrdinate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
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
    
    public Claim(TreeNode node, ClaimRegistry registry) throws UnableToParseTreeNodeException
    {
        this.registry = registry;
        
        UUID id = getClaimUUIDFromNodeName(node.getName());
        String name = getClaimNameFromNodeName(node.getName());
        
        if(id == null || name == null)
            throw new UnableToParseTreeNodeException(node, "Cannot parse claim name/id \"" + node.getName() + "\"");
        
        this.claimId = id;
        this.name = name;
        
        for(TreeNode subNode : node.getChildren())
        {
            if(subNode.getName().toUpperCase().startsWith(settingsTag.toUpperCase()))
                settings = new ClaimSettings(subNode);
            else if(subNode.getName().toUpperCase().startsWith(playersTag.toUpperCase()))
                players = new ClaimPlayers(subNode);
            else if(subNode.getName().toUpperCase().startsWith(powerTag.toUpperCase()))
                power = new ClaimPower() {{ registerListenersToClaimPower(this); }};
            else if(subNode.getName().toUpperCase().startsWith(chunksTag.toUpperCase()))
            {
                for(TreeNode chunkNode : subNode.getChildren())
                {
                    if(chunkNode.getName().toUpperCase().startsWith(chunkTag.toUpperCase()))
                    {
                        Integer x = null;
                        Integer z = null;
                        Integer worldId = null;
                        
                        for(TreeNode chunkSubNode : chunkNode.getChildren())
                        {
                            if     (chunkSubNode.getName().toUpperCase().startsWith(xCoOrdTag.toUpperCase())) // <editor-fold desc="{...}">
                            {
                                Integer currentX = getIntFromNodeName(chunkSubNode.getName());
                                
                                if(currentX == null)
                                {
                                    System.out.println("Could not parse int \"" + chunkSubNode.getName() + "\".");
                                    continue;
                                }
                                
                                x = currentX;
                            } // </editor-fold>
                            else if(chunkSubNode.getName().toUpperCase().startsWith(zCoOrdTag.toUpperCase())) // <editor-fold desc="{...}">
                            {
                                Integer currentZ = getIntFromNodeName(chunkSubNode.getName());
                                
                                if(currentZ == null)
                                {
                                    System.out.println("Could not parse int \"" + chunkSubNode.getName() + "\".");
                                    continue;
                                }
                                
                                x = currentZ;
                            } // </editor-fold>
                            else if(chunkSubNode.getName().toUpperCase().startsWith(worldIdTag.toUpperCase())) // <editor-fold desc="{...}">
                            {
                                Integer currentWorldId = getIntFromNodeName(chunkSubNode.getName());
                                
                                if(currentWorldId == null)
                                {
                                    System.out.println("Could not parse int world ID \"" + chunkSubNode.getName() + "\".");
                                    continue;
                                }
                                
                                x = currentWorldId;
                            } // </editor-fold>
                        }

                        if(x != null && z != null)
                        {
                            ChunkCoOrdinate chunk;
                            
                            if(worldId != null)
                                chunk = new ChunkCoOrdinate(x, z, worldId);
                            else
                                chunk = new ChunkCoOrdinate(x, z);
                            
                            chunks.add(chunk);
                        }
                    }
                }
            }
        }
    }
    
    protected final UUID claimId;
    protected String name;
    
    protected final ClaimRegistry registry;
    
    protected ClaimSettings settings = new ClaimSettings();
    protected ClaimPlayers players = new ClaimPlayers();
    protected ClaimPower power = new ClaimPower() {{ registerListenersToClaimPower(this); }};
    protected final Set<ChunkCoOrdinate> chunks = new HashSet<ChunkCoOrdinate>();
    
    protected final Object nameLock = new Object();
    
    protected final static String nameTag     = "Name";
    protected final static String settingsTag = "Settings";
    protected final static String playersTag  = "Players";
    protected final static String powerTag    = "Power";
    protected final static String chunksTag   = "Chunks";
    protected final static String chunkTag    = "Chunk";
    protected final static String xCoOrdTag   = "X";
    protected final static String zCoOrdTag   = "Z";
    protected final static String worldIdTag  = "World ID";
    protected final static String separator   = ": ";
    
    protected final static String replaceTag        = "#OWNMEMBERANY#";
    protected final static String escapedReplaceTag = "\\Q" + replaceTag + "\\E";
    
    void registerListenersToClaimPower(ClaimPower claimPower)
    {
        claimPower.addListener(new ClaimPower.ForceRevokeListener()
        {
            @Override
            public void onForceRevoke(UUID playerRevoking, int amountBeingRevoked, int amountMoreThanAvailableBeingRevoked)
            {
                for(int i = 0; i < amountMoreThanAvailableBeingRevoked; i++)
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
        });
        
        claimPower.addListener(new ClaimPower.QueuedRevocationExpirationListener()
        {
            @Override
            public void onQueuedRevocationExpiration(UUID playerId, int amount)
            {
                PlayerPower pPower = EnkiProtection.getInstance().getClaimPowers().getForPlayer(playerId);
                
                if(pPower != null)
                    pPower.notifyOfPowerGrantReturn(Claim.this, amount);
            }
        });
        
        claimPower.setPowerUsedGetter(new ClaimPower.PowerUsedGetter()
        {
            @Override
            public int getPowerUsed()
            { return chunks.size(); }
        });
    }
    
    public TreeNode toTreeNode()
    {
        TreeNode baseNode = new TreeNode(name + separator + claimId.toString());
        TreeNode chunksNode = new TreeNode(chunksTag + separator);
        
        baseNode.addChild(settings.toTreeNode());
        baseNode.addChild(players.toTreeNode());
        baseNode.addChild(power.toTreeNode());
        
        baseNode.addChildren(Arrays.asList(settings.toTreeNode(),
                                           players .toTreeNode(),
                                           power   .toTreeNode(),
                                           chunksNode));
        
        for(ChunkCoOrdinate chunk : chunks)
        {
            TreeNode chunkNode = new TreeNode(chunkTag);
            TreeNode xNode = new TreeNode(xCoOrdTag + separator + Integer.toString(chunk.getXCoOrd()));
            TreeNode zNode = new TreeNode(zCoOrdTag + separator + Integer.toString(chunk.getZCoOrd()));
            TreeNode worldNode = null;
            
            if(chunk.hasSpecifiedWorld())
                worldNode = new TreeNode(worldIdTag + separator + Integer.toString(chunk.getWorldID()));
            
            chunkNode.addChildren(Arrays.asList(xNode, zNode));
            
            if(worldNode != null)
                chunkNode.addChild(worldNode);
            
            chunksNode.addChild(chunkNode);
        }
        
        return baseNode;
    }
    
    static UUID getClaimUUIDFromNodeName(String nodeName)
    {
        String[] nameParts = nodeName.split("\\Q" + separator + "\\E");
        
        if(nameParts.length < 2)
            return null;
        
        String uuidString = nameParts[nameParts.length - 1].trim();
        UUID claimId;
        
        try
        { claimId = UUID.fromString(uuidString); }
        catch(IllegalArgumentException exception)
        { return null; }
        
        return claimId;
    }
    
    static String getClaimNameFromNodeName(String nodeName)
    {
        String[] nameParts = nodeName.split("\\Q" + separator + "\\E");
        
        if(nameParts.length < 2)
            return null;
        
        return nameParts[0].trim();
    }
    
    static Integer getIntFromNodeName(String nodeName)
    {
        String[] nameParts = nodeName.split("\\Q" + separator + "\\E");
        
        if(nameParts.length < 2)
            return null;
        
        String intString = nameParts[nameParts.length - 1].trim();
        Integer returnInt;
        
        try
        { returnInt = Integer.parseInt(intString); }
        catch(NumberFormatException exception)
        { return null; }
        
        return returnInt;
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
    
    public void claimChunk(ChunkCoOrdinate chunk) throws ChunkAlreadyClaimedException,
                                                         NotEnoughClaimPowerToClaimException
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
    
    public void claimChunk(Chunk chunk) throws ChunkAlreadyClaimedException,
                                               NotEnoughClaimPowerToClaimException
    { claimChunk(new ChunkCoOrdinate(chunk)); }
    
    public boolean unclaimChunk(ChunkCoOrdinate chunk)
    {
        synchronized(chunks)
        { return chunks.remove(chunk); }
    }
    
    public boolean unclaimChunk(Chunk chunk)
    { return unclaimChunk(new ChunkCoOrdinate(chunk)); }
    
    public void unclaimChunks()
    {
        synchronized(chunks)
        { chunks.clear(); }
    }
    
    /**
     * 
     */
    public void cleanUp()
    {
        // The only part that needs cleaned up is the ClaimPower, as it's double-referenced with the player claimpower
        // registry. TO DO: Once events are implemented, make the claim instead fire an event, listened to by the
        // claimpower registry, that removes listeners from the claim's events and removes any power grants.
        
        power.cleanUp();
    }
    
    // ========== Convenience methods ==========
    
    public boolean removingWouldBeNonConsecutive(ChunkCoOrdinate chunk) throws ChunkNotPresentException
    {
        synchronized(chunks)
        {
            if(!chunks.contains(chunk))
                throw new ChunkNotPresentException(chunk);
            
            if(chunks.size() == 1)
                return false;
            
            Collection<ChunkCoOrdinate> chunkGroupWithChunk = new HashSet<ChunkCoOrdinate>();
            Collection<ChunkCoOrdinate> currentlyChecking = new HashSet<ChunkCoOrdinate>();
            currentlyChecking.add(chunk);
            
            while(!currentlyChecking.isEmpty())
            {
                Set<ChunkCoOrdinate> newThisRound = new HashSet<ChunkCoOrdinate>();
                
                for(ChunkCoOrdinate current : currentlyChecking)
                {
                    Collection<ChunkCoOrdinate> toAdd = current.getAdjacentChunks();
                    newThisRound.addAll(toAdd);
                }
                
                newThisRound.retainAll(chunks);
                newThisRound.removeAll(chunkGroupWithChunk);
                newThisRound.removeAll(currentlyChecking);
                
                chunkGroupWithChunk.addAll(currentlyChecking);
                currentlyChecking = newThisRound;
            }
            
            if(chunkGroupWithChunk.size() == 1)
                return false;
            
            Collection<ChunkCoOrdinate> chunkGroupWithoutChunk = new HashSet<ChunkCoOrdinate>();
            
            List<ChunkCoOrdinate> availableAdjacentChunks = new ArrayList<ChunkCoOrdinate>();
            availableAdjacentChunks.addAll(chunk.getAdjacentChunks());
            availableAdjacentChunks.retainAll(chunks);
            currentlyChecking.add(availableAdjacentChunks.get(0));
            
            while(!currentlyChecking.isEmpty())
            {
                Set<ChunkCoOrdinate> newThisRound = new HashSet<ChunkCoOrdinate>();
                
                for(ChunkCoOrdinate current : currentlyChecking)
                {
                    Collection<ChunkCoOrdinate> toAdd = current.getAdjacentChunks();
                    newThisRound.addAll(toAdd);
                }
                
                newThisRound.retainAll(chunks);
                newThisRound.removeAll(chunkGroupWithoutChunk);
                newThisRound.removeAll(currentlyChecking);
                newThisRound.remove(chunk);
                
                chunkGroupWithChunk.addAll(currentlyChecking);
                currentlyChecking = newThisRound;
            }
            
            return chunkGroupWithChunk.size() != chunkGroupWithoutChunk.size() + 1;
        }
    }
    
    public boolean addingWouldBeNonConsecutive(ChunkCoOrdinate chunk) throws ChunkAlreadyPresentException
    {
        if(chunks.contains(chunk))
            throw new ChunkAlreadyPresentException(chunk);
        
        synchronized(chunks)
        { return !chunk.isNextTo(chunks); }
    }
    
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
    
    /**
     * Checks whether a player can do something based on an own.x, member.x, any.x permission format.
     * @param permission The permission to check for, in the format of some.permission.#OWNMEMBERANY#.dothing
     * @return Whether the player can do it.
     */
    protected boolean canDo(UUID playerId, String permission)
    {
        if(Permissions.hasPermission(playerId, permission.replaceAll(escapedReplaceTag, "any")))
            return true;
        
        if(Permissions.hasPermission(playerId, permission.replaceAll(escapedReplaceTag, "member")))
            if(players.isMemberOrBetter(playerId))
                return true;
        
        if(Permissions.hasPermission(playerId, permission.replaceAll(escapedReplaceTag, "own")))
            if(players.isOwner(playerId))
                return true;
        
        return false;
    }
    
    public boolean canDelete(UUID playerId)
    { return canDo(playerId, "enkiprotection.claim." + replaceTag + ".delete"); }
    
    public boolean canDelete(EntityPlayer player)
    { return canDelete(player.getGameProfile().getId()); }
    
    public boolean canJoin(UUID playerId)
    { return Permissions.hasPermission(playerId, "enkiprotection.claim.join"); }
    
    public boolean canJoin(EntityPlayer player)
    { return canJoin(player.getGameProfile().getId()); }
    
    public boolean canRename(UUID playerId)
    { return canDo(playerId, "enkiprotection.claim." + replaceTag + ".rename"); }
    
    public boolean canRename(EntityPlayer player)
    { return canRename(player.getGameProfile().getId()); }
    
    public boolean canInvite(UUID playerId)
    { return canDo(playerId, "enkiprotection.claim." + replaceTag + ".invite"); }
    
    public boolean canInvite(EntityPlayer player)
    { return canInvite(player.getGameProfile().getId()); }
    
    public boolean canSetAllies(UUID playerId)
    { return canDo(playerId, "enkiprotection.claim." + replaceTag + ".setallies"); }
    
    public boolean canSetAllies(EntityPlayer player)
    { return canSetAllies(player.getGameProfile().getId()); }
    
    public boolean canSetBanned(UUID playerId)
    { return canDo(playerId, "enkiprotection.claim." + replaceTag + ".setbanned"); }
    
    public boolean canSetBanned(EntityPlayer player)
    { return canSetBanned(player.getGameProfile().getId()); }
    
    public boolean canMakeOwner(UUID playerId)
    { return canDo(playerId, "enkiprotection.own." + replaceTag + ".makeowner"); }
    
    public boolean canMakeOwner(EntityPlayer player)
    { return canMakeOwner(player.getGameProfile().getId()); }
    
    public boolean canAddChunks(UUID playerId)
    { return canDo(playerId, "enkiprotection.claim.chunk" + replaceTag + ".add"); }
    
    public boolean canAddChunks(EntityPlayer player)
    { return canAddChunks(player.getGameProfile().getId()); }
    
    public boolean canRemoveChunks(UUID playerId)
    { return canDo(playerId, "enkiprotection.claim.chunk" + replaceTag + ".remove"); }
    
    public boolean canRemoveChunks(EntityPlayer player)
    { return canRemoveChunks(player.getGameProfile().getId()); }
    
    public boolean canAutoAddChunks(UUID playerId)
    {
        return canDo(playerId, "enkiprotection.claim.chunk" + replaceTag + ".add")
            && Permissions.hasPermission(playerId, "enkiprotection.claim.chunk.autoadd");
    }
    
    public boolean canAutoAddChunks(EntityPlayer player)
    { return canAutoAddChunks(player.getGameProfile().getId()); }
    
    public boolean canAddChunksNonConsecutively(UUID playerId)
    {
        return canDo(playerId, "enkiprotection.claim.chunk" + replaceTag + ".add")
            && Permissions.hasPermission(playerId, "enkiprotection.claim.chunk.nonconsecutiveadd");
    }
    
    public boolean canAddChunksNonConsecutively(EntityPlayer player)
    { return canAddChunksNonConsecutively(player.getGameProfile().getId()); }
    
    public boolean canAutoRemoveChunks(UUID playerId)
    {
        return canDo(playerId, "enkiprotection.claim.chunk" + replaceTag + ".remove")
            && Permissions.hasPermission(playerId, "enkiprotection.claim.chunk.autoremove");
    }
    
    public boolean canAutoRemoveChunks(EntityPlayer player)
    { return canAutoRemoveChunks(player.getGameProfile().getId()); }
    
    public boolean canRemoveChunksNonConsecutively(UUID playerId)
    {
        return canDo(playerId, "enkiprotection.claim.chunk" + replaceTag + ".remove")
            && Permissions.hasPermission(playerId, "enkiprotection.claim.chunk.nonconsecutiveadd");
    }
    
    public boolean canRemoveChunksNonConsecutively(EntityPlayer player)
    { return canRemoveChunksNonConsecutively(player.getGameProfile().getId()); }
    
    public boolean canAddChunk(UUID playerId, ChunkCoOrdinate chunk) throws ChunkAlreadyPresentException
    {
        if(!canAddChunks(playerId))
            return false;
        
        synchronized(chunks)
        {
            if(!addingWouldBeNonConsecutive(chunk))
                return true;
        }
        
        return Permissions.hasPermission(playerId, "enkiprotection.claim.chunk.nonconsecutiveadd");
    }
    
    public boolean canAddChunk(EntityPlayer player, ChunkCoOrdinate chunk) throws ChunkAlreadyPresentException
    { return canAddChunk(player.getGameProfile().getId(), chunk); }
    
    public boolean canRemoveChunk(UUID playerId, ChunkCoOrdinate chunk) throws ChunkNotPresentException
    {
        if(!canRemoveChunks(playerId))
            return false;
        
        synchronized(chunks)
        {
            if(!removingWouldBeNonConsecutive(chunk))
                return true;
        }
        
        return Permissions.hasPermission(playerId, "enkiprotection.claim.chunk.nonconsecutiveremove");
    }
    
    public boolean canRemoveChunk(EntityPlayer player, ChunkCoOrdinate chunk) throws ChunkNotPresentException
    { return canRemoveChunk(player.getGameProfile().getId(), chunk); }
    
    public boolean canSetWelcomeMessage(UUID playerId)
    { return canDo(playerId, "enkiprotection.claim.setting" + replaceTag + ".welcomemessage"); }
    
    public boolean canSetWelcomeMessage(EntityPlayer player)
    { return canSetWelcomeMessage(player.getGameProfile().getId()); }
    
    public boolean canSetAllowExplosions(UUID playerId)
    { return canDo(playerId, "enkiprotection.claim.setting" + replaceTag + ".allowexplosions"); }
    
    public boolean canSetAllowExplosions(EntityPlayer player)
    { return canSetAllowExplosions(player.getGameProfile().getId()); }
    
    public boolean canSetAllowFriendlyCombat(UUID playerId)
    { return canDo(playerId, "enkiprotection.claim.setting" + replaceTag + ".allowfriendlycombat"); }
    
    public boolean canSetAllowFriendlyCombat(EntityPlayer player)
    { return canSetAllowFriendlyCombat(player.getGameProfile().getId()); }
    
    public boolean canSetAllowPlayerCombat(UUID playerId)
    { return canDo(playerId, "enkiprotection.claim.setting" + replaceTag + ".allowplayercombat"); }
    
    public boolean canSetAllowPlayerCombat(EntityPlayer player)
    { return canSetAllowPlayerCombat(player.getGameProfile().getId()); }
    
    public boolean canSetAllowMobEntry(UUID playerId)
    { return canDo(playerId, "enkiprotection.claim.setting" + replaceTag + ".allowmobentry"); }
    
    public boolean canSetAllowMobEntry(EntityPlayer player)
    { return canSetAllowMobEntry(player.getGameProfile().getId()); }
    
    public boolean canSetAllowNonAllyEntry(UUID playerId)
    { return canDo(playerId, "enkiprotection.claim.setting" + replaceTag + ".allownonallyentry"); }
    
    public boolean canSetAllowNonAllyEntry(EntityPlayer player)
    { return canSetAllowNonAllyEntry(player.getGameProfile().getId()); }
    
    public boolean canSetAllowNonAllyBlockInteraction(UUID playerId)
    { return canDo(playerId, "enkiprotection.claim.setting" + replaceTag + ".allowinteractwithblocks"); }
    
    public boolean canSetAllowNonAllyBlockInteraction(EntityPlayer player)
    { return canSetAllowNonAllyBlockInteraction(player.getGameProfile().getId()); }
    
    public boolean canSetAllowNonAllyEntityInteraction(UUID playerId)
    { return canDo(playerId, "enkiprotection.claim.setting" + replaceTag + ".allowinteractwithentities"); }
    
    public boolean canSetAllowNonAllyEntityInteraction(EntityPlayer player)
    { return canSetAllowNonAllyEntityInteraction(player.getGameProfile().getId()); }
    
    public boolean canSetAllowNonAllyBuild(UUID playerId)
    { return canDo(playerId, "enkiprotection.claim.setting" + replaceTag + ".allowbreakorplaceblocks"); }
    
    public boolean canSetAllowNonAllyBuild(EntityPlayer player)
    { return canSetAllowNonAllyBuild(player.getGameProfile().getId()); }
}