package com.enkigaming.minecraft.forge.enkiprotection.registry;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerAutoclaimRegistry
{
    public static enum AutoclaimState
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
            if(state == null)
                return claimStates.remove(playerId);
            
            return claimStates.put(playerId, state);
        }
    }
    
    public AutoclaimState setClaiming(UUID playerId)
    { return setState(playerId, AutoclaimState.claiming); }
    
    public AutoclaimState setUnclaiming(UUID playerId)
    { return setState(playerId, AutoclaimState.unclaiming); }
    
    public AutoclaimState setDoingNothing(UUID playerId)
    { return setState(playerId, null); }
}