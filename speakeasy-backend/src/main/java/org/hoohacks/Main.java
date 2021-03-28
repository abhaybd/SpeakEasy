package org.hoohacks;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

public class Main extends Application {
    public static void main(String[] args) {
        Server.start();
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("SpeakEasy");

        WebView webView = new WebView();

        var engine = webView.getEngine();
        engine.setOnAlert(e -> System.out.println(e.getData()));
        engine.setJavaScriptEnabled(true);
        engine.load(Server.getHostUrl());

        Scene scene = new Scene(webView, 700, 750);

        primaryStage.setScene(scene);
        primaryStage.show();

        Platform.setImplicitExit(true);
    }

    @Override
    public void stop() {
        Server.stop();
    }
}
