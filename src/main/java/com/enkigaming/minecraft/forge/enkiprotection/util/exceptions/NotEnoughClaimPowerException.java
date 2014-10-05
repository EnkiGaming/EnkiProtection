package com.enkigaming.minecraft.forge.enkiprotection.util.exceptions;

import com.enkigaming.minecraft.forge.enkiprotection.factions.Faction;
import com.enkigaming.minecraft.forge.enkiprotection.util.ChunkPos;

public class NotEnoughClaimPowerException extends Exception
{
	private static final long serialVersionUID = 1L;
	
	public final Faction claim;
	public final int claimPower;
	public final int claimPowerRequired;
	public final ChunkPos chunk;
	
	public NotEnoughClaimPowerException(Faction c, int pow, int req, ChunkPos co)
	{
		super();
		
		claim = c;
		claimPower = pow;
		claimPowerRequired = req;
		chunk = co;
	}
}