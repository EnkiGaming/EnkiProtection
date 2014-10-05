package com.enkigaming.minecraft.forge.enkiprotection.util.exceptions;

import com.enkigaming.minecraft.forge.enkiprotection.factions.Faction;
import com.enkigaming.minecraft.forge.enkiprotection.util.ChunkPos;

public class ChunkAlreadyClaimedException extends Exception
{
	private static final long serialVersionUID = 1L;
	
	public final Faction attemptingToClaim;
	public final Faction alreadyBelongsTo;
	public final ChunkPos chunk;
	
	public ChunkAlreadyClaimedException(Faction c1, Faction c2, ChunkPos c)
	{
		super();
		attemptingToClaim = c1;
		alreadyBelongsTo = c2;
		chunk = c;
	}
}