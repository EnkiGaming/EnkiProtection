package com.enkigaming.minecraft.forge.enkiprotection.commandlisteners;

import com.enkigaming.minecraft.forge.enkiprotection.Permissions;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import org.apache.commons.lang3.NotImplementedException;

public class CmdClaim extends CommandBase
{
    protected static enum HelpOption
    {
        claim("Base command for claim-related commands.",
              "Claim <Subcommand> <Subcommand-specific arguments>",
              "Create, Delete, Leave, Invite, Accept, Join, Player, Chunk, Setting"),
        
        claimCreate("Creates a new claim.",
                    "Claim create <Claim name>",
                    null),
        
        claimDelete("Deletes an existing claim.",
                    "Claim delete <Claim name>",
                    null),
        
        claimLeave("Removes yourself as a member of a claim.",
                   "Claim leave <Claim name>",
                   null),
        
        claimAccept("Accepts an offer to join the claim as a member.",
                    "Claim accept <Claim name>",
                    null),
        
        claimJoin("Joins a claim as a member, or sends a request to.",
                  "Claim join <Claim name>",
                  null),
        
        claimPlayer("Base command for player-related claim commands.",
                    "Claim player <Subcommand> <Claim name> <Player name>",
                    "Ally, Ban"),
        
        claimPlayerInvite("Invites another player to join the claim as a member.",
                          "Claim player invite <Claim name> <Player name>",
                          null),
        
        claimPlayerCancelinvitation("Cancels any outstanding invitations for a player to join a claim as a member.",
                                    "Claim player cancelinvitation <Claim name> <Player name>",
                                    null),
        
        claimPlayerAlly("Sets a player as an ally of the claim.",
                        "Claim player ally <Claim name> <Player name>",
                        null),
        
        claimPlayerBan("Bans a player from the claim.",
                       "Claim player ban <Claim name> <Player name>",
                       null),
        
        claimChunk("Base command for chunk-related claim commands.",
                   "Claim chunk <Subcommand> <Claim name>",
                   "Add, Remove, Autoadd, Autoremove"),
        
        claimChunkAdd("Adds the chunk the player is in to the claim if it has enough available power.",
                      "Claim chunk add <Claim name>",
                      null),
        
        claimChunkRemove("Removes the chunk the player is in from the claim.",
                         "Claim chunk remove <Claim name>",
                         null),
        
        claimSetting("Base command for claim settings.",
                     "Claim setting <setting> <value(s)>",
                     "AllowExplosions, AllowFriendlyCombat, AllowPlayerCombat, AllowMobEntry, AllowNonAllyEntry, "
                     + "AllowEntry, AllowNonAllyInteractWithBlocks, AllowBlockInteraction, "
                     + "AllowNonAllyInteractWithEntities, AllowEntityInteraction, AllowInteraction, "
                     + "AllowNonAllyPlaceOrBreak, AllowNonAllyBuild, AllowPlaceOrBreak, AllowBuild"),
        
        claimSettingAllowExplosions("Sets whether explosions affect blocks or entities in the claim.",
                                    "Claim setting allowexplosions <Claim name> <true/false>",
                                    null),
        
        claimSettingAllowfriendlycombat("Sets whether claim owners/members/allies can fight eachother.",
                                        "Claim setting allowfriendlycombat <Claim name> <true/false>",
                                        null),
        
        claimSettingAllowplayercombat("Sets whether any players can fight eachother.",
                                      "Claim setting allowplayercombat <Claim name> <true/false>",
                                      null),
        
        claimSettingAllowmobentry("Sets whether mobs can enter/spawn in the claim.",
                                  "Claim setting allowmobentry <Claim name> <true/false>",
                                  null),
        
        claimSettingAllownonallyentry("Sets whether non-allies can enter the claim.",
                                     "Claim setting allownonallyentry <Claim name> <true/false>",
                                     null),
        
        claimSettingAllowEntry("Sets whether non-allies can enter the claim.",
                               "Claim setting allowentry <Claim name> <true/false>",
                               null),
        
        claimSettingAllownonallyinteractwithblocks("Sets whether non-allies can interact with blocks.",
                                                   "Claim setting allownonallyinteractwithblocks <Claim name> <true/false>",
                                                   null),
        
        claimSettingAllowblockinteraction("Sets whether non-allies can interact with blocks.",
                                          "Claim setting allowblockinteraction <Claim name> <true/false>",
                                          null),
        
        claimSettingAllownonallyinteractwithentities("Sets whether non-allies can interact with entities.",
                                                     "Claim setting allownonallyinteractwithentities <Claim name> <true/false>",
                                                     null),
        
        claimSettingAllowentityinteraction("Sets whether non-allies can interact with entities.",
                                           "Claim setting allowentityinteraction <Claim name> <true/false>",
                                           null),
        
        claimSettingAllowinteraction("Sets whether non-allies can interact with blocks or entities.",
                                     "Claim setting allowinteraction <Claim name> <true/false>",
                                     null),
        
        claimSettingAllownonallyplaceorbreak("Sets whether non-allies can modify the terrain.",
                                             "Claim setting allownonallyplaceorbreak <Claim name> <true/false>",
                                             null),
        
        claimSettingAllownonallybuild("Sets whether non-allies can modify the terrain.",
                                      "Claim setting allownonallybuild <Claim name> <true/false>",
                                      null),
        
        claimSettingAllowplaceorbreak("Sets whether non-allies can modify the terrain.",
                                      "Claim setting allowplaceorbreak <Claim name> <true/false>",
                                      null),
        
        claimSettingAllowbuild("Sets whether non-allies can modify the terrain.",
                               "Claim setting allowbuild <Claim name> <true/false>",
                               null);
        
        HelpOption(String description, String usage, String subcommands)
        {
            this.description = description;
            this.usage = usage;
            this.subcommands = subcommands;
        }
        
        String description;
        String usage;
        String subcommands;
        
        public String getDescription()
        { return description; }
        
        public String getUsage()
        { return usage; }
        
        public String getSubcommands()
        { return subcommands; }
    }
    
    final String usageText = "Usage: ";
    final String subcommandsText = "Subcommands: ";

    @Override
    public String getCommandName()
    { return "Claim"; }

    @Override
    public String getCommandUsage(ICommandSender sender)
    { return "Subcommands: create, delete, leave, invite, accept, join, player, chunk, setting. Type /claim help <subcommand> for help with it."; }
    
    @Override
    public List getCommandAliases()
    { return Arrays.asList("claim", "CLAIM"); }

    @Override
    public void processCommand(ICommandSender sender, String[] args)
    { handleClaim(sender, new ArrayList<String>(Arrays.asList(args))); }
    
    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender)
    { return true; } // Permissions are on a subcommand level.
    
    @Override
    public List addTabCompletionOptions(ICommandSender sender, String[] args)
    {
        throw new NotImplementedException("Not yet implemented.");
    }
    
    protected void sendSenderUsage(ICommandSender sender, HelpOption help)
    {
        String usage = help.getUsage();
        String subcommands = help.getSubcommands();
        
        if(usage != null)
            sender.addChatMessage(new ChatComponentText(usageText + usage));
        
        if(subcommands != null)
            sender.addChatMessage(new ChatComponentText(subcommandsText + subcommands));
    }
    
    protected void sendSenderHelp(ICommandSender sender, HelpOption help)
    {
        String description = help.getDescription();
        
        if(description != null)
            sender.addChatMessage(new ChatComponentText(description));
        
        sendSenderUsage(sender, help);
    }
    
    protected boolean checkPermission(ICommandSender sender, String permission)
    {
        if(sender instanceof EntityPlayer)
            if(!Permissions.hasPermission((EntityPlayer)sender, permission))
            {
                sender.addChatMessage(new ChatComponentText("You don't have permission to do that."));
                return false;
            }
        
        return true;
    }
    
    // ========================================================================================================
    // ===============Below this are the handle methods, which handle every possible subcommand.===============
    // ========================================================================================================
    
    protected void handleClaim(ICommandSender sender, List<String> args)
    {
        if(args.size() <= 0)
            sendSenderUsage(sender, HelpOption.claim);
        else if(args.get(0).equalsIgnoreCase("create"))
            handleClaimCreate(sender, args.subList(1, args.size()));
        else if(args.get(0).equalsIgnoreCase("delete"))
            handleClaimDelete(sender, args.subList(1, args.size()));
        else if(args.get(0).equalsIgnoreCase("leave"))
            handleClaimLeave(sender, args.subList(1, args.size()));
        else if(args.get(0).equalsIgnoreCase("accept"))
            handleClaimAccept(sender, args.subList(1, args.size()));
        else if(args.get(0).equalsIgnoreCase("player"))
            handleClaimPlayer(sender, args.subList(1, args.size()));
        else if(args.get(0).equalsIgnoreCase("chunk"))
            handleClaimChunk(sender, args.subList(1, args.size()));
        else if(args.get(0).equalsIgnoreCase("setting"))
            handleClaimSetting(sender, args.subList(1, args.size()));
        else
            sendSenderUsage(sender, HelpOption.claim);
    }
    
    protected void handleClaimCreate(ICommandSender sender, List<String> args)
    {}
    
    protected void handleClaimDelete(ICommandSender sender, List<String> args)
    {}
    
    protected void handleClaimLeave(ICommandSender sender, List<String> args)
    {}
    
    protected void handleClaimInvite(ICommandSender sender, List<String> args)
    {}
    
    protected void handleClaimAccept(ICommandSender sender, List<String> args)
    {}
    
    protected void handleClaimPlayer(ICommandSender sender, List<String> args)
    {}
    
    protected void handleClaimPlayerInvite(ICommandSender sender, List<String> args)
    {}
    
    protected void handleClaimPlayerCancelinvitation(ICommandSender sender, List<String> args)
    {}
    
    protected void handleClaimPlayerAlly(ICommandSender sender, List<String> args)
    {}
    
    protected void handleClaimPlayerBan(ICommandSender sender, List<String> args)
    {}
    
    protected void handleClaimChunk(ICommandSender sender, List<String> args)
    {}
    
    protected void handleClaimChunkAdd(ICommandSender sender, List<String> args)
    {}
    
    protected void handleClaimChunkRemove(ICommandSender sender, List<String> args)
    {}
    
    protected void handleClaimChunkAutoadd(ICommandSender sender, List<String> args)
    {}
    
    protected void handleClaimChunkAutoremove(ICommandSender sender, List<String> args)
    {}
    
    protected void handleClaimSetting(ICommandSender sender, List<String> args)
    {}
    
    protected void handleClaimSettingAllowexplosions(ICommandSender sender, List<String> args)
    {}
    
    protected void handleClaimSettingAllowfriendlycombat(ICommandSender sender, List<String> args)
    {}
    
    protected void handleClaimSettingAllowplayercombat(ICommandSender sender, List<String> args)
    {}
    
    protected void handleClaimSettingAllowmobentry(ICommandSender sender, List<String> args)
    {}
    
    protected void handleClaimSettingAllownonallyentry(ICommandSender sender, List<String> args)
    {}
    
    protected void handleClaimSettingAllownonallyinteractwithblocks(ICommandSender sender, List<String> args)
    {}
    
    protected void handleClaimSettingAllownonallyinteractwithentities(ICommandSender sender, List<String> args)
    {}
    
    protected void handleClaimSettingAllownonallyinteraction(ICommandSender sender, List<String> args)
    {}
    
    protected void handleClaimSettingAllownonallyplaceorbreak(ICommandSender sender, List<String> args)
    {}
    
    protected void handleClaimHelp(ICommandSender sender, List<String> args)
    {}
    
    protected void handleClaimHelpCreate(ICommandSender sender, List<String> args)
    {}
    
    protected void handleClaimHelpDelete(ICommandSender sender, List<String> args)
    {}
    
    protected void handleClaimHelpLeave(ICommandSender sender, List<String> args)
    {}
    
    protected void handleClaimHelpAccept(ICommandSender sender, List<String> args)
    {}
    
    protected void handleClaimHelpJoin(ICommandSender sender, List<String> args)
    {}
    
    protected void handleClaimHelpPlayer(ICommandSender sender, List<String> args)
    {}
    
    protected void handleClaimHelpPlayerInvite(ICommandSender sender, List<String> args)
    {}
    
    protected void handleClaimHelpPlayerAlly(ICommandSender sender, List<String> args)
    {}
    
    protected void handleClaimHelpPlayerBan(ICommandSender sender, List<String> args)
    {}
    
    protected void handleClaimHelpChunk(ICommandSender sender, List<String> args)
    {}
    
    protected void handleClaimHelpChunkAdd(ICommandSender sender, List<String> args)
    {}
    
    protected void handleClaimHelpChunkRemove(ICommandSender sender, List<String> args)
    {}
    
    protected void handleClaimHelpSetting(ICommandSender sender, List<String> args)
    {}
    
    protected void handleClaimHelpSettingAllowexplosions(ICommandSender sender, List<String> args)
    {}
    
    protected void handleClaimHelpSettingAllowfriendlycombat(ICommandSender sender, List<String> args)
    {}
    
    protected void handleClaimHelpSettingAllowplayercombat(ICommandSender sender, List<String> args)
    {}
    
    protected void handleClaimHelpSettingAllowmobentry(ICommandSender sender, List<String> args)
    {}
    
    protected void handleClaimHelpSettingAllownonallyentry(ICommandSender sender, List<String> args)
    {}
    
    protected void handleClaimHelpSettingAllowentry(ICommandSender sender, List<String> args)
    {}
    
    protected void handleClaimHelpSettingAllownonallyinteractwithblocks(ICommandSender sender, List<String> args)
    {}
    
    protected void handleClaimHelpSettingAllowblockinteraction(ICommandSender sender, List<String> args)
    {}
    
    protected void handleClaimHelpSettingAllownonallyinteractwithentities(ICommandSender sender, List<String> args)
    {}
    
    protected void handleClaimHelpSettingAllowentityinteraction(ICommandSender sender, List<String> args)
    {}
    
    protected void handleClaimHelpSettingAllowinteraction(ICommandSender sender, List<String> args)
    {}
    
    protected void handleClaimHelpSettingAllownonallyplaceorbreak(ICommandSender sender, List<String> args)
    {}
    
    protected void handleClaimHelpSettingAllownonallybuild(ICommandSender sender, List<String> args)
    {}
    
    protected void handleClaimHelpSettingAllowplaceorbreak(ICommandSender sender, List<String> args)
    {}
    
    protected void handleClaimHelpSettingAllowbuild(ICommandSender sender, List<String> args)
    {}
    
    protected void handleClaimHelpHelp(ICommandSender sender, List<String> args)
    {}
}