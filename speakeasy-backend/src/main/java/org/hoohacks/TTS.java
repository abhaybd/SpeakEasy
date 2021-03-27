package org.hoohacks;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.texttospeech.v1.AudioConfig;
import com.google.cloud.texttospeech.v1.AudioEncoding;
import com.google.cloud.texttospeech.v1.ListVoicesRequest;
import com.google.cloud.texttospeech.v1.SynthesisInput;
import com.google.cloud.texttospeech.v1.TextToSpeechClient;
import com.google.cloud.texttospeech.v1.TextToSpeechSettings;
import com.google.cloud.texttospeech.v1.Voice;
import com.google.cloud.texttospeech.v1.VoiceSelectionParams;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class TTS {

    public static final String DEF_LANG = "en-US";

    private static final TTS instance = new TTS(DEF_LANG);

    public static TTS getInstance() {
        return instance;
    }

    private final TextToSpeechClient client;
    private final String language;
    private final Set<String> voiceNames;

    public TTS(String language) {
        try {
            this.language = language;
            GoogleCredentials credentials;
            try (var credStream = TTS.class.getClassLoader().getResourceAsStream("speakeasy-gcp-tts.key.json")) {
                credentials = ServiceAccountCredentials.fromStream(Objects.requireNonNull(credStream));
            }

            var settings = TextToSpeechSettings.newBuilder()
                    .setCredentialsProvider(() -> credentials)
                    .build();
            client = TextToSpeechClient.create(settings);
            var req = ListVoicesRequest.getDefaultInstance();
            var response = client.listVoices(req);
            List<Voice> voices = response.getVoicesList();
            voiceNames = new HashSet<>(voices.size());
            for (Voice v : voices) {
                if (v.getLanguageCodesList().contains(language)) {
                    voiceNames.add(v.getName());
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Collection<String> getVoices() {
        return Collections.unmodifiableCollection(voiceNames);
    }

    public byte[] getSpeechRaw(String voiceName, String text) {
        return getSpeech(voiceName, text, AudioEncoding.LINEAR16);
    }

    public byte[] getSpeechMP3(String voiceName, String text) {
        return getSpeech(voiceName, text, AudioEncoding.MP3);
    }

    private byte[] getSpeech(String voiceName, String text, AudioEncoding encoding) {
        if (!voiceNames.contains(voiceName)) {
            throw new IllegalArgumentException("Unrecognized voice name: " + voiceName);
        }

        SynthesisInput input = SynthesisInput.newBuilder().setText(text).build();
        AudioConfig audioConfig = AudioConfig.newBuilder().setAudioEncoding(encoding).build();
        var voiceParams = VoiceSelectionParams.newBuilder()
                .setLanguageCode(language)
                .setName(voiceName)
                .build();

        var response = client.synthesizeSpeech(input, voiceParams, audioConfig);
        return response.getAudioContent().toByteArray();
    }
}
