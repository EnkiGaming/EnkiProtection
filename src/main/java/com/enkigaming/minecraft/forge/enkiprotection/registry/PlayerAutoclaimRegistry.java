package com.enkigaming.minecraft.forge.enkiprotection.registry;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerAutoclaimRegistry
{
    public static class AutoclaimState
    {
        public AutoclaimState(UUID claimId, AutoclaimSetting setting)
        {
            this.claimId = claimId;
            this.setting = setting;
        }
        
        final AutoclaimSetting setting;
        final UUID claimId;
        
        public AutoclaimSetting getSetting()
        { return setting; }
        
        public UUID getClaimId()
        { return claimId; }
    }
    
    public static enum AutoclaimSetting
    {
        claiming,
        unclaiming
    }
    
    final Map<UUID, AutoclaimState> claimStates = new HashMap<UUID, AutoclaimState>();
    
    public AutoclaimState getState(UUID playerId)
    {
        synchronized(claimStates)
        { return claimStates.get(playerId); }
    }
    
    public AutoclaimState setState(UUID playerId, AutoclaimState state)
    {
        synchronized(claimStates)
        {
            if(state == null || state.getSetting() == null)
                return claimStates.remove(playerId);
            
            return claimStates.put(playerId, state);
        }
    }
    
    public AutoclaimState setState(UUID playerId, UUID claimId, AutoclaimSetting setting)
    { return setState(playerId, new AutoclaimState(claimId, setting)); }
    
    public AutoclaimState setClaiming(UUID playerId, UUID claimId)
    { return setState(playerId, claimId, AutoclaimSetting.claiming); }
    
    public AutoclaimState setUnclaiming(UUID playerId, UUID claimId)
    { return setState(playerId, claimId, AutoclaimSetting.unclaiming); }
    
    public AutoclaimState setDoingNothing(UUID playerId)
    {
        synchronized(claimStates)
        { return claimStates.remove(playerId); }
    }
}