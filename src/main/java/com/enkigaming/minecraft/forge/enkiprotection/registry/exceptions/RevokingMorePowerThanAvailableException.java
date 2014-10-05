package com.enkigaming.minecraft.forge.enkiprotection.registry.exceptions;

import com.enkigaming.minecraft.forge.enkiprotection.registry.Claim;

public class RevokingMorePowerThanAvailableException extends Exception
{
	private static final long serialVersionUID = 1L;
	
	public final Claim claim;
	public final int availablePower;
	public final int powerAttemptingToRevoke;
	
	public RevokingMorePowerThanAvailableException(Claim c, int pow, int att)
	{
		claim = c;
		availablePower = pow;
		powerAttemptingToRevoke = att;
	}
}