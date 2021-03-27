package org.hoohacks;

import com.google.gson.Gson;
import static spark.Spark.*;

public class Server {
    public static final int PORT = 4567;

    public static void start() {
        start(PORT);
    }

    public static void start(int port) {
        port(port);
        staticFiles.location("/public");

        get("/voices", (req, res) -> {
            // get english voices
            Gson gson = new Gson();
            var voices = TTS.getInstance().getVoices();
            return gson.toJson(voices);
        });
    }

    public static void stop() {
        spark.Spark.stop();
    }
}
