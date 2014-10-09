package com.enkigaming.minecraft.forge.enkiprotection;

public class Strings
{
    public static String getStringCantBreakBlocks(String claimName)
    { return "You may not break blocks in " + claimName + "."; }
    
    public static String getStringCantPlaceBlocks(String claimName)
    { return "You may not place blocks in " + claimName + "."; }
    
    public static String getStringCantInteractWithBlocks(String claimName)
    { return "You may not interact with blocks in " + claimName + "."; }
    
    public static String getStringCantInteractWithEntities(String claimName)
    { return "You may not interact with entities in " + claimName + "."; }
    
    public static String getStringPlayerMayNotEnterClaim(String claimName)
    { return "You may not enter " + claimName; }
    
    public static String getStringPlayerSpawnedInClaimedNotAllowedChunk(String claimName)
    { return "Your spawn point was in " + claimName + ", which you may not enter. You have been spawned at the server's overworld spawnpoint instead."; }
}