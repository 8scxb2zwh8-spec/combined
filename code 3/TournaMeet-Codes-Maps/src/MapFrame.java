import javax.swing.*;
import java.awt.*;
import java.net.URL;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import netscape.javascript.JSObject;

public class MapFrame extends JFrame {

    private JFXPanel mapPanel;
    private WebEngine engine;
    private JButton pinButton;
    private double selectedLat = Double.NaN;
    private double selectedLon = Double.NaN;
    private LocationSelectedCallback callback;

    public interface LocationSelectedCallback {
        void onLocationSelected(double lat, double lon);
    }

    // Constructor for picking location
    public MapFrame(LocationSelectedCallback callback) {
        super("Map - Pick Location");
        this.callback = callback;

        setSize(800, 650);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        mapPanel = new JFXPanel();
        add(mapPanel, BorderLayout.CENTER);

        // Add pin button below the map
        pinButton = new JButton("Pin Location");
        pinButton.setEnabled(false);
        pinButton.addActionListener(e -> {
            if (!Double.isNaN(selectedLat) && !Double.isNaN(selectedLon)) {
                // Pass the selected location back
                callback.onLocationSelected(selectedLat, selectedLon);
                // Close the map window
                dispose();
            }
        });
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(pinButton);
        add(buttonPanel, BorderLayout.SOUTH);

        Platform.runLater(this::initPickerMap);
    }

    // Constructor for viewing a location
    public MapFrame(double latitude, double longitude) {
        super("Map - View Location");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        mapPanel = new JFXPanel();
        add(mapPanel, BorderLayout.CENTER);

        Platform.runLater(() -> initViewerMap(latitude, longitude));
    }

    private void initPickerMap() {
        WebView webView = new WebView();
        engine = webView.getEngine();

        URL url = getClass().getResource("/resources/map.html");
        if (url == null) {
            SwingUtilities.invokeLater(() -> {
                JOptionPane.showMessageDialog(this, "map.html not found in resources!", "Error", JOptionPane.ERROR_MESSAGE);
                dispose();
            });
            return;
        }

        engine.load(url.toExternalForm());

        engine.getLoadWorker().stateProperty().addListener((obs, oldState, newState) -> {
            if (newState == javafx.concurrent.Worker.State.SUCCEEDED) {
                JSObject window = (JSObject) engine.executeScript("window");
                // Expose a Java object for JS to call when map clicked
                window.setMember("javaConnector", new JSBridge());

                // Enable click listener in JS map.html
                engine.executeScript("enablePicker(function(lat, lon){javaConnector.onLocationSelected(lat, lon);});");

                SwingUtilities.invokeLater(() -> setVisible(true));
            }
        });

        mapPanel.setScene(new Scene(webView));
    }

    private void initViewerMap(double latitude, double longitude) {
        WebView webView = new WebView();
        engine = webView.getEngine();

        URL url = getClass().getResource("/resources/map.html");
        if (url == null) {
            SwingUtilities.invokeLater(() -> {
                JOptionPane.showMessageDialog(this, "map.html not found in resources!", "Error", JOptionPane.ERROR_MESSAGE);
                dispose();
            });
            return;
        }

        engine.load(url.toExternalForm());

        engine.getLoadWorker().stateProperty().addListener((obs, oldState, newState) -> {
            if (newState == javafx.concurrent.Worker.State.SUCCEEDED) {
                engine.executeScript("setMarker(" + latitude + "," + longitude + ");");
                SwingUtilities.invokeLater(() -> setVisible(true));
            }
        });

        mapPanel.setScene(new Scene(webView));
    }

    public class JSBridge {
        // Called from JS when map is clicked
        public void onLocationSelected(double lat, double lon) {
            selectedLat = lat;
            selectedLon = lon;
            // Enable the Pin button
            SwingUtilities.invokeLater(() -> pinButton.setEnabled(true));
        }
    }
}
