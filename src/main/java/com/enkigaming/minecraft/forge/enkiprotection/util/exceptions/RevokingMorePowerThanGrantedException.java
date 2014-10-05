package com.enkigaming.minecraft.forge.enkiprotection.util.exceptions;

import java.util.UUID;

import com.enkigaming.minecraft.forge.enkiprotection.factions.Faction;

public class RevokingMorePowerThanGrantedException extends Exception
{
	private static final long serialVersionUID = 1L;
	
	public final Faction claim;
	public final UUID player;
	public final int powerAttemptingToRevoke;
	public final int powerPlayerHasGranted;

	public RevokingMorePowerThanGrantedException(Faction c, UUID playerID, int rev, int has)
	{
		claim = c;
		player = playerID;
		powerAttemptingToRevoke = rev;
		powerPlayerHasGranted = has;
	}
}