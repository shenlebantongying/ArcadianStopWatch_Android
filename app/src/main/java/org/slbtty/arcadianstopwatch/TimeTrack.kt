package org.slbtty.arcadianstopwatch


import java.time.Duration
import java.time.Instant

import android.content.SharedPreferences
import androidx.core.content.edit

class TimeTrack(val sharedPreferences: SharedPreferences) {

    // Persistent States
    private lateinit var startInstant: Instant
    var paused = false
    private var pausedInstant: Instant? = null
    private lateinit var cumulativePauseDuration: Duration
    private lateinit var tickingInstant: Instant

    init {
        statesRecover()
    }

    fun statesReset() {
        startInstant = Instant.now()
        tickingInstant = startInstant
        paused = false
        pausedInstant = null
        cumulativePauseDuration = Duration.ZERO
        statesSave()
    }

    fun statesSave() {
        sharedPreferences.edit {
            putBoolean(::paused.name, paused)
            putString(::startInstant.name, startInstant.toString())
            putString(::pausedInstant.name, pausedInstant?.toString())
            putString(::tickingInstant.name, tickingInstant.toString())
            putString(::cumulativePauseDuration.name, cumulativePauseDuration.toString())
        }
    }

    fun statesRecover() {
        paused = sharedPreferences.getBoolean(::paused.name, false)

        val strStartInstant: String? = sharedPreferences.getString(::startInstant.name, "")
        val strPausedInstant: String? = sharedPreferences.getString(::pausedInstant.name, "")
        val strTickingInstant: String? = sharedPreferences.getString(::tickingInstant.name, "")
        val strCumulativePausedDuration: String? = sharedPreferences.getString(::cumulativePauseDuration.name, "")

        if (strStartInstant.isNullOrEmpty() || strPausedInstant.isNullOrEmpty() || strCumulativePausedDuration.isNullOrEmpty() || strTickingInstant.isNullOrEmpty()) {
            statesReset()
        } else {
            startInstant = Instant.parse(strStartInstant)
            pausedInstant = Instant.parse(strPausedInstant)
            tickingInstant = Instant.parse(strTickingInstant)
            cumulativePauseDuration = Duration.parse(strCumulativePausedDuration)
        }
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

        statesSave()
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

