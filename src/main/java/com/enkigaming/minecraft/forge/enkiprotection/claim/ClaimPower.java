package com.enkigaming.minecraft.forge.enkiprotection.claim;

import com.enkigaming.minecraft.forge.enkiprotection.EnkiProtection;
import com.enkigaming.minecraft.forge.enkiprotection.registry.exceptions.RevokingMorePowerThanAvailableException;
import com.enkigaming.minecraft.forge.enkiprotection.registry.exceptions.RevokingMorePowerThanGrantedException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.minecraft.entity.player.EntityPlayer;
import org.apache.commons.lang3.NotImplementedException;

// Need to check over power revocation queueing.

public abstract class ClaimPower
{
    protected static class QueuedRevocation
    {
        public QueuedRevocation(UUID revokingPlayerId, Date forceRevokeOn, int amount)
        {
            started = new Date();
            this.forceRevokeOn = new Date(forceRevokeOn.getTime());
            this.amount = amount;
            this.playerId = revokingPlayerId;
        }
        
        protected final Date started;
        protected Date forceRevokeOn;
        protected int amount;
        protected final UUID playerId;
        
        protected final Object revokeOnLock = new Object();
        protected final Object amountLock = new Object();
        
        public Date getWhenStarted()
        { return new Date(started.getTime()); }
        
        public Date getWhenToForceRevoke()
        {
            synchronized(revokeOnLock)
            { return new Date(forceRevokeOn.getTime()); }
        }
        
        public int getAmount()
        {
            synchronized(amountLock)
            { return amount; }
        }
        
        public UUID getPlayerId()
        { return playerId; }
        
        public Date setWhenToForceRevoke(Date newDate)
        {
            synchronized(revokeOnLock)
            {
                Date old = forceRevokeOn;
                forceRevokeOn = new Date(newDate.getTime());
                return old;
            }
        }
        
        public int setAmount(int newAmount)
        {
            synchronized(amountLock)
            {
                int old = amount;
                amount = newAmount;
                return old;
            }
        }
        
        public int removeFromAmount(int amountToRemove)
        {
            synchronized(amountLock)
            {
                int old = amount;
                amount -= amountToRemove;
                return old;
            }
        }
        
        public int addToAmount(int amountToAdd)
        {
            synchronized(amountLock)
            {
                int old = amount;
                amount += amountToAdd;
                return old;
            }
        }
        
        public boolean isDue()
        {
            synchronized(revokeOnLock)
            { return new Date().after(forceRevokeOn); }
        }
    }
    
    protected final Map<UUID, Integer> powerGrants = new HashMap<UUID, Integer>();
    protected final Map<UUID, QueuedRevocation> revokeQueue = new HashMap<UUID, QueuedRevocation>(); // synchronized with powerGrants
    
    public void markPowerAsGranted(UUID playerId, int amount)
    {
        synchronized(powerGrants)
        {
            int alreadyGranted = getPowerGrantedBy(playerId);
            
            powerGrants.put(playerId, alreadyGranted + amount);
        }
    }
    
    public void markPowerAsGranted(EntityPlayer player, int amount)
    { markPowerAsGranted(player.getGameProfile().getId(), amount); }
    
    public int getPowerGrantedBy(UUID playerId)
    {
        synchronized(powerGrants)
        {
            Integer powerGranted = powerGrants.get(playerId);
            
            if(powerGranted == null)
                powerGranted = 0;
            
            return powerGranted;
        }
    }
    
    public int getPowerGrantedBy(EntityPlayer player)
    { return getPowerGrantedBy(player.getGameProfile().getId()); }
    
    public int unmarkPowerAsGranted(UUID playerId, int amount) throws RevokingMorePowerThanAvailableException,
                                                                      RevokingMorePowerThanGrantedException
    {
        synchronized(powerGrants)
        {
            int powerGranted = getPowerGrantedBy(playerId);
            
            if(amount > powerGranted)
                throw new RevokingMorePowerThanGrantedException(playerId, amount, powerGranted);
            
            int availablePower = getAvailablePower();
            
            if(amount > availablePower)
                throw new RevokingMorePowerThanAvailableException(availablePower, amount);
            
            if(powerGranted == amount)
                powerGrants.remove(playerId);
            else
                powerGrants.put(playerId, powerGranted - amount);
            
            return powerGranted;
        }
    }
    
    public int unmarkPowerAsGranted(EntityPlayer player, int amount) throws RevokingMorePowerThanAvailableException,
                                                                            RevokingMorePowerThanGrantedException
    { return unmarkPowerAsGranted(player.getGameProfile().getId(), amount); }
    
    public int forceUnmarkPowerAsGranted(UUID playerId, int amount) throws RevokingMorePowerThanGrantedException
    {
        int powerAvailable;
        int powerGranted;
        
        synchronized(powerGrants)
        {
            powerAvailable = getAvailablePower();
            powerGranted = getPowerGrantedBy(playerId);
            
            if(powerGranted > amount)
                throw new RevokingMorePowerThanGrantedException(playerId, powerAvailable, amount);
            
            if(powerGranted == amount)
                powerGrants.remove(playerId);
            else
                powerGrants.put(playerId, powerGranted - amount);
        }
        
        if(amount > powerAvailable)
            onForceRevoke(playerId, amount, amount - powerAvailable);
        
        return powerGranted;
    }
    
    public int forceUnmarkPowerAsGranted(EntityPlayer player, int amount) throws RevokingMorePowerThanGrantedException
    { return forceUnmarkPowerAsGranted(player.getGameProfile().getId(), amount); }
    
    public int unmarkAllAvailablePowerAsGranted(UUID playerId)
    {
        synchronized(powerGrants)
        {
            int availablePower = getAvailablePower();
            
            if(availablePower <= 0)
                return 0;
            
            int powerGranted = powerGrants.get(playerId);
            
            int toUnmark = powerGranted;
            
            if(availablePower < powerGranted)
                toUnmark = availablePower;
            
            if(powerGranted == toUnmark)
                powerGrants.remove(playerId);
            else
                powerGrants.put(playerId, powerGranted - toUnmark);
            
            return toUnmark;
        }
    }
    
    public void queueRevocation(UUID playerId, int amount)
    {
        // 3600000 ms per hour.
        Date whenToHappen = new Date(new Date().getTime() + 
                                     EnkiProtection.getInstance().getSettings().getNumberOfHoursUntilPowerRecovationExpires() * 3600000);
        
        synchronized(powerGrants)
        {
            QueuedRevocation queuedRevocation = revokeQueue.get(playerId);
            
            if(queuedRevocation == null)
            {
                queuedRevocation = new QueuedRevocation(playerId, whenToHappen, amount);
                revokeQueue.put(playerId, queuedRevocation);
            }
            else
            {
                queuedRevocation.setWhenToForceRevoke(whenToHappen);
                queuedRevocation.addToAmount(amount);
            }
        }
    }
    
    public void queueRevocation(EntityPlayer player, int amount)
    { queueRevocation(player.getGameProfile().getId(), amount); }
    
    public void queueRevocation(UUID playerId)
    {
        throw new NotImplementedException("To be implemented");
    }
    
    public void queueRevocation(EntityPlayer player)
    {
        throw new NotImplementedException("To be implemented");
    }
    
    public void changeRevokeAmount(UUID playerId, int newAmount)
    {
        // 3600000 ms per hour.
        Date whenToHappen = new Date(new Date().getTime() + 
                                     EnkiProtection.getInstance().getSettings().getNumberOfHoursUntilPowerRecovationExpires() * 3600000);
        
        synchronized(powerGrants)
        {
            QueuedRevocation queuedRevocation = revokeQueue.get(playerId);
            
            if(queuedRevocation == null)
            {
                queuedRevocation = new QueuedRevocation(playerId, whenToHappen, newAmount);
                revokeQueue.put(playerId, queuedRevocation);
                return;
            }
            
            if(newAmount > queuedRevocation.getAmount())
                queuedRevocation.setWhenToForceRevoke(whenToHappen);
            
            queuedRevocation.setAmount(newAmount);
        }
    }
    
    public void changeRevokeAmount(EntityPlayer player, int newAmount)
    { changeRevokeAmount(player.getGameProfile().getId(), newAmount); }
    
    public int getTotalPower()
    {
        int power = 0;
        
        synchronized(powerGrants)
        {
            for(Integer i : powerGrants.values())
                power += i;
        }
        
        return power;
    }
    
    public int getAvailablePower()
    {
        int power;
        
        synchronized(powerGrants)
        {
            power = getTotalPower() - getPowerUsed();
            
            for(QueuedRevocation i : revokeQueue.values())
                power -= i.getAmount();
            
            if(power < 0)
                power = 0;
        }
        
        return power;
    }
    
    public int getAvailablePowerIgnoringQueuedRevocations()
    {
        synchronized(powerGrants)
        { return getTotalPower() - getPowerUsed(); }
    }
    
    public int getPowerPlayerHasGranted(UUID playerId)
    {
        Integer powerGranted;
        
        synchronized(powerGrants)
        {
            powerGranted = powerGrants.get(playerId);
            
            if(powerGranted == null)
                powerGranted = 0;
        }
        
        return powerGranted;
    }
    
    public int getPowerPlayerHasGranted(EntityPlayer player)
    { return getPowerPlayerHasGranted(player.getGameProfile().getId()); }
    
    public void checkPowerRevocationQueue()
    {
        synchronized(powerGrants)
        {
            Collection<QueuedRevocation> toRemove = new ArrayList<QueuedRevocation>();
            
            for(QueuedRevocation queuedRevocation : revokeQueue.values())
            {
                if(queuedRevocation.isDue())
                {
                    try
                    {
                        forceUnmarkPowerAsGranted(queuedRevocation.getPlayerId(), queuedRevocation.getAmount());
                        toRemove.add(queuedRevocation);
                    }
                    catch(RevokingMorePowerThanGrantedException ex)
                    { Logger.getLogger(ClaimPower.class.getName()).log(Level.SEVERE, null, ex); } // This should never happen ... famous last words.
                }
            }
            
            for(QueuedRevocation queuedRevocation : toRemove)
                revokeQueue.remove(queuedRevocation.getPlayerId());
        }
    }
    
    public abstract void onForceRevoke(UUID playerRevoking, int amountBeingRevoked, int amountMoreThanAvailablePowerBeingRevoked);
    
    public abstract int getPowerUsed();
    
    public abstract void notifyPlayerPowerOfRevocation(UUID playerId, int amount);
}