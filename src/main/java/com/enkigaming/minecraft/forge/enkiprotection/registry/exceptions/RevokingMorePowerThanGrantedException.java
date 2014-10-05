package com.enkigaming.minecraft.forge.enkiprotection.registry.exceptions;

import com.enkigaming.minecraft.forge.enkiprotection.registry.Claim;
import java.util.UUID;

public class RevokingMorePowerThanGrantedException extends Exception
{
	private static final long serialVersionUID = 1L;
	
	public final Claim claim;
	public final UUID player;
	public final int powerAttemptingToRevoke;
	public final int powerPlayerHasGranted;

	public RevokingMorePowerThanGrantedException(Claim c, UUID playerID, int rev, int has)
	{
		claim = c;
		player = playerID;
		powerAttemptingToRevoke = rev;
		powerPlayerHasGranted = has;
	}
}