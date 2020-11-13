package reflect.settingsActivity;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationBuilderWithBuilderAccessor;
import androidx.core.app.NotificationCompat;
import androidx.core.app.TaskStackBuilder;

import com.example.reflect.manifests.R;

import reflect.todolistactivity.MoodEntryListActivity;

public class AlarmReceiver extends BroadcastReceiver {

    String ALARM_CHANNEL_ID = "alarm_channel";
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onReceive(Context context, Intent intent){

        int requestId = 0;
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if(intent.hasExtra("RequestID")){
            requestId = intent.getIntExtra("RequestID", 0);
        }
        NotificationCompat.Builder alarm_builder = new NotificationCompat.Builder(context, ALARM_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle("ToDo Item due now!")
                .setContentText("The due date for item '" + intent.getStringExtra("title") + "' is " + intent.getStringExtra("dueDate"))
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setChannelId(ALARM_CHANNEL_ID);
        Intent resultIntent = new Intent(context, MoodEntryListActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(SettingsActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(requestId, PendingIntent.FLAG_UPDATE_CURRENT);
        alarm_builder.setContentIntent(resultPendingIntent);


        String channelId = ALARM_CHANNEL_ID;
        NotificationChannel channel = new NotificationChannel(
                channelId,
                "AlarmNotifications",
                NotificationManager.IMPORTANCE_HIGH);
        notificationManager.createNotificationChannel(channel);
        channel.enableVibration(true);

        notificationManager.notify(requestId,alarm_builder.build());
    }
    }

