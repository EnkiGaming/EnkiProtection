package com.enkigaming.minecraft.forge.enkiprotection.factions;

import java.util.ArrayList;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

import com.enkigaming.minecraft.forge.enkiprotection.util.ChunkPos;

public class Faction
{
	public final String factionID;
	public String description;
	public final ArrayList<FactionMember> members; // Able to manage the claim, modify it, add to it, etc. but can't add new members.
	public final ArrayList<String> allies; // Able to enter, break blocks, right-click, etc.
	public final ArrayList<String> enemies; // Not able to enter the area
	public final ArrayList<ChunkPos> claimedChunks;
	
	public Faction(String s)
	{
		factionID = s;
		
		description = "";
		members = new ArrayList<FactionMember>();
		allies = new ArrayList<String>();
		enemies = new ArrayList<String>();
		claimedChunks = new ArrayList<ChunkPos>();
	}
	
	public void readFromNBT(NBTTagCompound tag)
	{
		description = tag.getString("Desc");
		
		members.clear();
		
	}
	
	public void writeToNBT(NBTTagCompound tag)
	{
	}
	
	public int getTotalPower()
	{ return 0; }
	
	public int getRemainingPower()
	{ return 0; }
	
	public ArrayList<ChunkPos> getClaimedChunksForDim(int dimension)
	{
		ArrayList<ChunkPos> al = new ArrayList<ChunkPos>();
		
		for(int i = 0; i < claimedChunks.size(); i++)
		{
			ChunkPos p = claimedChunks.get(i);
			if(p.dim == dimension) al.add(p);
		}
		
		return al;
	}
	
	public ArrayList<Chunk> getClaimedChunks(World w)
	{
		ArrayList<Chunk> al = new ArrayList<Chunk>();
		
		for(int i = 0; i < claimedChunks.size(); i++)
		{
			ChunkPos p = claimedChunks.get(i);
			if(p.dim == w.provider.dimensionId)
				al.add(p.getChunk(w));
		}
		
		return al;
	}
	
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
	
	/*
	
	public boolean canFight(UUID attackingPlayer, UUID playerBeingAttacked)
	{}
	
	public boolean canFight(EntityPlayer attackingPlayer, EntityPlayer playerBeingAttacked)
	{ return canFight(attackingPlayer.getGameProfile().getId(), playerBeingAttacked.getGameProfile().getId()); }
	
	public boolean canEnter(UUID enteringPlayer)
	{}
	
	public boolean canEnter(EntityPlayer enteringPlayer)
	{ return canEnter(enteringPlayer.getUniqueID()); }
	
	public boolean canRightClickIn(UUID rightClickingPlayer)
	{}
	
	public boolean canRightClickIn(EntityPlayer rightClickingPlayer)
	{}
	
	public boolean canBreakOrPlaceBlocks(UUID placingOrBreakingPlayer)
	{}
	
	public boolean canBreakOrPlaceBlocks(EntityPlayer placingOrBreakingPlayer)
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
	*/
}