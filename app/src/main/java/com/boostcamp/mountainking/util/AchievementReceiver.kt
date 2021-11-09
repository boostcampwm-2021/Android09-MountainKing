package com.boostcamp.mountainking.util

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class AchievementReceiver : BroadcastReceiver() {
    companion object {
        private const val CHANNEL_ID = "AchievementComplete"
    }

    override fun onReceive(context: Context, intent: Intent?) {
        val achievementName = intent?.getStringExtra("achievementName") ?: "Error"
        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(com.boostcamp.mountainking.R.drawable.ic_achievement_svgrepo_com)
            .setContentTitle("업적 달성")
            .setContentText("$achievementName 업적을 달성했습니다.")
            .setPriority(NotificationCompat.PRIORITY_MAX)
        NotificationManagerCompat.from(context)
            .notify(System.currentTimeMillis().toInt(), builder.build())
    }

    fun notifyAchievementComplete(context: Context, achievementName: String){
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