package com.enkigaming.minecraft.forge.enkiprotection.commandlisteners;

import com.enkigaming.mcforge.enkilib.EnkiLib;
import com.enkigaming.minecraft.forge.enkiprotection.EnkiProtection;
import com.enkigaming.minecraft.forge.enkiprotection.Permissions;
import com.enkigaming.minecraft.forge.enkiprotection.claim.Claim;
import com.enkigaming.minecraft.forge.enkiprotection.claim.ClaimPlayers;
import com.enkigaming.minecraft.forge.enkiprotection.claim.exceptions.ChunkNotPresentException;
import com.enkigaming.minecraft.forge.enkiprotection.registry.exceptions.AcceptedInvitationException;
import com.enkigaming.minecraft.forge.enkiprotection.registry.exceptions.AcceptedRequestException;
import com.enkigaming.minecraft.forge.enkiprotection.registry.exceptions.ClaimNameAlreadyPresentException;
import com.enkigaming.minecraft.forge.enkiprotection.utils.ChunkCoOrdinate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChunkCoordinates;
import org.apache.commons.lang3.NotImplementedException;

public class CmdClaim extends CommandBase
{
    protected static enum HelpOption
    {
        claim("Base command for claim-related commands.",
              "Claim <Subcommand> <Subcommand-specific arguments>",
              "Create, Delete, Leave, Accept, Join, Rename, Player, Chunk, Setting"),
        
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
        
        claimRename("Renames a claim.",
                    "Claim rename <Claim name> <New claim name>",
                    null),
        
        claimPlayer("Base command for player-related claim commands.",
                    "Claim player <Subcommand> <Claim name> <Player name>",
                    "Invite, Cancelinvitation, Ally, Ban, Makeowner"),
        
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
        
        claimPlayerMakeowner("Makes a player the owner of a claim.",
                             "Claim player makeowner <Claim name> <Player name>",
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
                     "WelcomeMessage, AllowExplosions, AllowFriendlyCombat, AllowPlayerCombat, AllowMobEntry, "
                     + "AllowNonAllyEntry, AllowEntry, AllowNonAllyInteractWithBlocks, AllowBlockInteraction, "
                     + "AllowNonAllyInteractWithEntities, AllowEntityInteraction, AllowInteraction, "
                     + "AllowNonAllyPlaceOrBreak, AllowNonAllyBuild, AllowPlaceOrBreak, AllowBuild"),
        
        claimSettingWelcomemessage("Sets the message that appears when a player enters the claim.",
                                   "Claim setting welcomemessage <New message>",
                                   null),
        
        claimSettingAllowexplosions("Sets whether explosions affect blocks or entities in the claim.",
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
    { return "Subcommands: create, delete, leave, accept, join, rename, player, chunk, setting. Type /claim help <subcommand> for help with it."; }
    
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
    
    protected boolean ensureIsPlayer(ICommandSender sender)
    {
        if(!(sender instanceof EntityPlayer))
        {
            sender.addChatMessage(new ChatComponentText("This command can only be performed by players."));
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
        else if(args.get(0).equalsIgnoreCase("join"))
            handleClaimJoin(sender, args.subList(1, args.size()));
        else if(args.get(0).equalsIgnoreCase("rename"))
            handleClaimRename(sender, args.subList(1, args.size()));
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
    {
        if(!ensureIsPlayer(sender))
            return;
        
        if(args.size() != 1)
        {
            sendSenderUsage(sender, HelpOption.claimCreate);
            return;
        }
        
        if(!checkPermission(sender, "enkiprotection.claim.create"))
            return;
        
        Claim claim;
        
        try
        { claim = EnkiProtection.getInstance().getClaims().createClaim(args.get(0)); }
        catch(ClaimNameAlreadyPresentException ex)
        {
            sender.addChatMessage(new ChatComponentText("A claim with the name " + args.get(0) + " already exists."));
            sendSenderUsage(sender, HelpOption.claimCreate);
            return;
        }
        
        claim.getPlayerManager().setOwner((EntityPlayer)sender);
        sender.addChatMessage(new ChatComponentText("Claim created! - " + claim.getName()));
    }
    
    protected void handleClaimDelete(ICommandSender sender, List<String> args)
    {
        if(args.size() != 1)
        {
            sendSenderUsage(sender, HelpOption.claimDelete);
            return;
        }
        
        Claim claim = EnkiProtection.getInstance().getClaims().getClaim(args.get(0));
        
        if(claim == null)
        {
            sender.addChatMessage(new ChatComponentText("No claim with the name " + args.get(0) + " exists."));
            sendSenderUsage(sender, HelpOption.claimDelete);
            return;
        }
        
        if((sender instanceof EntityPlayer) && !claim.canDelete((EntityPlayer)sender))
        {
            sender.addChatMessage(new ChatComponentText("You don't have permission to do that."));
            return;
        }
        
        if(!EnkiProtection.getInstance().getClaims().removeClaim(claim))
        {
            sender.addChatMessage(new ChatComponentText("Something weird happened. The claim may have been deleted twice rapidly."));
            sender.addChatMessage(new ChatComponentText("See if it's still there and try again if it is."));
            return;
        }
        
        sender.addChatMessage(new ChatComponentText("Claim deleted!"));
    }
    
    protected void handleClaimLeave(ICommandSender sender, List<String> args)
    {
        if(!ensureIsPlayer(sender))
            return;
        
        EntityPlayer player = (EntityPlayer)sender;
        
        if(args.size() != 1)
        {
            sendSenderUsage(sender, HelpOption.claimLeave);
            return;
        }
        
        Claim claim = EnkiProtection.getInstance().getClaims().getClaim(args.get(0));
        
        if(claim == null)
        {
            sender.addChatMessage(new ChatComponentText("No claim with the name " + args.get(0) + " exists."));
            sendSenderUsage(sender, HelpOption.claimLeave);
            return;
        }
        
        claim.getPlayerManager().makePlayerNonMember(player);
        sender.addChatMessage(new ChatComponentText("Left claim!"));
    }
    
    protected void handleClaimAccept(ICommandSender sender, List<String> args)
    {
        if(!ensureIsPlayer(sender))
            return;
        
        EntityPlayer player = (EntityPlayer)sender;
        
        if(args.size() != 1)
        {
            sendSenderUsage(sender, HelpOption.claimLeave);
            return;
        }
        
        Claim claim = EnkiProtection.getInstance().getClaims().getClaim(args.get(0));
        
        if(claim == null)
        {
            sender.addChatMessage(new ChatComponentText("No claim with the name " + args.get(0) + " exists."));
            sendSenderUsage(sender, HelpOption.claimAccept);
            return;
        }
        
        if(EnkiProtection.getInstance().getPendingInvitations().markInvitationAsAccepted(player.getGameProfile().getId(), claim.getId()))
        {
            claim.getPlayerManager().makePlayerMember(player);
            sender.addChatMessage(new ChatComponentText("Joined claim!"));
            return;
        }
        
        sender.addChatMessage(new ChatComponentText("You have not been invited to that claim."));
    }
    
    protected void handleClaimJoin(ICommandSender sender, List<String> args)
    {
        if(!ensureIsPlayer(sender))
            return;
        
        EntityPlayer player = (EntityPlayer)sender;
        
        if(args.size() != 1)
        {
            sendSenderUsage(sender, HelpOption.claimJoin);
            return;
        }
        
        Claim claim = EnkiProtection.getInstance().getClaims().getClaim(args.get(0));
        
        if(claim == null)
        {
            sender.addChatMessage(new ChatComponentText("No claim with the name " + args.get(0) + " exists."));
            sendSenderUsage(sender, HelpOption.claimJoin);
            return;
        }
        
        if(!claim.canJoin(player))
        {
            sender.addChatMessage(new ChatComponentText("You don't have permissions to join that claim."));
            return;
        }
        
        try
        {
            if(EnkiProtection.getInstance().getPendingInvitations().addRequest(player.getGameProfile().getId(), claim.getId()))
                sender.addChatMessage(new ChatComponentText("Sent request to join claim!"));
            else
                sender.addChatMessage(new ChatComponentText("Renewed claim to join claim!"));
        }
        catch(AcceptedInvitationException exception)
        {
            claim.getPlayerManager().makePlayerMember(player);
            sender.addChatMessage(new ChatComponentText("Joined claim!"));
        }
    }
    
    protected void handleClaimRename(ICommandSender sender, List<String> args)
    {
        if(args.size() != 2)
        {
            sendSenderUsage(sender, HelpOption.claimRename);
            return;
        }
        
        Claim claim = EnkiProtection.getInstance().getClaims().getClaim(args.get(0));
        
        if(claim == null)
        {
            sender.addChatMessage(new ChatComponentText("No claim with the name " + args.get(0) + " exists."));
            sendSenderUsage(sender, HelpOption.claimRename);
            return;
        }
        
        EntityPlayer player = null;
        
        if(sender instanceof EntityPlayer)
            player = (EntityPlayer)sender;
        
        if(player != null && !claim.canRename(player))
        {
            sender.addChatMessage(new ChatComponentText("You don't have permissions to rename that claim."));
            return;
        }
        
        claim.setName(args.get(1));
        sender.addChatMessage(new ChatComponentText("Claim name set! " + args.get(1)));
    }
    
    protected void handleClaimPlayer(ICommandSender sender, List<String> args)
    {
        if(args.size() <= 0)
            sendSenderUsage(sender, HelpOption.claimPlayer);
        else if(args.get(0).equalsIgnoreCase("invite"))
            handleClaimPlayerInvite(sender, args.subList(1, args.size()));
        else if(args.get(0).equalsIgnoreCase("cancelinvitation"))
            handleClaimPlayerCancelinvitation(sender, args.subList(1, args.size()));
        else if(args.get(0).equalsIgnoreCase("ally"))
            handleClaimPlayerAlly(sender, args.subList(1, args.size()));
        else if(args.get(0).equalsIgnoreCase("ban"))
            handleClaimPlayerBan(sender, args.subList(1, args.size()));
        else if(args.get(0).equalsIgnoreCase("makeowner"))
            handleClaimPlayerMakeowner(sender, args.subList(1, args.size()));
        else
            sendSenderUsage(sender, HelpOption.claimPlayer);
    }
    
    protected void handleClaimPlayerInvite(ICommandSender sender, List<String> args)
    {
        if(args.size() != 2)
        {
            sendSenderUsage(sender, HelpOption.claimRename);
            return;
        }
        
        Claim claim = EnkiProtection.getInstance().getClaims().getClaim(args.get(0));
        
        if(claim == null)
        {
            sender.addChatMessage(new ChatComponentText("No claim with the name " + args.get(0) + " exists."));
            sendSenderUsage(sender, HelpOption.claimPlayerInvite);
            return;
        }
        
        EntityPlayer player = null;
        
        if(sender instanceof EntityPlayer)
            player = (EntityPlayer)sender;
        
        if(player != null && !claim.canInvite(player))
        {
            sender.addChatMessage(new ChatComponentText("You don't have permissions to invite players to that claim."));
            return;
        }
        
        UUID invitedPlayerId = EnkiLib.getInstance().getUsernameCache().getLastRecordedUUIDForName(args.get(1));
        
        if(invitedPlayerId == null)
        {
            sender.addChatMessage(new ChatComponentText("No player with the name " + args.get(0) + " has been recorded."));
            sendSenderUsage(sender, HelpOption.claimPlayerInvite);
            return;
        }
        try
        {
            if(EnkiProtection.getInstance().getPendingInvitations().addInvitation(invitedPlayerId, claim.getId()))
                sender.addChatMessage(new ChatComponentText("Player invited!"));
            else
                sender.addChatMessage(new ChatComponentText("Player invite renewed!"));
        }
        catch(AcceptedRequestException ex)
        {
            sender.addChatMessage(new ChatComponentText("Player has joined!"));
        }
    }
    
    protected void handleClaimPlayerCancelinvitation(ICommandSender sender, List<String> args)
    {}
    
    protected void handleClaimPlayerAlly(ICommandSender sender, List<String> args)
    {}
    
    protected void handleClaimPlayerBan(ICommandSender sender, List<String> args)
    {}
    
    protected void handleClaimPlayerMakeowner(ICommandSender sender, List<String> args)
    {}
    
    protected void handleClaimChunk(ICommandSender sender, List<String> args)
    {}
    
    protected void handleClaimChunkAdd(ICommandSender sender, List<String> args)
    {}
    
    protected void handleClaimChunkRemove(ICommandSender sender, List<String> args)
    {
        if(!(sender instanceof EntityPlayer))
        {
            sender.addChatMessage(new ChatComponentText("This command can only be performed by players."));
            return;
        }
        
        if(!(args.size() >= 0 && args.size() <= 1))
        {
            sendSenderUsage(sender, HelpOption.claimChunkRemove);
            return;
        }
        
        EntityPlayer player = (EntityPlayer)sender;
        Claim claim;
        
        if(args.isEmpty())
            claim = EnkiProtection.getInstance().getClaims().getClaimAtBlock(player.serverPosX, player.serverPosZ, player.dimension);
        else
            claim = EnkiProtection.getInstance().getClaims().getClaim(args.get(0));
        
        ChunkCoordinates playerLocation = player.getPlayerCoordinates();
        ChunkCoOrdinate playerChunk = new ChunkCoOrdinate(playerLocation.posX / 16, playerLocation.posZ / 16, player.dimension);
        
        if(claim == null)
        {
            if(args.isEmpty())
                sender.addChatMessage(new ChatComponentText("No claim with the name " + args.get(0) + " exists."));
            else
                sender.addChatMessage(new ChatComponentText("Current chunk is not in a claim."));
                
            sendSenderUsage(sender, HelpOption.claimChunkRemove);
            return;
        }
        
        try
        {
            if(!claim.canRemoveChunk(player, playerChunk))
            {
                sender.addChatMessage(new ChatComponentText("You don't have permission to do that."));
                return;
            }
        }
        catch(ChunkNotPresentException ex)
        { sender.addChatMessage(new ChatComponentText("That chunk is not currently in the claim.")); }
        
        claim.unclaimChunk(playerChunk);
        sender.addChatMessage(new ChatComponentText("Chunk un-claimed!"));
    }
    
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