package org.slbtty.arcadianstopwatch

import java.time.Duration
import java.time.Instant


class TimeTrack() {
    private lateinit var startInstant: Instant
    private lateinit var tickingInstant: Instant
    private var paused = false

    private var pausedInstant: Instant? = null
    private lateinit var cumulativePauseDuration: Duration

    init {
        reset()
    }

    fun reset() {
        startInstant = Instant.now()
        tickingInstant = startInstant
        paused = false
        pausedInstant = null
        cumulativePauseDuration = Duration.ZERO
    }

    fun getTimeString(): String {
        val duration = Duration.between(startInstant, tickingInstant).minus(cumulativePauseDuration)

        return String.format(
            null, "%d:%02d:%02d", duration.toHours(), duration.toMinutesPart(), duration.toSecondsPart()
        )
    }

    fun togglePauseUnpause() {
        if (paused) {
            cumulativePauseDuration = cumulativePauseDuration.plus(Duration.between(pausedInstant, Instant.now()))
            pausedInstant = null
            paused = false
        } else {
            pausedInstant = Instant.now()
            paused = true
        }
    }


    fun updateStopInstant() {
        if (!paused) {
            tickingInstant = Instant.now()
        }
    }

    override fun toString(): String {
        return "$startInstant -> $tickingInstant"
    }

}

