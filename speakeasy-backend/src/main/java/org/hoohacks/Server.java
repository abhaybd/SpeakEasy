package org.hoohacks;

import com.google.gson.Gson;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import spark.Spark;

import static spark.Spark.*;

public class Server {
    public static final int PORT = 4567;
    public static final String SAMPLE_MESSAGE = "Hello! Would you like this to be your voice?";
    public static final Gson gson = new Gson();

    private static final ExecutorService threadPool = Executors.newCachedThreadPool();

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

        AudioPlayer pipedPlayer;
        try {
            pipedPlayer = new AudioPlayer("CABLE Input (VB-Audio Virtual Cable)");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        AudioPlayer defaultPlayer = new AudioPlayer();

        // Get a list of english voices
        get("/voices", (req, res) -> {
            var voices = TTS.getInstance().getVoices();
            return gson.toJson(voices);
        });

        // Play an audio sample of the requested voice
        post("/sample", (req, res) -> {
            String voiceName = gson.fromJson(req.body(), SampleMessageData.class).getVoiceName();
            threadPool.submit(() -> {
                try {
                    defaultPlayer.play(TTS.getInstance().getSpeechRaw(voiceName, SAMPLE_MESSAGE));
                } catch (LineUnavailableException e) {
                    e.printStackTrace();
                }
            });
            return "";
        });

        // Speak the requested text in the requested voice
        post("/speak", (req, res) -> {
            SpeakMessageData data = gson.fromJson(req.body(), SpeakMessageData.class);
            String voiceName = data.getVoiceName();
            String message = data.getMessage();
            byte[] bytes = TTS.getInstance().getSpeechRawBytes(voiceName, message);
            threadPool.submit(() -> {
                try {
                    ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
                    AudioInputStream ais = AudioSystem.getAudioInputStream(bais);
                    pipedPlayer.play(ais);
                } catch (LineUnavailableException | UnsupportedAudioFileException | IOException e) {
                    e.printStackTrace();
                }
            });
            threadPool.submit(() -> {
                try {
                    ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
                    AudioInputStream ais = AudioSystem.getAudioInputStream(bais);
                    defaultPlayer.play(ais);
                } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
                    e.printStackTrace();
                }
            });
            return "";
        });
    }

    public static void stop() {
        Spark.stop();
        threadPool.shutdownNow();
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
        private String voiceName;
        private String message;

        public String getVoiceName() {
            return voiceName;
        }

        public String getMessage() {
            return message;
        }
    }
}
