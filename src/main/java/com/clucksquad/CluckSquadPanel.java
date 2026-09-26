package com.clucksquad;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.ui.PluginPanel;

public class CluckSquadPanel extends PluginPanel
{
    private final CluckSquadConfig config;
    private final ConfigManager configManager;
    private final List<DemoEvent> events = new ArrayList<>();

    private JLabel statsLabel;

    public CluckSquadPanel(CluckSquadConfig config, ConfigManager configManager)
    {
        this.config = config;
        this.configManager = configManager;
        seedEvents();
        rebuild();
    }

    private void seedEvents()
    {
        events.add(new DemoEvent(config.nextEvent(), config.eventType(), config.eventBoss(), config.eventTime(), config.eventDetails()));
        events.add(new DemoEvent("Bandos God Wars", "Bossing", "General Graardor", "11:00 PM BST", "Cluck Squad bossing night"));
        events.add(new DemoEvent("Huey Training Night", "Training", "Hueycoatl", "9:00 PM BST", "Learn the mechanics together"));
    }

    public void rebuild()
    {
        removeAll();

        JPanel root = new JPanel(new BorderLayout());
        root.setBorder(new EmptyBorder(6, 6, 6, 6));

        JLabel title = new JLabel("🐔 CLUCK SQUAD", SwingConstants.CENTER);
        title.setBorder(new EmptyBorder(4, 4, 8, 4));
        root.add(title, BorderLayout.NORTH);

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Events", createEventsTab());
        tabs.addTab("Leaderboard", createLeaderboardTab());
        tabs.addTab("My Stats", createStatsTab());
        tabs.addTab("Admin", createAdminTab());

        root.add(tabs, BorderLayout.CENTER);
        add(root);
        revalidate();
        repaint();
    }

    private JPanel createEventsTab()
    {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new EmptyBorder(8, 2, 8, 2));

        for (DemoEvent event : events)
        {
            JPanel card = new JPanel(new BorderLayout(4, 4));
            card.setAlignmentX(JPanel.LEFT_ALIGNMENT);
            card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEtchedBorder(),
                new EmptyBorder(6, 6, 6, 6)
            ));

            String text = "<html><b>" + event.name + "</b><br>" +
                "<b>Type:</b> " + event.type + "<br>" +
                "<b>Boss / Activity:</b> " + event.boss + "<br>" +
                "<b>Time:</b> " + event.time + "<br>" +
                "<b>Details:</b> " + event.details + "</html>";
            card.add(new JLabel(text), BorderLayout.CENTER);

            JPanel buttons = new JPanel(new FlowLayout(FlowLayout.CENTER, 3, 2));

            JButton join = new JButton(event.joined ? "✓ Joined" : "Join");
            join.addActionListener(e ->
            {
                event.joined = true;
                if (event == events.get(0))
                {
                    configManager.setConfiguration("clucksquad", "attending", true);
                }
                rebuild();
            });

            JButton leave = new JButton("Leave");
            leave.addActionListener(e ->
            {
                event.joined = false;
                if (event == events.get(0))
                {
                    configManager.setConfiguration("clucksquad", "attending", false);
                }
                rebuild();
            });

            JButton complete = new JButton("Complete");
            complete.addActionListener(e -> completeEvent(event));

            buttons.add(join);
            buttons.add(leave);
            buttons.add(complete);
            card.add(buttons, BorderLayout.SOUTH);

            card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 155));
            panel.add(card);
        }

        return wrapScrollable(panel);
    }

    private JPanel createLeaderboardTab()
    {
        JPanel panel = new JPanel(new GridLayout(0, 1, 0, 6));
        panel.setBorder(new EmptyBorder(8, 8, 8, 8));
        panel.add(new JLabel("🏆 CLUCK SQUAD LEADERBOARD"));
        panel.add(new JLabel("1. Belt Cluckle — 145 pts"));
        panel.add(new JLabel("2. Chicken Legs — 120 pts"));
        panel.add(new JLabel("3. Cluck Norris — 95 pts"));
        panel.add(new JLabel("4. Eggcellent — 80 pts"));
        panel.add(new JLabel("5. You — " + config.points() + " pts"));
        panel.add(new JLabel("<html><br>Online leaderboard will replace these demo members when the shared clan server is added.</html>"));
        return wrapScrollable(panel);
    }

    private JPanel createStatsTab()
    {
        JPanel panel = new JPanel(new GridLayout(0, 1, 0, 6));
        panel.setBorder(new EmptyBorder(8, 8, 8, 8));

        statsLabel = new JLabel();
        updateStatsLabel();

        panel.add(new JLabel("🐔 MY CLUCK SQUAD STATS"));
        panel.add(statsLabel);

        JButton addAttendance = new JButton("Record Attendance");
        addAttendance.addActionListener(e ->
        {
            configManager.setConfiguration("clucksquad", "attendance", config.attendance() + 1);
            rebuild();
        });

        JButton addPoints = new JButton("Award 10 Points");
        addPoints.addActionListener(e ->
        {
            configManager.setConfiguration("clucksquad", "points", config.points() + 10);
            rebuild();
        });

        JButton reset = new JButton("Reset My Demo Stats");
        reset.addActionListener(e ->
        {
            configManager.setConfiguration("clucksquad", "attendance", 0);
            configManager.setConfiguration("clucksquad", "points", 0);
            rebuild();
        });

        panel.add(addAttendance);
        panel.add(addPoints);
        panel.add(reset);
        return wrapScrollable(panel);
    }

    private void updateStatsLabel()
    {
        if (statsLabel != null)
        {
            statsLabel.setText("<html>Events attended: <b>" + config.attendance() +
                "</b><br>Points: <b>" + config.points() +
                "</b><br>Current event: <b>" +
                (config.attending() ? "JOINED" : "NOT JOINED") + "</b></html>");
        }
    }

    private JPanel createAdminTab()
    {
        JPanel panel = new JPanel(new GridLayout(0, 1, 0, 6));
        panel.setBorder(new EmptyBorder(8, 8, 8, 8));
        panel.add(new JLabel("⚙ CLUCK SQUAD EVENT ADMIN"));
        panel.add(new JLabel("Local demo controls — shared admin tools come with the online server."));

        JButton create = new JButton("Create Demo Event");
        create.addActionListener(e -> createEvent());

        JButton complete = new JButton("Complete Current Event +10 Points");
        complete.addActionListener(e ->
        {
            configManager.setConfiguration("clucksquad", "attendance", config.attendance() + 1);
            configManager.setConfiguration("clucksquad", "points", config.points() + 10);
            configManager.setConfiguration("clucksquad", "attending", false);
            rebuild();
            JOptionPane.showMessageDialog(this, "Event completed. Attendance +1 and 10 points awarded.");
        });

        panel.add(create);
        panel.add(complete);
        return wrapScrollable(panel);
    }

    private JPanel wrapScrollable(JPanel panel)
    {
        JPanel wrapper = new JPanel(new BorderLayout());
        JScrollPane scrollPane = new JScrollPane(panel);
        scrollPane.setBorder(null);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        wrapper.add(scrollPane, BorderLayout.CENTER);
        return wrapper;
    }

    private void createEvent()
    {
        JTextField name = new JTextField();
        JTextField type = new JTextField("Bossing");
        JTextField boss = new JTextField();
        JTextField time = new JTextField();
        JTextField details = new JTextField();

        JPanel form = new JPanel(new GridLayout(0, 1, 0, 4));
        form.add(new JLabel("Event name"));
        form.add(name);
        form.add(new JLabel("Event type"));
        form.add(type);
        form.add(new JLabel("Boss / activity"));
        form.add(boss);
        form.add(new JLabel("Time"));
        form.add(time);
        form.add(new JLabel("Details"));
        form.add(details);

        int result = JOptionPane.showConfirmDialog(this, form, "Create Cluck Squad Event", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION && !name.getText().trim().isEmpty())
        {
            events.add(new DemoEvent(
                name.getText().trim(),
                type.getText().trim(),
                boss.getText().trim(),
                time.getText().trim(),
                details.getText().trim()
            ));
            rebuild();
        }
    }

    private void completeEvent(DemoEvent event)
    {
        if (event == events.get(0))
        {
            configManager.setConfiguration("clucksquad", "attendance", config.attendance() + 1);
            configManager.setConfiguration("clucksquad", "points", config.points() + 10);
            configManager.setConfiguration("clucksquad", "attending", false);
        }

        JOptionPane.showMessageDialog(this, "Event completed: " + event.name + "\nDemo reward: +1 attendance, +10 points.");
        rebuild();
    }

    @Override
    public Dimension getPreferredSize()
    {
        return new Dimension(PluginPanel.PANEL_WIDTH, 500);
    }

    private static class DemoEvent
    {
        private final String name;
        private final String type;
        private final String boss;
        private final String time;
        private final String details;
        private boolean joined;

        private DemoEvent(String name, String type, String boss, String time, String details)
        {
            this.name = name;
            this.type = type;
            this.boss = boss;
            this.time = time;
            this.details = details;
        }
    }
}
