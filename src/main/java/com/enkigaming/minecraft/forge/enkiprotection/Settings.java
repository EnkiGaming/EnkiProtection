package com.enkigaming.minecraft.forge.enkiprotection;

public class Settings
{
    public Settings()
    {
        numberOfHoursUntilPowerRevocationExpires = 72;
    }
    
    protected int numberOfHoursUntilPowerRevocationExpires;
    
    public int getNumberOfHoursUntilPowerRecovationExpires()
    { return numberOfHoursUntilPowerRevocationExpires; }
}