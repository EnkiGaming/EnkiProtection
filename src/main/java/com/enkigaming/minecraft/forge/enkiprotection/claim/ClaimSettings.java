package com.enkigaming.minecraft.forge.enkiprotection.claim;

public class ClaimSettings
{
    public ClaimSettings()
    {}
    
    String welcomeMessage = null;
    
    boolean allowExplosions = true;
    boolean allowFriendlyCombat = true;
    boolean allowPlayerCombat = true;
    boolean allowMobEntry = true;
    boolean allowNonAllyEntry = true;
    boolean allowNonAllyInteractWithBlocks = true;
    boolean allowNonAllyInteractWithEntities = true;
    boolean allowNonAllyBreakOrPlaceBlocks = true;
    
    /**
     * Gets the message that appears when a player enters a claim.
     * @return The welcome message.
     */
    public String getWelcomeMessage()
    { return welcomeMessage; }
    
    /**
     * Gets whether explosions (TNT, creeper, etc.) are allowed.
     * @return Whether explosions are allowed.
     */
    public boolean explosionsAreAllowed()
    { return allowExplosions; }
    
    /**
     * Gets whether combat is allowed between the owner/members/players.
     * @return Whether friendly combat is allowed.
     */
    public boolean friendlyCombatIsAllowed()
    { return allowFriendlyCombat; }
    
    /**
     * Gets whether players can fight eachother.
     * @return Whether friendly combat is allowed.
     */
    public boolean playerCombatIsAllowed()
    { return allowPlayerCombat; }
    
    /**
     * Whether mobs can spawn in/enter the claim
     * @return Whether mobs are allowed in.
     */
    public boolean mobsAreAllowedIn()
    { return allowMobEntry; }
    
    public boolean nonAlliesAreAllowedIn()
    { return allowNonAllyEntry; }
    
    /**
     * Whether non-allied or better players can interact (right click, etc.) with blocks.
     * @return Whether non-allies can interact with blocks.
     */
    public boolean nonAlliesCanInteractWithBlocks()
    { return allowNonAllyInteractWithBlocks; }
    
    /**
     * Whether non-allied or better players can interact (right click, etc.) with entities.
     * @return Whether non-allies can interact with blocks.
     */
    public boolean nonAlliesCanInteractWithEntities()
    { return allowNonAllyInteractWithEntities; }
    
    /**
     * Whether non-allied or better players can break or place blocks.
     * @return Whether non-allies can break or place blocks.
     */
    public boolean nonAlliesCanBreakOrPlaceBlocks()
    { return allowNonAllyBreakOrPlaceBlocks; }
    
    /**
     * Sets the welcome message that players see upon entering a claim.
     * @param newWelcomeMessage The welcome message to display.
     * @return The old welcome message.
     */
    public String setWelcomeMessage(String newWelcomeMessage)
    {
        String old = welcomeMessage;
        welcomeMessage = newWelcomeMessage;
        return old;
    }
    
    /**
     * Sets whether or not to allow explosions.
     * @param newValue The new value of the setting.
     * @return The old value.
     */
    public boolean setAllowExplosions(boolean newValue)
    {
        boolean old = newValue;
        allowExplosions = newValue;
        return old;
    }
    
    /**
     * Sets whether or not to allow friendly combat.
     * @param newValue The new value of the setting.
     * @return The old value.
     */
    public boolean setAllowFriendlyCombat(boolean newValue)
    {
        boolean old = newValue;
        allowFriendlyCombat = newValue;
        return old;
    }
    
    /**
     * Sets whether or not to allow player combat.
     * @param newValue The new value of the setting.
     * @return The old value.
     */
    public boolean setAllowPlayerCombat(boolean newValue)
    {
        boolean old = newValue;
        allowPlayerCombat = newValue;
        return old;
    }
    
    /**
     * Sets whether or not to allow mobs in.
     * @param newValue The new value of the setting.
     * @return The old value.
     */
    public boolean setAllowMobEntry(boolean newValue)
    {
        boolean old = newValue;
        allowMobEntry = newValue;
        return old;
    }
    
    public boolean setAllowNonAllyEntry(boolean newValue)
    {
        boolean old = newValue;
        allowNonAllyEntry = newValue;
        return old;
    }
    
    /**
     * Sets whether or not to allow non-ally interaction with blocks.
     * @param newValue The new value of the setting.
     * @return The old value.
     */
    public boolean setAllowNonAllyInteractWithBlocks(boolean newValue)
    {
        boolean old = newValue;
        allowNonAllyInteractWithBlocks = newValue;
        return old;
    }
    
    /**
     * Sets whether or not to allow non-ally interaction with entities.
     * @param newValue The new value of the setting.
     * @return The old value.
     */
    public boolean setAllowNonAllyInteractWithEntities(boolean newValue)
    {
        boolean old = newValue;
        allowNonAllyInteractWithEntities = newValue;
        return old;
    }
    
    /**
     * Sets whether or not to allow non-ally placing or breaking or blocks.
     * @param newValue The new value of the setting.
     * @return The old value.
     */
    public boolean setAllowNonAllyBreakOrPlaceBlocks(boolean newValue)
    {
        boolean old = newValue;
        allowNonAllyBreakOrPlaceBlocks = newValue;
        return old;
    }
}