package com.enkigaming.minecraft.forge.enkiprotection;

public class Settings
{
    public Settings()
    {
        numberOfHoursUntilPowerRevocationExpires = 72;
        numberOfMinutesUntilClaimInvitationExpires = 30;
    }
    
    protected int numberOfHoursUntilPowerRevocationExpires;
    protected int numberOfMinutesUntilClaimInvitationExpires;
    
    public int getNumberOfHoursUntilPowerRecovationExpires()
    { return numberOfHoursUntilPowerRevocationExpires; }
    
    public int getNumberOfMinutesUntilClaimInvitationExpires()
    { return numberOfMinutesUntilClaimInvitationExpires; }
}