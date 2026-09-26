package com.clucksquad;

import com.google.inject.Provides;
import java.awt.BasicStroke;
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

        panel = new CluckSquadPanel(config, client.getConfigManager());

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
        Graphics2D g = image.createGraphics();
        g.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);

        // White chicken body
        g.setColor(Color.WHITE);
        g.fillOval(5, 9, 21, 17);

        // Head
        g.fillOval(14, 4, 13, 13);

        // Black outline
        g.setColor(new Color(35, 35, 35));
        g.setStroke(new BasicStroke(2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g.drawOval(5, 9, 21, 17);
        g.drawOval(14, 4, 13, 13);

        // Eye
        g.fillOval(22, 8, 3, 3);

        // Beak
        g.setColor(new Color(240, 150, 30));
        int[] beakX = {26, 31, 26};
        int[] beakY = {10, 13, 16};
        g.fillPolygon(beakX, beakY, 3);

        // Comb
        g.setColor(new Color(210, 55, 45));
        g.fillOval(17, 2, 4, 5);
        g.fillOval(21, 1, 4, 6);
        g.fillOval(24, 3, 4, 5);

        // Wattle
        g.fillOval(23, 14, 5, 7);

        // Wing
        g.setColor(new Color(220, 220, 220));
        g.fillOval(8, 13, 11, 9);

        // Legs
        g.setColor(new Color(240, 150, 30));
        g.setStroke(new BasicStroke(1.5f));
        g.drawLine(12, 25, 12, 30);
        g.drawLine(19, 25, 19, 30);
        g.drawLine(12, 30, 9, 30);
        g.drawLine(12, 30, 15, 30);
        g.drawLine(19, 30, 16, 30);
        g.drawLine(19, 30, 22, 30);

        g.dispose();
        return image;
    }

    @Provides
    CluckSquadConfig provideConfig(ConfigManager configManager)
    {
        return configManager.getConfig(CluckSquadConfig.class);
    }
}
