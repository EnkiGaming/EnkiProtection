package com.enkigaming.minecraft.forge.enkiprotection.util.exceptions;

import com.enkigaming.minecraft.forge.enkiprotection.factions.Faction;
import com.enkigaming.minecraft.forge.enkiprotection.util.ChunkPos;

public class ChunkNotInClaimException extends Exception
{
	private static final long serialVersionUID = 1L;
	
	public final Faction notIn;
	public final Faction in;
	public final ChunkPos chunk;
	
	public ChunkNotInClaimException(Faction c1, Faction c2, ChunkPos c)
	{
		super();
		
		notIn = c1;
		in = c2;
		chunk = c;
	}
}