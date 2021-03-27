package org.hoohacks;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URI;
import java.util.Objects;
import javax.imageio.ImageIO;

public class Main {
    public static void main(String[] args) throws IOException, AWTException {
        new Main().startApp();
    }

    private final MenuItem start, stop, exit;
    private final Desktop desktop;
    private final TrayIcon icon;

    private Main() throws IOException {
        if (!SystemTray.isSupported() || !Desktop.isDesktopSupported()) {
            throw new IllegalStateException("System tray and/or Desktop not supported!");
        }

        desktop = Desktop.getDesktop();
        if (!desktop.isSupported(Desktop.Action.BROWSE)) {
            throw new IllegalStateException("Browsing not supported!");
        }

        PopupMenu menu = new PopupMenu();
        BufferedImage iconImg = ImageIO.read(
                Objects.requireNonNull(Main.class.getClassLoader().getResourceAsStream("tray-icon.png")));
        icon = new TrayIcon(iconImg, "SpeakEasy");
        icon.setImageAutoSize(true);

        start = new MenuItem("Start");
        stop = new MenuItem("Stop");
        exit = new MenuItem("Exit");

        start.addActionListener(this::start);
        stop.addActionListener(this::stop);
        exit.addActionListener(this::exit);

        menu.add(start);
        menu.add(stop);
        menu.add(exit);

        icon.setPopupMenu(menu);
    }

    public void startApp() throws AWTException {
        SystemTray tray = SystemTray.getSystemTray();
        tray.add(icon);
        start(null);
    }

    private void start(ActionEvent event) {
        Server.start();
        stop.setEnabled(true);
        try {
            desktop.browse(URI.create("http://localhost:5000"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void stop(ActionEvent e) {
        Server.stop();
        stop.setEnabled(false);
    }

    private void exit(ActionEvent e) {
        Server.stop();
        System.exit(0);
    }
}
