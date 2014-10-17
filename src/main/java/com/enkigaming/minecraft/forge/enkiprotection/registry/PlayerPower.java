package com.enkigaming.minecraft.forge.enkiprotection.registry;

import com.enkigaming.minecraft.forge.enkiprotection.claim.Claim;
import com.enkigaming.minecraft.forge.enkiprotection.registry.exceptions.GrantingMoreClaimPowerThanHaveException;
import com.enkigaming.minecraft.forge.enkiprotection.registry.exceptions.NotEnoughClaimPowerToRemoveException;
import com.enkigaming.minecraft.forge.enkiprotection.registry.exceptions.RevokingMorePowerThanAvailableException;
import com.enkigaming.minecraft.forge.enkiprotection.registry.exceptions.RevokingMorePowerThanGrantedException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerPower
{
    public PlayerPower(UUID playerId)
    {
        this.playerId = playerId;
        totalPower = 0;
    }
    
    protected final UUID playerId;
    protected int totalPower;
    
    protected Map<Claim, Integer> powerGrants = new HashMap<Claim, Integer>();
    
    protected final Object powerLock = new Object();
    
    public int getTotalPower()
    {
        synchronized(powerLock)
        { return totalPower; }
    }
    
    public int getAvailablePower()
    {
        synchronized(powerLock)
        { return totalPower - getPowerGranted(); }
    }
    
    public int getPowerGranted()
    {
        synchronized(powerLock)
        {
            int powerGranted = 0;
            
            for(Integer i : powerGrants.values())
                powerGranted += i;
            
            return powerGranted;
        }
    }
    
    public int setPower(int newPower) throws NotEnoughClaimPowerToRemoveException
    {
        synchronized(powerLock)
        {
            int powerGranted = getPowerGranted();
            
            if(newPower < powerGranted || newPower < 0)
                throw new NotEnoughClaimPowerToRemoveException(playerId, totalPower - newPower, totalPower - powerGranted, totalPower);
            
            int old = totalPower;
            totalPower = newPower;
            return old;
        }
    }
    
    public int forceSetPower(int newPower)
    {
        synchronized(powerLock)
        {
            int old = totalPower;
            totalPower = newPower;
            return old;
        }
    }
    
    public int givePower(int power)
    {
        if(power < 0)
            throw new IllegalArgumentException("Power may not be less than 0.");
        
        synchronized(powerLock)
        {
            int old = totalPower;
            totalPower += power;
            return old;
        }
    }
    
    public int takePower(int power) throws NotEnoughClaimPowerToRemoveException
    {
        if(power < 0)
            throw new IllegalArgumentException("Power may not be less than 0.");
        
        synchronized(powerLock)
        {
            int powerAvailable = getAvailablePower();
            
            if(totalPower - power < powerAvailable)
                throw new NotEnoughClaimPowerToRemoveException(playerId, power, powerAvailable, totalPower);
            
            int old = totalPower;
            totalPower -= power;
            return old;
        }
    }
    
    public int forceTakePower(int power)
    {
        synchronized(powerLock)
        {
            int old = totalPower;
            totalPower -= power;
            return old;
        }
    }
    
    public void grantPowerTo(Claim claim, int amount) throws GrantingMoreClaimPowerThanHaveException
    {
        synchronized(powerLock)
        {
            int availablePower = getAvailablePower();
            
            if(amount > availablePower)
                throw new GrantingMoreClaimPowerThanHaveException(playerId, availablePower, amount, totalPower);
            
            claim.getPowerManager().markPowerAsGranted(playerId, amount);
            
            Integer alreadyGranted = powerGrants.get(claim);
            
            if(alreadyGranted == null)
                alreadyGranted = 0;
            
            powerGrants.put(claim, alreadyGranted + amount);
        }
    }
    
    public void forceGrantPowerTo(Claim claim, int amount)
    {
        synchronized(powerLock)
        {
            claim.getPowerManager().markPowerAsGranted(playerId, amount);
            
            Integer alreadyGranted = powerGrants.get(claim);
            
            if(alreadyGranted == null)
                alreadyGranted = 0;
            
            powerGrants.put(claim, alreadyGranted + amount);
        }
    }
    
    public int grantAllPowerTo(Claim claim)
    {
        synchronized(powerLock)
        {
            int power = getAvailablePower();
            
            forceGrantPowerTo(claim, power);
            return power;
        }
    }
    
    public void revokePowerFrom(Claim claim, int amount) throws RevokingMorePowerThanAvailableException,
                                                                RevokingMorePowerThanGrantedException
    {
        synchronized(powerLock)
        { claim.getPowerManager().unmarkPowerAsGranted(playerId, amount); }
    }
    
    public int revokeAllAvailablePowerFrom(Claim claim)
    {
        synchronized(powerLock)
        {
            int revoked = claim.getPowerManager().unmarkAllAvailablePowerAsGranted(playerId);
            int powerGranted = powerGrants.get(claim);
            
            if(revoked >= powerGranted)
                powerGrants.remove(claim);
            else
                powerGrants.put(claim, powerGranted - revoked);
            
            return revoked;
        }
    }
    
    public void queuePowerRevocation(Claim claim, int amount)
    {
        synchronized(powerLock)
        { claim.getPowerManager().queueRevocation(playerId, amount); }
    }
    
    public void queuePowerRevocation(Claim claim)
    {
        synchronized(powerLock)
        { claim.getPowerManager().queueRevocation(playerId); }
    }
    
    public void transferPowerTo(PlayerPower player, int amount) throws NotEnoughClaimPowerToRemoveException
    {
        synchronized(powerLock)
        {
            
        }
    }
    
    public void notifyOfPowerGrantReturn(Claim claim, int amount)
    {  }
}