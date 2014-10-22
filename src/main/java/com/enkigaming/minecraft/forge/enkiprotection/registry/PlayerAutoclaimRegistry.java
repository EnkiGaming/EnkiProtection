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
    {  }
    
    public AutoclaimState setState(UUID playerId, UUID claimId, AutoclaimSetting setting)
    {  }
    
    public AutoclaimState setClaiming(UUID playerId, UUID claimId)
    {  }
    
    public AutoclaimState setUnclaiming(UUID playerId, UUID claimId)
    {  }
    
    public AutoclaimState setDoingNothing(UUID playerId)
    {  }
}