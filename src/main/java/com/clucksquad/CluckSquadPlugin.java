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
import net.runelite.client.ui.overlay.OverlayManager;

@PluginDescriptor(
    name = "Cluck Squad Events",
    description = "Tracks Cluck Squad clan events, attendance and points",
    tags = {"clan", "events", "cluck squad", "attendance", "bossing"}
)
public class CluckSquadPlugin extends Plugin
{
    @Inject private Client client;
    @Inject private CluckSquadConfig config;
    @Inject private CluckSquadOverlay overlay;
    @Inject private OverlayManager overlayManager;

    private boolean loginMessageShown;

    @Override
    protected void startUp()
    {
        loginMessageShown = false;

        if (config.showOverlay())
        {
            overlayManager.add(overlay);
        }
    }

    @Override
    protected void shutDown()
    {
        overlayManager.remove(overlay);
    }

    @Subscribe
    public void onGameStateChanged(GameStateChanged event)
    {
        if (event.getGameState() != GameState.LOGGED_IN)
        {
            loginMessageShown = false;
            return;
        }

        if (!loginMessageShown && config.showLoginMessage())
        {
            loginMessageShown = true;
            String eventName = config.nextEvent();

            if (eventName != null && !eventName.trim().isEmpty() && !eventName.equalsIgnoreCase("No event set"))
            {
                String attendance = config.attending() ? " — You're marked as attending" : "";
                client.addChatMessage(
                    ChatMessageType.GAMEMESSAGE,
                    "",
                    "🐔 Cluck Squad: " + eventName + " — " + config.eventTime() + attendance,
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
