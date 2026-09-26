package com.clucksquad;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class CluckSquadPluginTest
{
    public static void main(String[] args) throws Exception
    {
        ExternalPluginManager.loadBuiltin(CluckSquadPlugin.class);
        RuneLite.main(args);
    }
}
