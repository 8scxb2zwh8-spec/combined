/**
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
**/
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class MainFrame extends JFrame {

    private JPanel itemsPanel;
    private JLabel noTournamentsLabel;

    private ArrayList<Tournament> tournaments = new ArrayList<>();

    private double chosenLat;
    private double chosenLon;

    public MainFrame() {
        setTitle("Tournament App");
        setSize(600, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // ---------------- TOP BAR ----------------
        JButton addButton = new JButton("+");
        addButton.setFont(new Font("Arial", Font.BOLD, 18));
        addButton.setPreferredSize(new Dimension(45, 30));

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        topPanel.add(addButton, BorderLayout.EAST);
        add(topPanel, BorderLayout.NORTH);

        // ---------------- LIST PANEL ----------------
        itemsPanel = new JPanel();
        itemsPanel.setLayout(new BoxLayout(itemsPanel, BoxLayout.Y_AXIS));
        itemsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        noTournamentsLabel = new JLabel("No Tournaments Available");
        noTournamentsLabel.setFont(new Font("Arial", Font.BOLD, 16));
        noTournamentsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        itemsPanel.add(noTournamentsLabel);

        JScrollPane scrollPane = new JScrollPane(itemsPanel);
        add(scrollPane, BorderLayout.CENTER);

        addButton.addActionListener(e -> new CreateTournamentFrame(this));

    }
    
    public void addTournament(String name, String date, String time,
                          String age, String game, double lat, double lon) {
    tournaments.add(new Tournament(name, date, time, age, game, lat, lon));
    refreshPosts();
}

    
    // =========================================================
    // CREATE TOURNAMENT WINDOW
    // =========================================================
    private void openCreateTournamentWindow() {
        JFrame frame = new JFrame("Create Tournament");
        frame.setSize(400, 360);
        frame.setLocationRelativeTo(this);
        frame.setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridLayout(5, 2, 8, 8));
        formPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JTextField nameField = new JTextField();
        JTextField dateField = new JTextField();
        JTextField timeField = new JTextField();
        JTextField ageField = new JTextField();
        JTextField gameField = new JTextField();

        formPanel.add(new JLabel("Name:")); formPanel.add(nameField);
        formPanel.add(new JLabel("Date:")); formPanel.add(dateField);
        formPanel.add(new JLabel("Time:")); formPanel.add(timeField);
        formPanel.add(new JLabel("Age Restriction:")); formPanel.add(ageField);
        formPanel.add(new JLabel("Game:")); formPanel.add(gameField);

        frame.add(formPanel, BorderLayout.CENTER);

        JButton pickLocationBtn = new JButton("Pick Location");
        JButton finishBtn = new JButton("Finish");
        JButton cancelBtn = new JButton("Cancel");
        finishBtn.setEnabled(false);

        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonsPanel.add(pickLocationBtn);
        buttonsPanel.add(cancelBtn);
        buttonsPanel.add(finishBtn);

        frame.add(buttonsPanel, BorderLayout.SOUTH);

        // Pick location
        pickLocationBtn.addActionListener(e -> {
            new MapFrame((lat, lon) -> {
                chosenLat = lat;
                chosenLon = lon;
                finishBtn.setEnabled(true);
            });
        });

        // Finish
        finishBtn.addActionListener(e -> {
            if (nameField.getText().trim().isEmpty()
                    || dateField.getText().trim().isEmpty()
                    || timeField.getText().trim().isEmpty()
                    || ageField.getText().trim().isEmpty()
                    || gameField.getText().trim().isEmpty()) {
                return;
            }

            tournaments.add(new Tournament(
                    nameField.getText(),
                    dateField.getText(),
                    timeField.getText(),
                    ageField.getText(),
                    gameField.getText(),
                    chosenLat,
                    chosenLon
            ));

            refreshPosts();
            frame.dispose();
        });

        cancelBtn.addActionListener(e -> frame.dispose());
        frame.setVisible(true);
    }

    // =========================================================
    // REFRESH POSTS
    // =========================================================
    private void refreshPosts() {
        itemsPanel.removeAll();

        if (tournaments.isEmpty()) {
            itemsPanel.add(noTournamentsLabel);
        } else {
            for (Tournament t : tournaments) {
                itemsPanel.add(createPostCard(t));
                itemsPanel.add(Box.createVerticalStrut(10));
            }
        }

        itemsPanel.revalidate();
        itemsPanel.repaint();
    }

    // =========================================================
    // CREATE POST CARD
    // =========================================================
    private JPanel createPostCard(Tournament t) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.GRAY),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        JLabel title = new JLabel(t.name);
        title.setFont(new Font("Arial", Font.BOLD, 16));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel detailsPanel = new JPanel();
        detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS));
        detailsPanel.setVisible(false);

        detailsPanel.add(new JLabel("Date: " + t.date));
        detailsPanel.add(new JLabel("Time: " + t.time));
        detailsPanel.add(new JLabel("Age: " + t.age));
        detailsPanel.add(new JLabel("Game: " + t.game));

        JButton seeMoreBtn = new JButton("See More");
        JButton locateBtn = new JButton("Locate");

        seeMoreBtn.addActionListener(e -> {
            boolean visible = detailsPanel.isVisible();
            detailsPanel.setVisible(!visible);
            seeMoreBtn.setText(visible ? "See More" : "See Less");
            card.revalidate();
        });

        locateBtn.addActionListener(e -> new MapFrame(t.lat, t.lon));

        JPanel buttonRow = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonRow.add(seeMoreBtn);
        buttonRow.add(locateBtn);

        card.add(title);
        card.add(Box.createVerticalStrut(5));
        card.add(detailsPanel);
        card.add(Box.createVerticalStrut(5));
        card.add(buttonRow);

        return card;
    }

    // =========================================================
    // MAIN
    // =========================================================
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainFrame().setVisible(true));
    }

    // =========================================================
    // DATA CLASS
    // =========================================================
    static class Tournament {
        String name, date, time, age, game;
        double lat, lon;

        Tournament(String name, String date, String time,
                   String age, String game,
                   double lat, double lon) {
            this.name = name;
            this.date = date;
            this.time = time;
            this.age = age;
            this.game = game;
            this.lat = lat;
            this.lon = lon;
        }
    }
}
