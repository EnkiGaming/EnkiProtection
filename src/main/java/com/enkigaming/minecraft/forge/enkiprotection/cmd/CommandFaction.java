package com.enkigaming.minecraft.forge.enkiprotection.cmd;

import com.enkigaming.minecraft.forge.enkiprotection.util.EPUtils;

import net.minecraft.command.*;

public class CommandFaction extends CommandBase
{
	public String getCommandName()
	{ return "f"; }
	
	public String getCommandUsage(ICommandSender p_71518_1_)
	{ return "/f <subcommand>"; }
	
	public void processCommand(ICommandSender ics, String[] args)
	{
		if(args == null || args.length < 1)
			EPUtils.printChat(ics, "/f help");
		else
		{
			
		}
	}
}