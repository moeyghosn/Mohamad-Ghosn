package com.example.data.audio

import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.math.sin

/**
 * Procedural Arabian Oud & Desert Night Ambiance Synthesizer
 * Generates relaxing oriental musical frequencies and campfire tones
 */
class AudioAmbianceManager {

    private var audioTrack: AudioTrack? = null
    private var isPlaying = false
    private var job: Job? = null
    private val scope = CoroutineScope(Dispatchers.Default)

    // Arabian Bayati / Rast Scale musical note frequencies (Hz)
    // D (293.66), E half-flat (311.13), F (349.23), G (392.0), A (440.0), Bb (466.16), C (523.25), D (587.33)
    private val maqamNotes = listOf(293.66, 329.63, 349.23, 392.00, 440.00, 493.88, 523.25, 587.33)

    fun isAmbiancePlaying(): Boolean = isPlaying

    fun toggleAmbiance(onStateChanged: (Boolean) -> Unit) {
        if (isPlaying) {
            stop()
            onStateChanged(false)
        } else {
            start()
            onStateChanged(true)
        }
    }

    fun start() {
        if (isPlaying) return
        isPlaying = true

        val sampleRate = 22050
        val bufferSize = AudioTrack.getMinBufferSize(
            sampleRate,
            AudioFormat.CHANNEL_OUT_MONO,
            AudioFormat.ENCODING_PCM_16BIT
        )

        try {
            audioTrack = AudioTrack.Builder()
                .setAudioAttributes(
                    AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .build()
                )
                .setAudioFormat(
                    AudioFormat.Builder()
                        .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                        .setSampleRate(sampleRate)
                        .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                        .build()
                )
                .setBufferSizeInBytes(bufferSize * 2)
                .setTransferMode(AudioTrack.MODE_STREAM)
                .build()

            audioTrack?.play()

            job = scope.launch {
                var noteIndex = 0
                val noteDurationMs = 800
                val numSamplesPerNote = (sampleRate * (noteDurationMs / 1000.0)).toInt()

                while (isActive && isPlaying) {
                    val targetFreq = maqamNotes[noteIndex % maqamNotes.size]
                    val shortBuffer = ShortArray(numSamplesPerNote)

                    for (i in 0 until numSamplesPerNote) {
                        val t = i.toDouble() / sampleRate
                        // Oud string harmonic envelope (pluck + decay)
                        val envelope = Math.exp(-3.5 * (i.toDouble() / numSamplesPerNote))
                        // Fundamental note + subtle warm second harmonic
                        val sampleValue = (sin(2.0 * Math.PI * targetFreq * t) * 0.7 +
                                sin(2.0 * Math.PI * (targetFreq * 2) * t) * 0.25 +
                                sin(2.0 * Math.PI * (targetFreq * 3) * t) * 0.05) * envelope

                        shortBuffer[i] = (sampleValue * Short.MAX_VALUE * 0.22).toInt().toShort()
                    }

                    audioTrack?.write(shortBuffer, 0, shortBuffer.size)

                    // Note progression in Arabian cadence
                    noteIndex = (noteIndex + (if (Math.random() > 0.4) 1 else -1 + maqamNotes.size)) % maqamNotes.size
                    delay(50)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            stop()
        }
    }

    fun stop() {
        isPlaying = false
        job?.cancel()
        job = null
        try {
            audioTrack?.pause()
            audioTrack?.flush()
            audioTrack?.release()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            audioTrack = null
        }
    }
}
