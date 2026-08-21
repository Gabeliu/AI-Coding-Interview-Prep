package com.aicodinginterviewprep.service;

import com.aicodinginterviewprep.config.EnvConfig;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.UUID;

public class SpeechToTextService {
    private static final String API_URL = "https://api.openai.com/v1/audio/transcriptions";
    private static final String MODEL = "whisper-1";

    private final HttpClient httpClient;
    private final String apiKey;

    public SpeechToTextService() {
        this(
            HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(10)).build(),
            EnvConfig.get("OPENAI_API_KEY"));
    }

    SpeechToTextService(HttpClient httpClient, String apiKey) {
        this.httpClient = httpClient;
        this.apiKey = apiKey;
    }

    public String transcribe(byte[] wavAudio) throws IOException, InterruptedException {
        if (apiKey == null || apiKey.isBlank()) {
            throw new IllegalStateException(
                "OPENAI_API_KEY is not set. Add it to your local .env file (see .env.example).");
        }
        if (wavAudio == null || wavAudio.length == 0) {
            throw new IllegalStateException("No audio was recorded.");
        }

        String boundary = "----AICodingInterviewPrep" + UUID.randomUUID();
        byte[] body = buildMultipartBody(boundary, wavAudio);

        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(API_URL))
            .timeout(Duration.ofSeconds(30))
            .header("Authorization", "Bearer " + apiKey)
            .header("Content-Type", "multipart/form-data; boundary=" + boundary)
            .POST(HttpRequest.BodyPublishers.ofByteArray(body))
            .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new IllegalStateException(
                "Transcription request failed (HTTP " + response.statusCode() + "): " + response.body());
        }

        return new JSONObject(response.body()).getString("text").trim();
    }

    static byte[] buildMultipartBody(String boundary, byte[] audioBytes) {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            String partPrefix = "--" + boundary + "\r\n";

            out.write((partPrefix
                + "Content-Disposition: form-data; name=\"model\"\r\n\r\n"
                + MODEL + "\r\n").getBytes(StandardCharsets.UTF_8));

            out.write((partPrefix
                + "Content-Disposition: form-data; name=\"file\"; filename=\"recording.wav\"\r\n"
                + "Content-Type: audio/wav\r\n\r\n").getBytes(StandardCharsets.UTF_8));
            out.write(audioBytes);
            out.write("\r\n".getBytes(StandardCharsets.UTF_8));

            out.write(("--" + boundary + "--\r\n").getBytes(StandardCharsets.UTF_8));
            return out.toByteArray();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
