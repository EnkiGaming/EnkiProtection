package com.enkigaming.minecraft.forge.enkiprotection.registry.exceptions;

import com.enkigaming.minecraft.forge.enkiprotection.registry.*;

public class ChunkNotInClaimException extends Exception
{
	private static final long serialVersionUID = 1L;
	
	public final Claim notIn;
	public final Claim in;
	public final ChunkCoOrdinate chunk;
	
	public ChunkNotInClaimException(Claim c1, Claim c2, ChunkCoOrdinate c)
	{
		super();
		
		notIn = c1;
		in = c2;
		chunk = c;
	}
}