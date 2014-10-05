package com.enkigaming.minecraft.forge.enkiprotection.factions;

import java.util.UUID;

import net.minecraft.nbt.NBTTagCompound;

public class FactionMember
{
	public final UUID playerID;
	public FactionRank rank;
	public int chunksClaimed;
	public long lastPowerRestore;
	
	public FactionMember(UUID id)
	{ playerID = id; }
	
	public void readFromNBT(NBTTagCompound tag)
	{
		rank = FactionRank.get(tag.getString("Rank"));
		chunksClaimed = tag.getShort("ChunksClaimed");
		lastPowerRestore = tag.getLong("LastPowerRestore");
	}
	
	public void writeToNBT(NBTTagCompound tag)
	{
		tag.setString("Rank", rank.ID);
		tag.setShort("ChunksClaimed", (short)chunksClaimed);
		tag.setLong("LastPowerRestore", lastPowerRestore);
	}
}