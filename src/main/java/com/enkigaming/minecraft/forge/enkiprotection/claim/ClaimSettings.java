package com.enkigaming.minecraft.forge.enkiprotection.claim;

import com.enkigaming.mcforge.enkilib.filehandling.TreeFileHandler.TreeNode;
import com.enkigaming.minecraft.forge.enkiprotection.utils.Utils;
import java.util.Arrays;

public class ClaimSettings
{
    public ClaimSettings()
    {}
    
    public ClaimSettings(TreeNode node)
    {
        this();
        
        for(TreeNode subnode : node.getChildren())
        {
            if(     subnode.getName().toUpperCase().startsWith(welcomeMessageTag                  .toUpperCase())) // <editor-fold desc="{ }">
            {
                String[] welcomeMessageParts = subnode.getName().split("\\Q" + separator + "\\E");
                
                if(welcomeMessageParts.length < 2)
                {
                    System.out.println("Could not parse \"" + subnode.getName() + "\" into a string for the welcome message.");
                    continue;
                }
                
                welcomeMessage = welcomeMessageParts[welcomeMessageParts.length - 1];
            } // </editor-fold>
            else if(subnode.getName().toUpperCase().startsWith(allowExplosionsTag                 .toUpperCase())) // <editor-fold desc="{ }">
            {
                Boolean allowExplosions = getBooleanFromNodeName(subnode.getName());
                
                if(allowExplosions == null)
                {
                    System.out.println("Could not parse \"" + subnode.getName() + "\" into a boolean");
                    continue;
                }
                
                this.allowExplosions = allowExplosions;
            } // </editor-fold>
            else if(subnode.getName().toUpperCase().startsWith(allowFriendlyCombatTag             .toUpperCase())) // <editor-fold desc="{ }">
            {
                Boolean allowFriendlyCombat = getBooleanFromNodeName(subnode.getName());
                
                if(allowFriendlyCombat == null)
                {
                    System.out.println("Could not parse \"" + subnode.getName() + "\" into a boolean");
                    continue;
                }
                
                this.allowFriendlyCombat = allowFriendlyCombat;
            } // </editor-fold>
            else if(subnode.getName().toUpperCase().startsWith(allowPlayerCombatTag               .toUpperCase())) // <editor-fold desc="{ }">
            {
                Boolean allowPlayerCombat = getBooleanFromNodeName(subnode.getName());
                
                if(allowPlayerCombat == null)
                {
                    System.out.println("Could not parse \"" + subnode.getName() + "\" into a boolean");
                    continue;
                }
                
                this.allowExplosions = allowPlayerCombat;
            } // </editor-fold>
            else if(subnode.getName().toUpperCase().startsWith(allowMobEntryTag                   .toUpperCase())) // <editor-fold desc="{ }">
            {
                Boolean allowMobEntry = getBooleanFromNodeName(subnode.getName());
                
                if(allowMobEntry == null)
                {
                    System.out.println("Could not parse \"" + subnode.getName() + "\" into a boolean");
                    continue;
                }
                
                this.allowMobEntry = allowMobEntry;
            } // </editor-fold>
            else if(subnode.getName().toUpperCase().startsWith(allowNonAllyEntryTag               .toUpperCase())) // <editor-fold desc="{ }">
            {
                Boolean allowNonAllyEntry = getBooleanFromNodeName(subnode.getName());
                
                if(allowNonAllyEntry == null)
                {
                    System.out.println("Could not parse \"" + subnode.getName() + "\" into a boolean");
                    continue;
                }
                
                this.allowNonAllyEntry = allowNonAllyEntry;
            } // </editor-fold>
            else if(subnode.getName().toUpperCase().startsWith(allowNonAllyInteractWithBlocksTag  .toUpperCase())) // <editor-fold desc="{ }">
            {
                Boolean allowNonAllyInteractWithBlocks = getBooleanFromNodeName(subnode.getName());
                
                if(allowNonAllyInteractWithBlocks == null)
                {
                    System.out.println("Could not parse \"" + subnode.getName() + "\" into a boolean");
                    continue;
                }
                
                this.allowNonAllyInteractWithBlocks = allowNonAllyInteractWithBlocks;
            } // </editor-fold>
            else if(subnode.getName().toUpperCase().startsWith(allowNonAllyInteractWithEntitiesTag.toUpperCase())) // <editor-fold desc="{ }">
            {
                Boolean allowNonAllyInteractWithEntities = getBooleanFromNodeName(subnode.getName());
                
                if(allowNonAllyInteractWithEntities == null)
                {
                    System.out.println("Could not parse \"" + subnode.getName() + "\" into a boolean");
                    continue;
                }
                
                this.allowNonAllyInteractWithEntities = allowNonAllyInteractWithEntities;
            } // </editor-fold>
            else if(subnode.getName().toUpperCase().startsWith(allowNonAllyBreakOrPlaceBlocksTag  .toUpperCase())) // <editor-fold desc="{ }">
            {
                Boolean allowNonAllyBreakOrPlaceBlocks = getBooleanFromNodeName(subnode.getName());
                
                if(allowNonAllyBreakOrPlaceBlocks == null)
                {
                    System.out.println("Could not parse \"" + subnode.getName() + "\" into a boolean");
                    continue;
                }
                
                this.allowNonAllyBreakOrPlaceBlocks = allowNonAllyBreakOrPlaceBlocks;
            } // </editor-fold>
        }
    }
    
    protected String welcomeMessage = null;
    
    protected boolean allowExplosions = true;
    protected boolean allowFriendlyCombat = true;
    protected boolean allowPlayerCombat = true;
    protected boolean allowMobEntry = true;
    protected boolean allowNonAllyEntry = true;
    protected boolean allowNonAllyInteractWithBlocks = true;
    protected boolean allowNonAllyInteractWithEntities = true;
    protected boolean allowNonAllyBreakOrPlaceBlocks = true;
    
    protected static final String settingsTag = "Settings";
    protected static final String welcomeMessageTag = "Welcome message";
    protected static final String allowExplosionsTag = "Allow explosions";
    protected static final String allowFriendlyCombatTag = "Allow friendly combat";
    protected static final String allowPlayerCombatTag = "Allow combat";
    protected static final String allowMobEntryTag = "Allow mobs";
    protected static final String allowNonAllyEntryTag = "Allow non-ally entry";
    protected static final String allowNonAllyInteractWithBlocksTag = "Allow non-allies to interact with blocks";
    protected static final String allowNonAllyInteractWithEntitiesTag = "Allow non-allies to interact with entities";
    protected static final String allowNonAllyBreakOrPlaceBlocksTag = "Allow non-allies to break or place blocks";
    protected static final String separator = ": ";
    
    public TreeNode toTreeNode()
    {
        TreeNode baseNode = new TreeNode(settingsTag + separator);
        
        if(welcomeMessage != null)
        {
            TreeNode welcomeNode = new TreeNode(welcomeMessageTag + separator + welcomeMessage);
            baseNode.addChild(welcomeNode);
        }
        
        TreeNode allowExplosionsNode              = new TreeNode(allowExplosionsTag                  + separator + Boolean.toString(allowExplosions));
        TreeNode allowFriendlyCombatNode          = new TreeNode(allowFriendlyCombatTag              + separator + Boolean.toString(allowFriendlyCombat));
        TreeNode allowPlayerCombatNode            = new TreeNode(allowPlayerCombatTag                + separator + Boolean.toString(allowPlayerCombat));
        TreeNode allowMobEntryNode                = new TreeNode(allowMobEntryTag                    + separator + Boolean.toString(allowMobEntry));
        TreeNode allowNonAllyEntryNode            = new TreeNode(allowNonAllyEntryTag                + separator + Boolean.toString(allowNonAllyEntry));
        TreeNode allowNonAllyInteractBlocksNode   = new TreeNode(allowNonAllyInteractWithBlocksTag   + separator + Boolean.toString(allowNonAllyInteractWithBlocks));
        TreeNode allowNonAllyInteractEntitiesNode = new TreeNode(allowNonAllyInteractWithEntitiesTag + separator + Boolean.toString(allowNonAllyInteractWithEntities));
        TreeNode allowNonAllyBreakPlaceNode       = new TreeNode(allowNonAllyBreakOrPlaceBlocksTag   + separator + Boolean.toString(allowNonAllyBreakOrPlaceBlocks));
        
        baseNode.addChildren(Arrays.asList(allowExplosionsNode,              allowFriendlyCombatNode,
                                           allowPlayerCombatNode,            allowMobEntryNode,
                                           allowNonAllyEntryNode,            allowNonAllyInteractBlocksNode,
                                           allowNonAllyInteractEntitiesNode, allowNonAllyInteractBlocksNode));
        
        return baseNode;
        
        // This seems like the cleanest-looking toTreeNode method I've written xD
    }
    
    public static ClaimSettings fromTreeNode(TreeNode node)
    { return new ClaimSettings(node); }
    
    static Boolean getBooleanFromNodeName(String nodeName)
    {
        Boolean result;
        String[] nameParts = nodeName.split("\\Q" + separator + "\\E");
        
        if(nameParts.length < 2)
            return null;
        
        String name = nameParts[nameParts.length - 1].trim();
        
        if(Utils.stringParsesToTrue(name))
            return true;
        
        if(Utils.stringParsesToFalse(name))
            return false;
        
        return null;
    }
    
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