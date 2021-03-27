package org.hoohacks;

import com.google.gson.Gson;
import static spark.Spark.*;

public class Main {
    public static void main(String[] args) {
        get("/voices", (req, res) -> {
            // get english voices
            Gson gson = new Gson();
            var voices = TTS.getInstance().getVoices();
            return gson.toJson(voices);
        });
    }
}
