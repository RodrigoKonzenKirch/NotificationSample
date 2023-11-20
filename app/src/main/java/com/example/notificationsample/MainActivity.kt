package com.example.notificationsample

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.app.NotificationManager
import android.app.NotificationChannel
import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.notificationsample.ui.theme.NotificationSampleTheme

class MainActivity : ComponentActivity() {
    private val CHANNEL_ID = "defaultChannelID"
    private val NOTIFICATION_ID = 0


    private val builder = NotificationCompat.Builder(this, CHANNEL_ID)
        .setSmallIcon(R.drawable.ic_default_notification)
        .setContentTitle("This is an important notification")
        .setContentText("Hi. You shouldn't miss this notification. It's very important. I would never bother you without a good reason!")
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NotificationSampleTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    AppScreen("Notification is sent when app goes to the background")
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        createNotificationChannel()
    }

    override fun onPause() {
        val context = this
        with(NotificationManagerCompat.from(this)) {
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                notify(NOTIFICATION_ID, builder.build())
            }
        }
        super.onPause()
    }

    private fun createNotificationChannel() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val name = getString(R.string.default_channel_name)
            val descriptionText = getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}

@Composable
fun AppScreen(message: String, modifier: Modifier = Modifier) {
    Text(
            text = message,
            modifier = modifier
    )
}
