package org.slbtty.arcadianstopwatch

import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.keepScreenOn
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalView
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
                Layering(timeTrack)
            }
        }
    }

}

@Composable
fun Layering(timeTrack: TimeTrack) {
    val localConfiguration = LocalConfiguration.current
    val localActivity = LocalActivity.current
    val localeView = LocalView.current
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .background(MaterialTheme.colorScheme.surface)
            .safeContentPadding()
            .fillMaxSize()
            .keepScreenOn()
            .pointerInput(Unit) {
                detectTapGestures(
                    onDoubleTap = { offset ->
                        localActivity?.requestedOrientation =
                            if (localConfiguration.orientation == Configuration.ORIENTATION_PORTRAIT) {
                                val turnLeft: Boolean = offset.x < (localeView.width / 2)
                                if (turnLeft) {
                                    ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                                } else {
                                    ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE
                                }

                            } else {
                                ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                            }

                    })
            },
    ) {
        TheText(
            timeTrack = timeTrack
        )
    }
}


@Composable
fun TheText(timeTrack: TimeTrack) {

    var durationStr by remember { mutableStateOf(timeTrack.getTimeString()) }
    var paused by remember { mutableStateOf(false) }

    paused = timeTrack.paused

    fun updateTrackingStr() {
        timeTrack.updateStopInstant()
        durationStr = timeTrack.getTimeString()
    }

    Text(
        text = durationStr,
        color = if (paused) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface,
        autoSize = TextAutoSize.StepBased(maxFontSize = TextAutoSizeDefaults.MaxFontSize * 1.4),
        maxLines = 1,
        modifier = Modifier.combinedClickable(
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

