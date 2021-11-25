package com.boostcamp.mountainking.util

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.os.bundleOf
import com.boostcamp.mountainking.MainActivity

class AchievementReceiver : BroadcastReceiver() {
    companion object {
        private const val CHANNEL_ID = "AchievementComplete"
    }

    override fun onReceive(context: Context, intent: Intent?) {
        val achievementName = intent?.getStringExtra("achievementName") ?: return
        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(com.boostcamp.mountainking.R.drawable.ic_notification)
            .setContentTitle("업적 달성")
            .setContentText("$achievementName 업적을 달성했습니다.")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(
                PendingIntent.getActivity(
                    context,
                    achievementName.hashCode(),
                    Intent(context, MainActivity::class.java).apply {
                        putExtra("fragment", "achievement")
                    },
                    PendingIntent.FLAG_IMMUTABLE
                )
            )
            .setAutoCancel(true)
        NotificationManagerCompat.from(context)
            .notify(System.currentTimeMillis().toInt(), builder.build())
    }

    fun notifyAchievementComplete(context: Context, achievementName: String) {
        val receiverIntent = Intent(context, AchievementReceiver::class.java).apply {
            putExtra("achievementName", achievementName)
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            achievementName.hashCode(),
            receiverIntent,
            PendingIntent.FLAG_IMMUTABLE
        )
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.set(AlarmManager.RTC, System.currentTimeMillis(), pendingIntent)
    }
}
