package org.slbtty.arcadianstopwatch

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
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
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        var timeTrack = TimeTrack(this.getPreferences(MODE_PRIVATE))

        setContent {
            ArcadianStopWatchTheme {
                Box(
                    modifier = Modifier.background(MaterialTheme.colorScheme.background)
                ) {
                    Greeting(
                        timeTrack = timeTrack, modifier = Modifier
                            .fillMaxSize()
                            .wrapContentSize(Alignment.Center)
                    )
                }

            }
        }

    }


}

@Composable
fun Greeting(timeTrack: TimeTrack, modifier: Modifier = Modifier) {

    var durationStr by remember { mutableStateOf("") }
    var paused by remember { mutableStateOf(false) }

    paused = timeTrack.paused

    fun updateTrackingStr() {
        timeTrack.updateStopInstant()
        durationStr = timeTrack.getTimeString()
    }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(
            text = durationStr,
            color = if (paused) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onBackground

        )
        Button(
            onClick = {
                timeTrack.statesReset()
                paused = false
            }) {
            Text(
                text = "Reset"
            )
        }

        Button(
            onClick = {
                timeTrack.togglePauseUnpause()
                paused = !paused

            }) {
            Text(
                text = if (paused) "Unpause" else "Pause",
            )
        }

        LaunchedEffect(true) {
            while (true) {
                updateTrackingStr()
                delay(1000)
            }

        }

    }


}
