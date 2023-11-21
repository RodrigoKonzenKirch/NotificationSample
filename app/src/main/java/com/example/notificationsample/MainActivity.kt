package com.example.notificationsample

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.graphics.drawable.toBitmap
import com.example.notificationsample.ui.theme.NotificationSampleTheme

class MainActivity : ComponentActivity() {

    val CHANNEL_ID = "defaultChannelID"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val bitmapImage = resources.getDrawable(R.drawable.abstract_wavy_green, applicationContext.theme).toBitmap()

        setContent {
            NotificationSampleTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    AppScreen(
                        "Try out sending different types of notifications:",
                        baseContext,
                        CHANNEL_ID,
                        bitmapImage
                    )
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        createNotificationChannel()
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
fun AppScreen(
    message: String,
    context: Context,
    channelId: String,
    notificationImageBitmap: Bitmap,
    modifier: Modifier = Modifier
) {

    val notificationIdBasic = 0
    val notificationIdImage = 1
    val notificationIdExpandable = 2
    val defaultPadding = 12.dp

    val basicNotificationBuilder = NotificationCompat.Builder(context, channelId)
        .setSmallIcon(R.drawable.ic_default_notification)
        .setContentTitle("Basic notification")
        .setContentText("Hi. You shouldn't miss this notification. It demonstrate a basic notification with title and text only!")
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)

    val largeImageNotificationBuilder = NotificationCompat.Builder(context, channelId)
        .setSmallIcon(R.drawable.ic_default_notification)
        .setContentTitle("Notification with image")
        .setContentText("This is a notification that shows text and an image as a thumbnail")
        .setLargeIcon(notificationImageBitmap)

    val largeExpandableImageNotificationBuilder = NotificationCompat.Builder(context, channelId)
        .setSmallIcon(R.drawable.ic_default_notification)
        .setContentTitle("Notification with image")
        .setContentText("This is a notification that shows text and an image as a thumbnail. When the notification is expanded, the image maximizes it's size")
        .setLargeIcon(notificationImageBitmap)
        .setStyle(NotificationCompat.BigPictureStyle()
            .bigPicture(notificationImageBitmap)
            .bigLargeIcon(null))

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = message,
            modifier = modifier.padding(defaultPadding)
        )

        Button(
            modifier = modifier.padding(defaultPadding),
            onClick = {
                with(NotificationManagerCompat.from(context)) {
                    if (ActivityCompat.checkSelfPermission(
                            context,
                            Manifest.permission.POST_NOTIFICATIONS
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {
                        notify(notificationIdBasic, basicNotificationBuilder.build())
                    }
                }

            }) {
            Text(text = "Send a basic notification")
        }

        Button(
            modifier = modifier.padding(defaultPadding),
            onClick = {
                with(NotificationManagerCompat.from(context)) {
                    if (ActivityCompat.checkSelfPermission(
                            context,
                            Manifest.permission.POST_NOTIFICATIONS
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {
                        notify(notificationIdImage, largeImageNotificationBuilder.build())
                    }
                }

            }) {
            Text(text = "Send image notification")
        }

        Button(
            modifier = modifier.padding(defaultPadding),
            onClick = {
                with(NotificationManagerCompat.from(context)) {
                    if (ActivityCompat.checkSelfPermission(
                            context,
                            Manifest.permission.POST_NOTIFICATIONS
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {
                        notify(notificationIdExpandable, largeExpandableImageNotificationBuilder.build())
                    }
                }

            }) {
            Text(text = "Send expandable image notification")
        }
    }
}
