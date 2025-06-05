package org.slbtty.arcadianstopwatch

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.text.TextAutoSize
import androidx.compose.foundation.text.TextAutoSizeDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {

    lateinit var timeTrack: TimeTrack

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val preference = this.getPreferences(MODE_PRIVATE)
        timeTrack = TimeTrack(preference)

        setContent {
            ArcadianStopWatchTheme {
                Column(
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.background)
                        .safeContentPadding()
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Greeting(
                        timeTrack = timeTrack
                    )
                }

            }
        }
    }

}


@Composable
fun Greeting(timeTrack: TimeTrack) {

    var durationStr by remember { mutableStateOf(timeTrack.getTimeString()) }
    var paused by remember { mutableStateOf(false) }

    paused = timeTrack.paused

    fun updateTrackingStr() {
        timeTrack.updateStopInstant()
        durationStr = timeTrack.getTimeString()
    }

    Text(
        text = durationStr,
        color = if (paused) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onBackground,
        autoSize = TextAutoSize.StepBased(maxFontSize = TextAutoSizeDefaults.MaxFontSize * 1.5),
        maxLines = 1,
        modifier = Modifier
            .combinedClickable(
                onClick = {
                    timeTrack.togglePauseUnpause()
                    paused = !paused
                },
                onLongClick = {
                    timeTrack.statesReset()
                    paused = false
                },
            )

    )

    LaunchedEffect(true) {
        while (true) {
            updateTrackingStr()
            delay(1000)
        }

    }
}

