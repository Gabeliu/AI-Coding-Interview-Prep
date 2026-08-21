package com.aicodinginterviewprep.service;

import org.junit.jupiter.api.Test;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SpeechToTextServiceTest {

    private static final byte[] SOME_AUDIO = {1, 2, 3, 4};

    @Test
    @SuppressWarnings("unchecked")
    void transcribeReturnsTrimmedTextFromA200Response() throws Exception {
        HttpClient httpClient = mock(HttpClient.class);
        HttpResponse<String> response = mock(HttpResponse.class);
        when(response.statusCode()).thenReturn(200);
        when(response.body()).thenReturn("{\"text\": \"  What is a hash map?  \"}");
        when(httpClient.<String>send(any(HttpRequest.class), any())).thenReturn(response);

        SpeechToTextService service = new SpeechToTextService(httpClient, "fake-key");

        assertEquals("What is a hash map?", service.transcribe(SOME_AUDIO));
    }

    @Test
    @SuppressWarnings("unchecked")
    void transcribeThrowsOnNon200Response() throws Exception {
        HttpClient httpClient = mock(HttpClient.class);
        HttpResponse<String> response = mock(HttpResponse.class);
        when(response.statusCode()).thenReturn(400);
        when(response.body()).thenReturn("{\"error\":\"bad audio\"}");
        when(httpClient.<String>send(any(HttpRequest.class), any())).thenReturn(response);

        SpeechToTextService service = new SpeechToTextService(httpClient, "fake-key");

        IllegalStateException exception = assertThrows(IllegalStateException.class,
            () -> service.transcribe(SOME_AUDIO));
        assertTrue(exception.getMessage().contains("400"));
    }

    @Test
    void transcribeThrowsWhenApiKeyIsMissing() {
        SpeechToTextService service = new SpeechToTextService(mock(HttpClient.class), null);

        assertThrows(IllegalStateException.class, () -> service.transcribe(SOME_AUDIO));
    }

    @Test
    void transcribeThrowsWhenApiKeyIsBlank() {
        SpeechToTextService service = new SpeechToTextService(mock(HttpClient.class), "   ");

        assertThrows(IllegalStateException.class, () -> service.transcribe(SOME_AUDIO));
    }

    @Test
    void transcribeThrowsWhenAudioIsNull() {
        SpeechToTextService service = new SpeechToTextService(mock(HttpClient.class), "fake-key");

        assertThrows(IllegalStateException.class, () -> service.transcribe(null));
    }

    @Test
    void transcribeThrowsWhenAudioIsEmpty() {
        SpeechToTextService service = new SpeechToTextService(mock(HttpClient.class), "fake-key");

        assertThrows(IllegalStateException.class, () -> service.transcribe(new byte[0]));
    }

    @Test
    void buildMultipartBodyIncludesModelFieldAndAudioBytes() {
        byte[] body = SpeechToTextService.buildMultipartBody("boundary123", SOME_AUDIO);
        String bodyText = new String(body, StandardCharsets.UTF_8);

        assertTrue(bodyText.contains("--boundary123"));
        assertTrue(bodyText.contains("name=\"model\""));
        assertTrue(bodyText.contains("whisper-1"));
        assertTrue(bodyText.contains("name=\"file\"; filename=\"recording.wav\""));
        assertTrue(bodyText.contains("Content-Type: audio/wav"));
        assertTrue(bodyText.endsWith("--boundary123--\r\n"));
    }
}
