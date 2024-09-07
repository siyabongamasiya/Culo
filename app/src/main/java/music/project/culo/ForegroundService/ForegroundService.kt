package music.project.culo.ForegroundService

import android.app.Notification
import android.app.Notification.Action
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.core.app.NotificationCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import music.project.culo.Domain.Model.Song
import music.project.culo.Domain.Model.Songs
import music.project.culo.R
import music.project.culo.SongManager.SongManager
import music.project.culo.Utils.ChannelDetails
import music.project.culo.Utils.FOREGROUND_SERVICE_REQUEST_CODE
import music.project.culo.Utils.ForegroundIntentExtras
import music.project.culo.Utils.MusicActions
import music.project.culo.Utils.NEXT
import music.project.culo.Utils.NOTIFICATION_ID
import music.project.culo.Utils.PAUSE
import music.project.culo.Utils.PLAY
import music.project.culo.Utils.PREVIOUS
import music.project.culo.Utils.QUEDE_SONG_URL

class ForegroundService : Service() {
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        SongManager.createExoplayer(this)
        SongManager.setNotifier {song ->
            startForeground(NOTIFICATION_ID,createNotification(song))
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when(intent?.action){
            MusicActions.start.toString() -> {
                val scope = CoroutineScope(Dispatchers.Main)
                val initialSongUrl = intent.getStringExtra(ForegroundIntentExtras.ID.toString())
                val songs = intent.getParcelableExtra<Songs>(ForegroundIntentExtras.LIST.toString()) as Songs


                SongManager.setSongs(songs)
                startForeground(NOTIFICATION_ID,createNotification(SongManager.startSong(initialSongUrl!!)))
                scope.launch {
                    while (true){
                        delay(200)
                        if (SongManager.isPlaying()) {
                            SongManager.broadcastLatestCurrentTime()
                        }else{
                            createNotification(SongManager.getCurrentSong())
                        }
                    }
                }

            }

            MusicActions.pauseplay.toString() -> {
                try {
                    startForeground(NOTIFICATION_ID, createNotification(SongManager.playOrPause()))
                }catch (exception : Exception){
                    stopForeground(STOP_FOREGROUND_REMOVE)
                }
            }

            MusicActions.next.toString() -> {
                try {
                    startForeground(NOTIFICATION_ID, createNotification(SongManager.playNextSong()))
                }catch (exception : Exception){
                    stopForeground(STOP_FOREGROUND_REMOVE)
                }
            }

            MusicActions.previous.toString() -> {
                try {
                    startForeground(
                        NOTIFICATION_ID,
                        createNotification(SongManager.playPreiousSong())
                    )
                }catch (exception : Exception){
                    stopForeground(STOP_FOREGROUND_REMOVE)
                }
            }

            MusicActions.seekto.toString() -> {
                val seek = intent.getLongExtra(ForegroundIntentExtras.SEEK.toString(),0)
                startForeground(NOTIFICATION_ID,createNotification(SongManager.SeekTo(seek)))
            }

            MusicActions.queue.toString() -> {
                val url = intent.getStringExtra(QUEDE_SONG_URL.toString())
                if(url != null) {
                    SongManager.queue(url)
                }
            }

            MusicActions.shuffle.toString() -> {
                SongManager.shuffle()
            }
        }
        return START_STICKY
    }

    private fun createChannel(){
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O){
            val channel = NotificationChannel(ChannelDetails.CHANNEL_ID.string,
                ChannelDetails.CHANNEL_NAME.string,
                NotificationManager.IMPORTANCE_LOW)

            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun createNotification(song : Song) : Notification{
        createChannel()
        val previousIntent = Intent(this,ForegroundService :: class.java).also {intent ->
            intent.action = MusicActions.previous.toString()
        }

        val playPauseIntent = Intent(this,ForegroundService :: class.java).also {intent ->
            intent.action = MusicActions.pauseplay.toString()
        }

        val NextIntent = Intent(this,ForegroundService :: class.java).also {intent ->
            intent.action = MusicActions.next.toString()
        }


        val previousPendingIntent = PendingIntent.getForegroundService(this, FOREGROUND_SERVICE_REQUEST_CODE,
            previousIntent,PendingIntent.FLAG_IMMUTABLE)

        val playpausePendingIntent = PendingIntent.getForegroundService(this, FOREGROUND_SERVICE_REQUEST_CODE,
            playPauseIntent
            ,PendingIntent.FLAG_IMMUTABLE)

        val nextPendingIntent = PendingIntent.getForegroundService(this, FOREGROUND_SERVICE_REQUEST_CODE,
            NextIntent,PendingIntent.FLAG_IMMUTABLE)


        if(SongManager.isPlaying()){
            val builder = NotificationCompat
                .Builder(this,ChannelDetails.CHANNEL_ID.string)
                .setSmallIcon(R.drawable.logoimage)
                .setContentTitle(song.title)
                .setContentText(song.artist)
                .setAutoCancel(false)
                .addAction(R.drawable.baseline_arrow_back_ios_new_24, PREVIOUS,previousPendingIntent)
                .addAction(R.drawable.baseline_play_arrow_24, PAUSE,playpausePendingIntent)
                .addAction(R.drawable.baseline_arrow_forward_ios_24, NEXT,nextPendingIntent)


            return builder.build()
        }else{
            val builder = NotificationCompat
                .Builder(this,ChannelDetails.CHANNEL_ID.string)
                .setSmallIcon(R.drawable.logoimage)
                .setContentTitle(song.title)
                .setContentText(song.artist)
                .setAutoCancel(false)
                .addAction(R.drawable.baseline_arrow_back_ios_new_24, PREVIOUS,previousPendingIntent)
                .addAction(R.drawable.baseline_play_arrow_24, PLAY,playpausePendingIntent)
                .addAction(R.drawable.baseline_arrow_forward_ios_24, NEXT,nextPendingIntent)


            return builder.build()
        }
    }
}