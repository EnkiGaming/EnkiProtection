package com.enkigaming.minecraft.forge.enkiprotection.registry;

import com.enkigaming.mcforge.enkilib.EnkiLib;
import com.enkigaming.mcforge.enkilib.filehandling.CSVFileHandler;
import com.enkigaming.mcforge.enkilib.filehandling.CSVFileHandler.CSVRowMember;
import com.enkigaming.mcforge.enkilib.filehandling.FileHandler;
import com.enkigaming.minecraft.forge.enkiprotection.EnkiProtection;
import com.enkigaming.minecraft.forge.enkiprotection.claim.Claim;
import com.enkigaming.minecraft.forge.enkiprotection.playerpower.PlayerPower;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import net.minecraft.entity.player.EntityPlayer;

public class PlayerPowerRegistry
{
    public PlayerPowerRegistry(File saveFolder)
    { fileHandler = makeFileHandler(saveFolder); }
    
    protected final Map<UUID, PlayerPower> playerPowers = new HashMap<UUID, PlayerPower>();
    
    protected final Lock playerPowerLock = new ReentrantLock();
    
    FileHandler fileHandler;
    // Filehandler should load after the claim registry
    
    protected FileHandler makeFileHandler(File saveFolder)
    {
        FileHandler newFileHandler = new CSVFileHandler("PlayerPowerRegistry", new File(saveFolder, "PlayerPowers.csv"), "Not all player powers could be loaded. Those that could be, were.")
        {
            List<Entry<UUID, PlayerPower>> powersList;
            
            @Override
            protected void onNoFileToInterpret()
            { }

            @Override
            protected List<String> getColumnNames()
            { return Arrays.asList("Player ID", "Player Name", "Power"); }

            @Override
            protected void preInterpretation()
            {
                playerPowerLock.lock();
                playerPowers.clear();
            }

            @Override
            protected boolean interpretRow(List<String> list)
            {
                UUID playerId;
                int powerAmount;
                
                try
                { playerId = UUID.fromString(list.get(0)); }
                catch(IllegalArgumentException exception)
                {
                    System.out.println("Could not parse \"" + list.get(0) + "\" into a UUID/player ID.");
                    return false;
                }
                
                try
                { powerAmount = Integer.parseInt(list.get(2)); }
                catch(NumberFormatException exception)
                {
                    System.out.println("Could not parse \"" + list.get(2) + "\" into an integer.");
                    return false;
                }
                
                PlayerPower playerPower = new PlayerPower(playerId);
                playerPower.givePower(powerAmount);
                playerPowers.put(playerId, playerPower);
                return true;
            }

            @Override
            protected void postInterpretation()
            {
                refreshPowerGrants();
                playerPowerLock.unlock();
            }

            @Override
            protected void preSave()
            {
                playerPowerLock.lock();
                powersList = new ArrayList<Entry<UUID, PlayerPower>>(playerPowers.entrySet());
            }

            @Override
            protected List<CSVRowMember> getRow(int i)
            {
                if(i >= powersList.size())
                    return null;
                
                String lastRecordedName = EnkiLib.getLastRecordedNameOf(powersList.get(i).getKey());
                
                if(lastRecordedName == null)
                    return new ArrayList<CSVRowMember>(Arrays.asList(
                            new CSVRowMember(powersList.get(i).getKey().toString(), false),
                            new CSVRowMember("", false),
                            new CSVRowMember(Integer.toString(powersList.get(i).getValue().getTotalPower()), false)));
                
                return new ArrayList<CSVRowMember>(Arrays.asList(
                        new CSVRowMember(powersList.get(i).getKey().toString(), false),
                        new CSVRowMember(lastRecordedName, true),
                        new CSVRowMember(Integer.toString(powersList.get(i).getValue().getTotalPower()), false)));
            }

            @Override
            protected void postSave()
            {
                powersList = null;
                playerPowerLock.unlock();
            }
        };
        
        newFileHandler.mustLoadAfterHandler("ClaimRegistry");
        return newFileHandler;
    }
    
    public PlayerPower getForPlayer(UUID playerId)
    {
        playerPowerLock.lock();
        
        try
        { return playerPowers.get(playerId); }
        finally
        { playerPowerLock.unlock(); }
    }
    
    public PlayerPower getForPlayer(EntityPlayer player)
    { return getForPlayer(player.getGameProfile().getId()); }
    
    /**
     * Erases all recorded power grants and copies replacement data from the claims registry.
     */
    protected void refreshPowerGrants()
    {
        playerPowerLock.lock();
        
        try
        {
            Map<UUID, Map<Claim, Integer>> grants = new HashMap<UUID, Map<Claim, Integer>>();
            
            for(Claim claim : EnkiProtection.getInstance().getClaims().getClaims())
            {
                for(Entry<UUID, Integer> grant : claim.getPowerManager().getPowerGrantsIncludingQueuedRevocations().entrySet())
                {
                    Map<Claim, Integer> current = grants.get(grant.getKey());
                    
                    if(current == null)
                    {
                        current = new HashMap<Claim, Integer>();
                        grants.put(grant.getKey(), current);
                    }
                    
                    current.put(claim, grant.getValue());
                }
            }
            
            for(PlayerPower power : playerPowers.values())
                power.clearGrantsInThisOnly();
            
            for(Entry<UUID, Map<Claim, Integer>> grant : grants.entrySet())
            {
                PlayerPower power = playerPowers.get(grant.getKey());
                
                if(power == null)
                {
                    power = new PlayerPower(grant.getKey());
                    playerPowers.put(grant.getKey(), power);
                }
                
                power.recordGrantsInThisOnly(grant.getValue());
            }
        }
        finally
        { playerPowerLock.unlock(); }
    }
}