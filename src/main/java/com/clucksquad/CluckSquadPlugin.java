package com.clucksquad;

import com.google.inject.Provides;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.inject.Inject;
import javax.swing.SwingUtilities;

import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.events.GameStateChanged;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.ClientToolbar;
import net.runelite.client.ui.NavigationButton;
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
    @Inject private ClientToolbar clientToolbar;

    private boolean loginMessageShown;
    private CluckSquadPanel panel;
    private NavigationButton navigationButton;

    @Override
    protected void startUp()
    {
        loginMessageShown = false;

        if (config.showOverlay())
        {
            overlayManager.add(overlay);
        }

        panel = new CluckSquadPanel(config);

        BufferedImage icon = createIcon();

        navigationButton = NavigationButton.builder()
            .tooltip("Cluck Squad Events")
            .icon(icon)
            .priority(5)
            .panel(panel)
            .build();

        clientToolbar.addNavigation(navigationButton);
    }

    @Override
    protected void shutDown()
    {
        overlayManager.remove(overlay);

        if (navigationButton != null)
        {
            clientToolbar.removeNavigation(navigationButton);
        }

        panel = null;
        navigationButton = null;
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
                    "Cluck Squad: " + eventName + " — " + config.eventTime() + attendance,
                    null
                );
            }
        }
    }

    @Subscribe
    public void onConfigChanged(net.runelite.client.events.ConfigChanged event)
    {
        if ("clucksquad".equals(event.getGroup()) && panel != null)
        {
            SwingUtilities.invokeLater(panel::rebuild);
        }
    }

    private BufferedImage createIcon()
    {
        BufferedImage image = new BufferedImage(32, 32, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = image.createGraphics();

        graphics.setColor(new Color(255, 204, 0));
        graphics.fillOval(5, 7, 22, 20);

        graphics.setColor(Color.WHITE);
        graphics.fillOval(10, 10, 6, 6);

        graphics.setColor(Color.BLACK);
        graphics.fillOval(12, 12, 2, 2);

        graphics.setColor(new Color(220, 60, 40));
        graphics.fillOval(22, 14, 7, 5);
        graphics.fillOval(14, 23, 4, 5);

        graphics.dispose();
        return image;
    }

    @Provides
    CluckSquadConfig provideConfig(ConfigManager configManager)
    {
        return configManager.getConfig(CluckSquadConfig.class);
    }
}
