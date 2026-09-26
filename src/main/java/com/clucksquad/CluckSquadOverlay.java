package com.clucksquad;

import java.awt.Dimension;
import java.awt.Graphics2D;

import javax.inject.Inject;

import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.components.LineComponent;
import net.runelite.client.ui.overlay.components.PanelComponent;

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

        PanelComponent panel = new PanelComponent();
        panel.getChildren().add(LineComponent.builder()
            .left("🐔 CLUCK SQUAD")
            .build());
        panel.getChildren().add(LineComponent.builder()
            .left("Next event:")
            .right(config.nextEvent())
            .build());
        panel.getChildren().add(LineComponent.builder()
            .left("Time:")
            .right(config.eventTime())
            .build());
        panel.getChildren().add(LineComponent.builder()
            .left("Attended:")
            .right(Integer.toString(config.attendance()))
            .build());
        panel.getChildren().add(LineComponent.builder()
            .left("Points:")
            .right(Integer.toString(config.points()))
            .build());

        return panel.render(graphics);
    }
}
