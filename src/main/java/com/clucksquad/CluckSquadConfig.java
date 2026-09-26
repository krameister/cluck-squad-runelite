package com.clucksquad;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("clucksquad")
public interface CluckSquadConfig extends Config
{
    @ConfigItem(
        keyName = "showOverlay",
        name = "Show event overlay",
        description = "Show the current Cluck Squad event on screen"
    )
    default boolean showOverlay()
    {
        return true;
    }

    @ConfigItem(
        keyName = "nextEvent",
        name = "Next event",
        description = "Name of the next Cluck Squad event"
    )
    default String nextEvent()
    {
        return "No event set";
    }

    @ConfigItem(
        keyName = "eventTime",
        name = "Event time",
        description = "Time of the next event, for example 9:00 PM BST"
    )
    default String eventTime()
    {
        return "Not set";
    }

    @ConfigItem(
        keyName = "eventDetails",
        name = "Event details",
        description = "Short description of the next event"
    )
    default String eventDetails()
    {
        return "Check Discord for details";
    }

    @ConfigItem(
        keyName = "attendance",
        name = "My events attended",
        description = "Number of Cluck Squad events you have attended"
    )
    default int attendance()
    {
        return 0;
    }

    @ConfigItem(
        keyName = "points",
        name = "My event points",
        description = "Your current Cluck Squad event points"
    )
    default int points()
    {
        return 0;
    }
}
