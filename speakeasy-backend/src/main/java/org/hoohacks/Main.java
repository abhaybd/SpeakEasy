package org.hoohacks;

import java.util.*;
import java.net.*;
import java.net.http.*;
import java.net.http.HttpResponse.*;
import com.google.gson.Gson;
import static spark.Spark.*;

public class Main {
    public static void main(String[] args) {
        get("/voices", (req, res) -> {
            // get english voices
            HttpClient client  = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://texttospeech.googleapis.com/v1/voices?languageCode=en-US"))
                    .build();
            HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
            String body = response.body();

            // Transform json into list of voices
            Gson gson = new Gson();
            Data data = gson.fromJson(body, Data.class);
            Voice[] voices = data.getVoices();
            MinimalData minimalData = new MinimalData(voices);

            return gson.toJson(minimalData);
        });
    }

    private static class Data {
        private final Voice[] voices;

        public Data(Voice[] voices) {
            this.voices = voices;
        }

        public Voice[] getVoices() {
            return voices;
        }
    }

    private static class MinimalData {
        private final MinimalVoice[] voices;

        public MinimalData(Voice[] voices) {
            this.voices = new MinimalVoice[voices.length];

            // extract relevant information from list of voices
            for (int i = 0 ; i < voices.length; i++) {
                this.voices[i] = new MinimalVoice(voices[i]);
            }
        }
    }

    private static class Voice {
        private final String[] languageCodes;
        private final String name;
        private final SsmlVoiceGender ssmlGender;
        private final int naturalSampleRateHertz;

        public Voice(String[] languageCodes, String name, SsmlVoiceGender ssmlGender, int naturalSampleRateHertz) {
            this.languageCodes = languageCodes;
            this.name = name;
            this.ssmlGender = ssmlGender;
            this.naturalSampleRateHertz = naturalSampleRateHertz;
        }

        public String getName() {
            return name;
        }

        public String getGender() {
            return ssmlGender == SsmlVoiceGender.SSML_VOICE_GENDER_UNSPECIFIED ? "UNSPECIFIED" : ssmlGender.toString();
        }

        private enum SsmlVoiceGender {
            SSML_VOICE_GENDER_UNSPECIFIED,
            MALE,
            FEMALE,
            NEUTRAL
        }
    }

    private static class MinimalVoice {
        private final String name;
        private final String gender;

        public MinimalVoice(Voice voice) {
            this.name = voice.getName();
            this.gender = voice.getGender();
        }
    }
}
