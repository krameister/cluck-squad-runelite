package com.clucksquad;

import java.awt.Dimension;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.ui.PluginPanel;

public class CluckSquadPanel extends PluginPanel
{
    private final CluckSquadConfig config;
    private final ConfigManager configManager;

    public CluckSquadPanel(CluckSquadConfig config, ConfigManager configManager)
    {
        this.config = config;
        this.configManager = configManager;
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

        eventPanel.add(new JLabel("Upcoming Event"));
        eventPanel.add(new JLabel(config.nextEvent()));
        eventPanel.add(new JLabel("Type: " + config.eventType()));
        eventPanel.add(new JLabel("Boss / Activity: " + config.eventBoss()));
        eventPanel.add(new JLabel("Time: " + config.eventTime()));

        JButton attendButton = new JButton(config.attending() ? "✓ I'm Attending" : "I'm Attending");
        attendButton.addActionListener(e ->
            configManager.setConfiguration("clucksquad", "attending", true));
        eventPanel.add(attendButton);

        JButton declineButton = new JButton("I'm Not Attending");
        declineButton.addActionListener(e ->
            configManager.setConfiguration("clucksquad", "attending", false));
        eventPanel.add(declineButton);

        eventPanel.add(new JLabel("My attendance: " + config.attendance()));
        eventPanel.add(new JLabel("My points: " + config.points()));

        add(eventPanel);

        JLabel status = new JLabel(
            "<html><body style='width: 190px'>" +
            (config.attending() ? "You are marked as attending." : "You are not currently marked as attending.") +
            "</body></html>"
        );
        status.setBorder(new EmptyBorder(8, 4, 8, 4));
        add(status);

        JLabel details = new JLabel("<html><body style='width: 190px'>" +
            config.eventDetails() + "</body></html>");
        details.setBorder(new EmptyBorder(4, 4, 8, 4));
        add(details);

        revalidate();
        repaint();
    }

    @Override
    public Dimension getPreferredSize()
    {
        return new Dimension(PluginPanel.PANEL_WIDTH, 360);
    }
}
