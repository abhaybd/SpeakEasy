package org.hoohacks;

import com.google.gson.Gson;
import static spark.Spark.*;

public class Server {
    public static final int PORT = 4567;
    public static final String SAMPLE_MESSAGE = "Hello, World!";
    public static final Gson gson = new Gson();

    public static void start() {
        port(PORT);
        staticFiles.location("/public");

        get("/voices", (req, res) -> {
            // get english voices
            var voices = TTS.getInstance().getVoices();
            return gson.toJson(voices);
        });
    }

    public static void stop() {
        spark.Spark.stop();
    }

    public static String getHostUrl() {
        return "http://localhost:" + PORT;
    }
}
