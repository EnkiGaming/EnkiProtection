package com.enkigaming.minecraft.forge.enkiprotection.util.exceptions;

import java.util.UUID;

public class GrantingMoreClaimPowerThanHaveException extends Exception
{
	private static final long serialVersionUID = 1L;
	
	public final UUID player;
	public final int claimPowerPlayerHasAvailable;
	public final int claimPowerAttemptingToGrant;
	public final int claimPowerPlayerHasTotal;
	
	public GrantingMoreClaimPowerThanHaveException(UUID playerID, int available, int attmpting, int total)
	{
		player = playerID;
		claimPowerPlayerHasAvailable = available;
		claimPowerAttemptingToGrant = attmpting;
		claimPowerPlayerHasTotal = total;
	}
}