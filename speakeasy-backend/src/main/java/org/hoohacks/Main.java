package org.hoohacks;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;
import javax.imageio.ImageIO;

public class Main {
    public static void main(String[] args) {
        try {
            if (!SystemTray.isSupported()) {
                System.err.println("System tray not supported!");
                return;
            }
            PopupMenu menu = new PopupMenu();
            BufferedImage iconImg = ImageIO.read(
                    Objects.requireNonNull(Main.class.getClassLoader().getResourceAsStream("tray-icon.png")));
            TrayIcon icon = new TrayIcon(iconImg, "SpeakEasy");
            icon.setImageAutoSize(true);
            SystemTray tray = SystemTray.getSystemTray();

            MenuItem start = new MenuItem("Start");
            MenuItem stop = new MenuItem("Stop");
            MenuItem exit = new MenuItem("Exit");

            start.addActionListener(Main::start);
            stop.addActionListener(Main::stop);
            exit.addActionListener(Main::exit);

            menu.add(start);
            menu.add(stop);
            menu.add(exit);

            icon.setPopupMenu(menu);

            tray.add(icon);
        } catch (IOException | AWTException e) {
            e.printStackTrace();
        }
    }

    private static void start(ActionEvent e) {
        System.out.println(e.getActionCommand());
    }

    private static void stop(ActionEvent e) {
        System.out.println(e.getActionCommand());
    }

    private static void exit(ActionEvent e) {
        System.out.println(e.getActionCommand());
    }
}
