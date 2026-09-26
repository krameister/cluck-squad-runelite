package com.clucksquad;

import com.google.inject.Provides;
import javax.inject.Inject;

import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.events.GameStateChanged;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;

@PluginDescriptor(
    name = "Cluck Squad Events",
    description = "Tracks Cluck Squad clan events, attendance and points",
    tags = {"clan", "events", "cluck squad", "attendance", "bossing"}
)
public class CluckSquadPlugin extends Plugin
{
    @Inject
    private Client client;

    @Inject
    private CluckSquadConfig config;

    @Inject
    private CluckSquadOverlay overlay;

    @Override
    protected void startUp()
    {
        if (config.showOverlay())
        {
            overlay.setEnabled(true);
        }
    }

    @Override
    protected void shutDown()
    {
        overlay.setEnabled(false);
    }

    @Subscribe
    public void onGameStateChanged(GameStateChanged event)
    {
        if (event.getGameState() == GameState.LOGGED_IN)
        {
            String eventName = config.nextEvent();

            if (eventName != null && !eventName.trim().isEmpty() && !eventName.equalsIgnoreCase("No event set"))
            {
                client.addChatMessage(
                    ChatMessageType.GAMEMESSAGE,
                    "",
                    "🐔 Cluck Squad: Next event — " + eventName + " (" + config.eventTime() + ")",
                    null
                );
            }
        }
    }

    @Provides
    CluckSquadConfig provideConfig(ConfigManager configManager)
    {
        return configManager.getConfig(CluckSquadConfig.class);
    }
}
