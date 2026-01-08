import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class CreateTournamentFrame extends JFrame {

    private double chosenLat;
    private double chosenLon;

    public CreateTournamentFrame(MainFrame parent) {
        setTitle("Host a Tournament");
        setSize(420, 600);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel root = new JPanel();
        root.setBackground(new Color(255, 248, 240));
        root.setLayout(new BoxLayout(root, BoxLayout.Y_AXIS));
        root.setBorder(new EmptyBorder(20, 25, 20, 25));
        add(root);

        // ===== TITLE =====
        JLabel title = new JLabel("Host a Tournament");
        title.setFont(new Font("Arial", Font.BOLD, 20));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        root.add(title);
        root.add(Box.createVerticalStrut(25));

        // ===== TOURNAMENT NAME =====
        root.add(leftLabel("Tournament Name"));
        JTextField nameField = field();
        root.add(nameField);

        // ===== SPORT + DATE =====
        root.add(Box.createVerticalStrut(15));
        JPanel row = new JPanel(new GridLayout(1, 2, 12, 0));
        row.setOpaque(false);

        // Sport
        JPanel sportPanel = new JPanel();
        sportPanel.setOpaque(false);
        sportPanel.setLayout(new BoxLayout(sportPanel, BoxLayout.Y_AXIS));
        sportPanel.add(leftLabel("Sport"));
        JTextField sportField = field();
        sportPanel.add(sportField);

        // Date & Time (NON-EDITABLE)
        JPanel datePanel = new JPanel();
        datePanel.setOpaque(false);
        datePanel.setLayout(new BoxLayout(datePanel, BoxLayout.Y_AXIS));
        datePanel.add(leftLabel("Date & Time"));

        JSpinner dateSpinner = new JSpinner(new SpinnerDateModel());
        dateSpinner.setEditor(new JSpinner.DateEditor(dateSpinner, "MM/dd/yyyy"));

        JSpinner timeSpinner = new JSpinner(new SpinnerDateModel());
        timeSpinner.setEditor(new JSpinner.DateEditor(timeSpinner, "hh:mm"));

        JComboBox<String> ampmBox = new JComboBox<>(new String[]{"AM", "PM"});

        JPanel timeRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        timeRow.setOpaque(false);
        timeRow.add(timeSpinner);
        timeRow.add(ampmBox);

        datePanel.add(dateSpinner);
        datePanel.add(Box.createVerticalStrut(5));
        datePanel.add(timeRow);

        row.add(sportPanel);
        row.add(datePanel);
        root.add(row);

        // ===== LOCATION NAME =====
        root.add(Box.createVerticalStrut(15));
        root.add(leftLabel("Location Name"));
        JTextField locationField = field();
        root.add(locationField);

        JButton pickLocationBtn = new JButton("Pick Location on Map");
        pickLocationBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        pickLocationBtn.addActionListener(e ->
                new MapFrame((lat, lon) -> {
                    chosenLat = lat;
                    chosenLon = lon;
                })
        );
        root.add(Box.createVerticalStrut(6));
        root.add(pickLocationBtn);

        // ===== DESCRIPTION =====
        root.add(Box.createVerticalStrut(15));
        root.add(leftLabel("Description"));
        JTextArea descArea = new JTextArea(5, 20);
        descArea.setLineWrap(true);
        descArea.setWrapStyleWord(true);
        JScrollPane scroll = new JScrollPane(descArea);
        scroll.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
        root.add(scroll);

        // ===== CREATE BUTTON =====
        root.add(Box.createVerticalStrut(25));
        JButton createBtn = new JButton("Create Tournament");
        createBtn.setBackground(new Color(255, 122, 0));
        createBtn.setForeground(Color.WHITE);
        createBtn.setFocusPainted(false);
        createBtn.setFont(new Font("Arial", Font.BOLD, 15));
        createBtn.setPreferredSize(new Dimension(260, 45));
        createBtn.setMaximumSize(new Dimension(260, 45));
        createBtn.setAlignmentX(Component.CENTER_ALIGNMENT);

        createBtn.addActionListener(e -> {
            parent.addTournament(
                    nameField.getText(),
                    dateSpinner.getValue().toString(),
                    timeSpinner.getValue().toString() + " " + ampmBox.getSelectedItem(),
                    "All Ages",
                    sportField.getText(),
                    chosenLat,
                    chosenLon
            );
            dispose();
        });

        root.add(createBtn);

        setVisible(true);
    }

    // ===== HELPERS =====
    private JTextField field() {
        JTextField f = new JTextField();
        f.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        f.setFont(new Font("Arial", Font.PLAIN, 14));
        return f;
    }

    private JLabel leftLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("Arial", Font.BOLD, 13));
        l.setAlignmentX(Component.LEFT_ALIGNMENT);
        return l;
    }
}

