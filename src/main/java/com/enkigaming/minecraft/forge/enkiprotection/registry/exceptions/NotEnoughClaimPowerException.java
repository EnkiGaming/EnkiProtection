package com.enkigaming.minecraft.forge.enkiprotection.registry.exceptions;

import com.enkigaming.minecraft.forge.enkiprotection.registry.*;

public class NotEnoughClaimPowerException extends Exception
{
	private static final long serialVersionUID = 1L;
	
	public final Claim claim;
	public final int claimPower;
	public final int claimPowerRequired;
	public final ChunkCoOrdinate chunk;
	
	public NotEnoughClaimPowerException(Claim c, int pow, int req, ChunkCoOrdinate co)
	{
		super();
		
		claim = c;
		claimPower = pow;
		claimPowerRequired = req;
		chunk = co;
	}
}