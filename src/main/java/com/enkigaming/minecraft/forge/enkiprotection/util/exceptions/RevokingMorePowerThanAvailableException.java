package com.enkigaming.minecraft.forge.enkiprotection.util.exceptions;

import com.enkigaming.minecraft.forge.enkiprotection.factions.Faction;

public class RevokingMorePowerThanAvailableException extends Exception
{
	private static final long serialVersionUID = 1L;
	
	public final Faction claim;
	public final int availablePower;
	public final int powerAttemptingToRevoke;
	
	public RevokingMorePowerThanAvailableException(Faction c, int pow, int att)
	{
		claim = c;
		availablePower = pow;
		powerAttemptingToRevoke = att;
	}
}