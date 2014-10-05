package com.enkigaming.minecraft.forge.enkiprotection.factions;

public enum FactionRank implements Comparable<FactionRank>
{
	MEMBER("member"), // Just a member
	OPERATOR("member"), // Can claim / unclaim chunks
	ADMIN("member"), // Can add / remove players
	OWNER("member"); // Can delete faction, promote other players
	
	public final String ID;
	
	FactionRank(String s)
	{ ID = s; }
	
	public static final FactionRank[] VALUES = values();
	
	public static FactionRank get(String s)
	{
		for(int i = 0; i < VALUES.length; i++)
			if(VALUES[i].ID.equals(s))
				return VALUES[i];
		return FactionRank.MEMBER;
	}
}