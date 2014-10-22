package com.enkigaming.minecraft.forge.enkiprotection.registry;

import com.enkigaming.mcforge.enkilib.EnkiLib;
import com.enkigaming.mcforge.enkilib.filehandling.CSVFileHandler;
import com.enkigaming.mcforge.enkilib.filehandling.CSVFileHandler.CSVRowMember;
import com.enkigaming.mcforge.enkilib.filehandling.FileHandler;
import com.enkigaming.minecraft.forge.enkiprotection.EnkiProtection;
import com.enkigaming.minecraft.forge.enkiprotection.claim.Claim;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class PendingInvitationRegistry
{
    protected static class PendingInvitation
    {
        public PendingInvitation(UUID playerId, UUID claimId)
        {
            this.playerId = playerId;
            this.claimId = claimId;
            this.expires = new Date(new Date().getTime() + (60000 * EnkiProtection.getInstance().getSettings().getNumberOfMinutesUntilClaimInvitationExpires()));
        }
        
        public PendingInvitation(UUID playerId, UUID claimId, Date expiryDate)
        {
            this.playerId = playerId;
            this.claimId = claimId;
            this.expires = new Date(expiryDate.getTime());
        }
        
        final UUID playerId;
        final UUID claimId;
        Date expires;
        
        final Object expiresLock = new Object();
        
        public UUID getPlayerId()
        { return playerId; }
        
        public UUID getClaimId()
        { return claimId; }
        
        public Date getExpiryDate()
        {
            synchronized(expiresLock)
            { return new Date(expires.getTime()); }
        }
        
        public boolean hasExpired()
        {
            synchronized(expiresLock)
            { return new Date().after(expires); }
        }
        
        public void renew()
        {
            synchronized(expires)
            { expires = new Date(new Date().getTime() + (60000 * EnkiProtection.getInstance().getSettings().getNumberOfMinutesUntilClaimInvitationExpires())); }
        }
    }
    
    // Replace at some point with proper event.
    public static class InvitationEvent
    {
        public InvitationEvent(UUID playerId, UUID claimId, boolean accepted)
        {
            this.player = playerId;
            this.claim = claimId;
            this.accepted = accepted;
        }
        
        final protected UUID player;
        final protected UUID claim;
        final protected boolean accepted;
        
        public UUID getPlayerId()
        { return player; }
        
        public UUID getClaimId()
        { return claim; }
        
        public boolean wasAccepted()
        { return accepted; }
    }
    
    public static abstract class InvitationEventListener
    { public abstract void onEvent(InvitationEvent event); }
    
    public PendingInvitationRegistry(File saveFolder)
    { fileHandler = makeFileHandler(saveFolder); }
    
    protected Multimap<UUID, PendingInvitation> pendingInvitations = HashMultimap.<UUID, PendingInvitation>create();
    
    protected Collection<InvitationEventListener> listeners = new ArrayList<InvitationEventListener>();
    
    protected Lock invitationsLock = new ReentrantLock();
    
    protected FileHandler fileHandler;
    
    protected FileHandler makeFileHandler(File saveFolder)
    {
        return new CSVFileHandler("PendingInvitationRegistry", new File(saveFolder, "PendingInvitations.csv"), "Not all pending invitations could be loaded, those that could be were.")
        {
            List<PendingInvitation> invitationList;
            Date now;
            
            @Override
            protected void onNoFileToInterpret()
            {}

            @Override
            protected List<String> getColumnNames()
            { return Arrays.asList("Claim ID", "Claim name", "Player ID", "Last recorded player name", "Expires (ms since epoch)"); }

            @Override
            protected void preInterpretation()
            {
                invitationsLock.lock();
                pendingInvitations.clear();
                now = new Date();
            }

            @Override
            protected boolean interpretRow(List<String> list)
            {
                if(list.size() != 5)
                    return false;
                
                UUID claimId;
                UUID playerId;
                long expiryDateAsLong;
                Date expiryDate;
                
                try
                { expiryDateAsLong = Long.parseLong(list.get(4)); }
                catch(NumberFormatException exception)
                { return false; }
                
                expiryDate = new Date(expiryDateAsLong);
                
                if(now.after(expiryDate))
                    return true;
                
                try
                { claimId = UUID.fromString(list.get(0)); }
                catch(IllegalArgumentException exception)
                { return false; }
                
                try
                { playerId = UUID.fromString(list.get(2)); }
                catch(IllegalArgumentException exception)
                { return false; }
                
                pendingInvitations.put(playerId, new PendingInvitation(playerId, claimId, expiryDate));
                return true;
            }

            @Override
            protected void postInterpretation()
            {
                now = null;
                invitationsLock.unlock();
            }

            @Override
            protected void preSave()
            {
                invitationsLock.lock();
                invitationList = new ArrayList<PendingInvitation>(pendingInvitations.values());
            }

            @Override
            protected List<CSVRowMember> getRow(int i)
            {
                String playerName = EnkiLib.getLastRecordedNameOf(invitationList.get(i).getPlayerId());
                Claim claim = EnkiProtection.getInstance().getClaims().getClaim(invitationList.get(i).getClaimId());
                
                CSVRowMember playerNameField;
                
                if(playerName == null)
                    playerNameField = new CSVRowMember("", false);
                else
                    playerNameField = new CSVRowMember(playerName, true);
                
                CSVRowMember claimNameField;
                
                if(claim == null)
                    claimNameField = new CSVRowMember("", false);
                else
                    claimNameField = new CSVRowMember(claim.getName(), true);
                
                return Arrays.asList(new CSVRowMember(invitationList.get(i).getClaimId().toString(), false),
                                     claimNameField,
                                     new CSVRowMember(invitationList.get(i).getPlayerId().toString(), false),
                                     playerNameField,
                                     new CSVRowMember(Long.toString(invitationList.get(i).getExpiryDate().getTime()), false));
            }

            @Override
            protected void postSave()
            {
                invitationList = null;
                invitationsLock.unlock();
            }
        };
    }
    
    public FileHandler getFileHandler()
    { return fileHandler; }
    
    /**
     * Adds an invitation for the player to join the passed claim.
     * @param playerId The ID of the player to join the claim.
     * @param claimId The ID of the claim to join.
     * @return True if successful, false if there was already a matching invitation, in which case it gets renewed.
     */
    public boolean addInvitation(UUID playerId, UUID claimId)
    {
        invitationsLock.lock();
        
        try
        {
            for(PendingInvitation invitation : pendingInvitations.get(playerId))
                if(invitation.getClaimId().equals(claimId))
                {
                    invitation.renew();
                    return false;
                }
            
            pendingInvitations.put(playerId, new PendingInvitation(playerId, claimId));
            return true;
        }
        finally
        { invitationsLock.unlock(); }
    }
    
    public void clearInvitations(UUID playerId, UUID claimId)
    {
        invitationsLock.lock();
        
        try
        { pendingInvitations.clear(); }
        finally
        { invitationsLock.unlock(); }
    }
    
    public Collection<UUID> getClaimsInvitedTo(UUID playerId)
    {
        invitationsLock.lock();
        
        try
        {
            Collection<UUID> claimIds = new ArrayList<UUID>();
            
            for(PendingInvitation invitation : pendingInvitations.get(playerId))
                claimIds.add(invitation.getClaimId());
            
            return claimIds;
        }
        finally
        { invitationsLock.unlock(); }
    }
    
    public Collection<UUID> getPlayersInvitedToClaim(UUID claimId)
    {
        invitationsLock.lock();
        
        try
        {
            Collection<UUID> playerIds = new ArrayList<UUID>();
            
            for(PendingInvitation invitation : pendingInvitations.values())
                if(invitation.getClaimId().equals(claimId))
                    playerIds.add(invitation.getPlayerId());
            
            return playerIds;
        }
        finally
        { invitationsLock.unlock(); }
    }
    
    // True if successful, false if no invitation to remove.
    public boolean markInvitationAsAccepted(UUID playerId, UUID claimId)
    {
        invitationsLock.lock();
        
        try
        {
            PendingInvitation invitation = null;
            
            for(PendingInvitation currentInvitation : pendingInvitations.get(playerId))
                if(currentInvitation.getClaimId().equals(claimId))
                {
                    invitation = currentInvitation;
                    break;
                }
            
            if(invitation == null)
                return false;
            
            pendingInvitations.remove(playerId, invitation);
        }
        finally
        { invitationsLock.unlock(); }
        
        synchronized(listeners)
        {
            InvitationEvent args = new InvitationEvent(playerId, claimId, true);
            
            for(InvitationEventListener listener : listeners)
                listener.onEvent(args);
        }
        
        return true;
    }
    
    // True if successful, false if no invitation to remove.
    public boolean markInvitationAsRejected(UUID playerId, UUID claimId)
    {
        invitationsLock.lock();
        
        try
        {
            PendingInvitation invitation = null;
            
            for(PendingInvitation currentInvitation : pendingInvitations.get(playerId))
                if(currentInvitation.getClaimId().equals(claimId))
                {
                    invitation = currentInvitation;
                    break;
                }
            
            if(invitation == null)
                return false;
            
            pendingInvitations.remove(playerId, invitation);
        }
        finally
        { invitationsLock.unlock(); }
        
        synchronized(listeners)
        {
            InvitationEvent args = new InvitationEvent(playerId, claimId, false);
            
            for(InvitationEventListener listener : listeners)
                listener.onEvent(args);
        }
        
        return true;
    }
    
    public void registerInvitationEndListener(InvitationEventListener listener)
    {
        synchronized(listeners)
        { listeners.add(listener); }
    }
    
    public void clearOutExpiredInvitations()
    {
        invitationsLock.lock();
        
        try
        {
            Collection<PendingInvitation> invitations = new ArrayList<PendingInvitation>(pendingInvitations.values());
            
            for(PendingInvitation invitation : invitations)
                if(invitation.hasExpired())
                    pendingInvitations.remove(invitation.getPlayerId(), invitation.getClaimId());
        }
        finally
        { invitationsLock.unlock(); }
    }
}