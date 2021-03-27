package org.hoohacks;

import com.google.gson.Gson;
import static spark.Spark.*;

public class Server {
    public static void start() {
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
