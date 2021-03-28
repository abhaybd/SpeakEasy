package org.hoohacks;

import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.Semaphore;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;

public class AudioPlayer {
    private final Mixer.Info info;
    private final Semaphore clipStartSemaphore, clipEndSemaphore;

    public AudioPlayer() {
        this.info = null;
        clipStartSemaphore = new Semaphore(1);
        clipEndSemaphore = new Semaphore(0);
    }

    public AudioPlayer(String deviceName) throws IOException {
        this.info = Arrays.stream(AudioSystem.getMixerInfo())
                .filter(i -> i.getName().equals(deviceName))
                .findFirst()
                .orElseThrow(() -> new IOException("No device found: " + deviceName));
        clipStartSemaphore = new Semaphore(1);
        clipEndSemaphore = new Semaphore(0);
    }

    public void play(AudioInputStream ais) throws LineUnavailableException {
        try {
            clipStartSemaphore.acquire();
            Clip clip = info != null ? AudioSystem.getClip(info) : AudioSystem.getClip();
            try {
                clip.addLineListener(this::lineListener);
                clip.open(ais);
                clip.start();
                clipEndSemaphore.acquire();
                clip.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } catch (InterruptedException e) {
            // ignored
        } finally {
            clipStartSemaphore.drainPermits();
            clipStartSemaphore.release();
        }
    }

    private void lineListener(LineEvent event) {
        if (event.getType() == LineEvent.Type.STOP) {
            clipEndSemaphore.drainPermits();
            clipEndSemaphore.release();
        }
    }
}
