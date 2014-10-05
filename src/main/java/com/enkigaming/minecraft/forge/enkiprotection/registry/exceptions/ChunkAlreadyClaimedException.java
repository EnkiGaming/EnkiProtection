package com.enkigaming.minecraft.forge.enkiprotection.registry.exceptions;

import com.enkigaming.minecraft.forge.enkiprotection.registry.*;

public class ChunkAlreadyClaimedException extends Exception
{
	private static final long serialVersionUID = 1L;
	
	public final Claim attemptingToClaim;
	public final Claim alreadyBelongsTo;
	public final ChunkCoOrdinate chunk;
	
	public ChunkAlreadyClaimedException(Claim c1, Claim c2, ChunkCoOrdinate c)
	{
		super();
		attemptingToClaim = c1;
		alreadyBelongsTo = c2;
		chunk = c;
	}
}