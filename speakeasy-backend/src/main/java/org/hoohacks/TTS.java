package org.hoohacks;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.texttospeech.v1.AudioConfig;
import com.google.cloud.texttospeech.v1.AudioEncoding;
import com.google.cloud.texttospeech.v1.SynthesisInput;
import com.google.cloud.texttospeech.v1.TextToSpeechClient;
import com.google.cloud.texttospeech.v1.TextToSpeechSettings;
import com.google.cloud.texttospeech.v1.Voice;
import com.google.cloud.texttospeech.v1.VoiceSelectionParams;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

public class TTS {

    public static final String DEF_LANG = "en-US";

    private static final TTS instance = new TTS(DEF_LANG);

    public static TTS getInstance() {
        return instance;
    }

    private final TextToSpeechClient client;
    private final String language;
    private final Map<String, Voice> voiceMap;

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
            var response = client.listVoices(language);
            List<Voice> voices = response.getVoicesList();
            voiceMap = new HashMap<>(voices.size());
            for (Voice v : voices) {
                voiceMap.put(v.getName(), v);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Collection<String> getVoices() {
        return Collections.unmodifiableCollection(voiceMap.keySet());
    }

    public AudioInputStream getSpeechRaw(String voiceName, String text) {
        if (!voiceMap.containsKey(voiceName)) {
            throw new IllegalArgumentException("Unrecognized voice name: " + voiceName);
        }

        SynthesisInput input = SynthesisInput.newBuilder().setText(text).build();
        AudioConfig audioConfig = AudioConfig.newBuilder().setAudioEncoding(AudioEncoding.LINEAR16).build();
        var voiceParams = VoiceSelectionParams.newBuilder()
                .setLanguageCode(language)
                .setName(voiceName)
                .build();

        var response = client.synthesizeSpeech(input, voiceParams, audioConfig);
        byte[] bytes = response.getAudioContent().toByteArray();
        ByteArrayInputStream in = new ByteArrayInputStream(bytes);
        try {
            return AudioSystem.getAudioInputStream(in);
        } catch (UnsupportedAudioFileException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public byte[] getSpeechMP3(String voiceName, String text) {
        if (!voiceMap.containsKey(voiceName)) {
            throw new IllegalArgumentException("Unrecognized voice name: " + voiceName);
        }

        SynthesisInput input = SynthesisInput.newBuilder().setText(text).build();
        AudioConfig audioConfig = AudioConfig.newBuilder().setAudioEncoding(AudioEncoding.MP3).build();
        var voiceParams = VoiceSelectionParams.newBuilder()
                .setLanguageCode(language)
                .setName(voiceName)
                .build();

        return client.synthesizeSpeech(input, voiceParams, audioConfig).getAudioContent().toByteArray();
    }
}
