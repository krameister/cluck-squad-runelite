package com.clucksquad;

import java.awt.Dimension;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import net.runelite.client.ui.PluginPanel;

public class CluckSquadPanel extends PluginPanel
{
    private final CluckSquadConfig config;

    public CluckSquadPanel(CluckSquadConfig config)
    {
        this.config = config;
        rebuild();
    }

    public void rebuild()
    {
        removeAll();

        JLabel title = new JLabel("CLUCK SQUAD", SwingConstants.CENTER);
        title.setBorder(new EmptyBorder(8, 4, 8, 4));
        add(title);

        JPanel eventPanel = new JPanel(new GridLayout(0, 1, 0, 4));
        eventPanel.setBorder(BorderFactory.createEmptyBorder(4, 4, 8, 4));

        eventPanel.add(new JLabel("Next event: " + config.nextEvent()));
        eventPanel.add(new JLabel("Type: " + config.eventType()));
        eventPanel.add(new JLabel("Boss / Activity: " + config.eventBoss()));
        eventPanel.add(new JLabel("Time: " + config.eventTime()));
        eventPanel.add(new JLabel("Attending: " + (config.attending() ? "YES" : "NO")));
        eventPanel.add(new JLabel("My attendance: " + config.attendance()));
        eventPanel.add(new JLabel("My points: " + config.points()));

        add(eventPanel);

        JLabel details = new JLabel("<html><body style='width: 190px'>" +
            config.eventDetails() + "</body></html>");
        details.setBorder(new EmptyBorder(8, 4, 8, 4));
        add(details);

        revalidate();
        repaint();
    }

    @Override
    public Dimension getPreferredSize()
    {
        return new Dimension(PluginPanel.PANEL_WIDTH, 300);
    }
}
