package com.enkigaming.minecraft.forge.enkiprotection.utils;

public class Utils
{
    public boolean stringParsesToTrue(String string)
    {
        string = string.trim();
        
        return string.equalsIgnoreCase("true")
            || string.equalsIgnoreCase("yes")
            || string.equalsIgnoreCase("positive")
            || string.equalsIgnoreCase("pos")
            || string.equalsIgnoreCase("t")
            || string.equalsIgnoreCase("y")
            || string.equalsIgnoreCase("p");
    }
    
    public boolean stringParsesToFalse(String string)
    {
        string = string.trim();
        
        return string.equalsIgnoreCase("false")
            || string.equalsIgnoreCase("no")
            || string.equalsIgnoreCase("negative")
            || string.equalsIgnoreCase("neg")
            || string.equalsIgnoreCase("f")
            || string.equalsIgnoreCase("n");
    }
}