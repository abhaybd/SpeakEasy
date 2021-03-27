package org.hoohacks;

import com.google.gson.Gson;
import static spark.Spark.*;

public class Server {
    public static final int PORT = 4567;
    public static final String SAMPLE_MESSAGE = "Hello, World!";
    public static final Gson gson = new Gson();

    public static void start() {
        // serve static files from src/main/resources/public
        staticFiles.location("/public");

        // Set port for server
        port(PORT);

        CorsFilter.apply();

        // redirect localhost:PORT to localhost:PORT/index.html
        redirect.get("/", "/index.html");

        // Custom 404 page
        notFound("<html><body><h1>Error 404: Page not found</h1></body></html>");

        // Get a list of english voices
        get("/voices", (req, res) -> {
            var voices = TTS.getInstance().getVoices();
            return gson.toJson(voices);
        });

        // Play an audio sample of the requested voice
        post("/sample", (req, res) -> {
            String voiceName = gson.fromJson(req.body(), SampleMessageData.class).getVoiceName();
            AudioPlayer player = new AudioPlayer();
            player.play(TTS.getInstance().getSpeechRaw(voiceName, SAMPLE_MESSAGE));
            return null;
        });

        // Speak the requested text in the requested voice
        post("/speak", (req, res) -> {
            SpeakMessageData data = gson.fromJson(req.body(), SpeakMessageData.class);
            String voiceName = data.getVoiceName();
            String message = data.getName() + " says " + data.getMessage();
            AudioPlayer player = new AudioPlayer("CABLE Input (VB-Audio Virtual Cable)");
            player.play(TTS.getInstance().getSpeechRaw(voiceName, message));
            return null;
        });
    }

    public static void stop() {
        spark.Spark.stop();
    }

    public static String getHostUrl() {
        return "http://localhost:" + PORT;
    }

    private static class SampleMessageData {
        private String voiceName;

        public String getVoiceName() {
            return voiceName;
        }
    }

    private static class SpeakMessageData {
        private String name;
        private String voiceName;
        private String message;

        public String getName() {
            return name;
        }

        public String getVoiceName() {
            return voiceName;
        }

        public String getMessage() {
            return message;
        }
    }
}
