package com.clucksquad;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import javax.inject.Inject;

import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayPosition;

public class CluckSquadOverlay extends Overlay
{
    private final CluckSquadConfig config;
    private boolean enabled;

    @Inject
    public CluckSquadOverlay(CluckSquadConfig config)
    {
        this.config = config;
        setPosition(OverlayPosition.TOP_LEFT);
    }

    public void setEnabled(boolean enabled)
    {
        this.enabled = enabled;
    }

    @Override
    public Dimension render(Graphics2D graphics)
    {
        if (!enabled || !config.showOverlay())
        {
            return null;
        }

        int x = 10;
        int y = 20;
        int lineHeight = 18;

        graphics.setColor(Color.WHITE);
        graphics.drawString("CLUCK SQUAD", x, y);
        graphics.drawString("Event: " + config.nextEvent(), x, y += lineHeight);
        graphics.drawString("Type: " + config.eventType(), x, y += lineHeight);
        graphics.drawString("Boss: " + config.eventBoss(), x, y += lineHeight);
        graphics.drawString("Time: " + config.eventTime(), x, y += lineHeight);
        graphics.drawString("Attending: " + (config.attending() ? "YES" : "NO"), x, y += lineHeight);
        graphics.drawString("My attendance: " + config.attendance(), x, y += lineHeight);
        graphics.drawString("My points: " + config.points(), x, y += lineHeight);

        return new Dimension(250, y + 10);
    }
}
