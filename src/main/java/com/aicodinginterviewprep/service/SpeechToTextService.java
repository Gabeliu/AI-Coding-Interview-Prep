package com.aicodinginterviewprep.service;

import org.json.JSONObject;
import org.vosk.LibVosk;
import org.vosk.LogLevel;
import org.vosk.Model;
import org.vosk.Recognizer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Transcribes recorded audio to text entirely offline using Vosk, so this
 * feature works regardless of what a shared/restricted OpenAI API key allows.
 * The speech model is loaded lazily on first use since it takes a couple of
 * seconds to load and would otherwise delay app startup for every user,
 * even ones who never touch voice input.
 */
public class SpeechToTextService {
    private static final float SAMPLE_RATE = 16000f;
    private static final Path DEFAULT_MODEL_PATH = Path.of("models", "vosk-model-small-en-us-0.15");

    private final Path modelPath;
    private Model model;

    public SpeechToTextService() {
        this(DEFAULT_MODEL_PATH);
    }

    SpeechToTextService(Path modelPath) {
        this.modelPath = modelPath;
    }

    SpeechToTextService(Model model) {
        this.modelPath = null;
        this.model = model;
    }

    public synchronized String transcribe(byte[] pcmAudio) throws IOException {
        if (pcmAudio == null || pcmAudio.length == 0) {
            throw new IllegalStateException("No audio was recorded.");
        }

        ensureModelLoaded();

        try (Recognizer recognizer = new Recognizer(model, SAMPLE_RATE)) {
            recognizer.acceptWaveForm(pcmAudio, pcmAudio.length);
            return new JSONObject(recognizer.getFinalResult()).getString("text").trim();
        }
    }

    private void ensureModelLoaded() throws IOException {
        if (model != null) {
            return;
        }
        if (!Files.isDirectory(modelPath)) {
            throw new IllegalStateException(
                "Offline speech model not found at " + modelPath.toAbsolutePath()
                    + ". See README for setup instructions.");
        }

        LibVosk.setLogLevel(LogLevel.WARNINGS);
        model = new Model(modelPath.toAbsolutePath().toString());
    }
}
