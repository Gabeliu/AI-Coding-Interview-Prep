package com.aicodinginterviewprep;

import org.junit.jupiter.api.Test;

import javax.sound.sampled.TargetDataLine;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class MicrophoneRecorderTest {

    @Test
    void isRecording_falseBeforeStart() {
        MicrophoneRecorder recorder = new MicrophoneRecorder();

        assertFalse(recorder.isRecording());
    }

    @Test
    void startRecording_setsIsRecordingTrue() throws Exception {
        TargetDataLine line = fakeLineProducingBytes(1);
        MicrophoneRecorder recorder = new MicrophoneRecorder();

        recorder.startRecording(line);

        assertTrue(recorder.isRecording());
        recorder.stopRecording();
    }

    @Test
    void startRecording_whenAlreadyRecording_throws() throws Exception {
        TargetDataLine line = fakeLineProducingBytes(1);
        MicrophoneRecorder recorder = new MicrophoneRecorder();
        recorder.startRecording(line);

        assertThrows(IllegalStateException.class, () -> recorder.startRecording(line));

        recorder.stopRecording();
    }

    @Test
    void stopRecording_whenNotRecording_throws() {
        MicrophoneRecorder recorder = new MicrophoneRecorder();

        assertThrows(IllegalStateException.class, recorder::stopRecording);
    }

    @Test
    void stopRecording_setsIsRecordingFalse() throws Exception {
        TargetDataLine line = fakeLineProducingBytes(1);
        MicrophoneRecorder recorder = new MicrophoneRecorder();
        recorder.startRecording(line);

        recorder.stopRecording();

        assertFalse(recorder.isRecording());
    }

    @Test
    void stopRecording_returnsValidWavBytes() throws Exception {
        CountDownLatch capturedEnough = new CountDownLatch(1);
        TargetDataLine line = fakeLineProducingBytes(3, capturedEnough);
        MicrophoneRecorder recorder = new MicrophoneRecorder();

        recorder.startRecording(line);
        assertTrue(capturedEnough.await(2, TimeUnit.SECONDS), "expected at least 3 reads before stopping");

        byte[] wav = recorder.stopRecording();

        assertTrue(wav.length > 44, "WAV output should be larger than the 44-byte header");
        assertEquals("RIFF", new String(wav, 0, 4, StandardCharsets.US_ASCII));
        assertEquals("WAVE", new String(wav, 8, 4, StandardCharsets.US_ASCII));
    }

    private TargetDataLine fakeLineProducingBytes(int minCalls) {
        return fakeLineProducingBytes(minCalls, new CountDownLatch(1));
    }

    private TargetDataLine fakeLineProducingBytes(int minCalls, CountDownLatch capturedEnough) {
        TargetDataLine line = mock(TargetDataLine.class);
        AtomicInteger callCount = new AtomicInteger();

        when(line.read(any(byte[].class), eq(0), anyInt())).thenAnswer(invocation -> {
            byte[] buffer = invocation.getArgument(0);
            int requestedLength = invocation.getArgument(2);
            int filled = Math.min(requestedLength, 100);
            for (int i = 0; i < filled; i++) {
                buffer[i] = 1;
            }
            if (callCount.incrementAndGet() >= minCalls) {
                capturedEnough.countDown();
            }
            return filled;
        });

        return line;
    }
}
