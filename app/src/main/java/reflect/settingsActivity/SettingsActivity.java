package reflect.settingsActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.reflect.manifests.R;

import java.util.Calendar;

import reflect.todolistactivity.MoodEntryListActivity;

public class SettingsActivity extends AppCompatActivity {


    private static Calendar calendar;
    private DatePickerDialog.OnDateSetListener onSetDate;
    private TimePickerDialog.OnTimeSetListener onSetTime;
    private TextView morningTime;
    private TextView afternoonTime;
    private TextView eveningTime;
    private String timeOfDay;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        calendar = Calendar.getInstance();

        final Button setMorning = findViewById(R.id.morningBtn);
        final Button setAfternoon = findViewById(R.id.afternoonBtn);
        final Button setEvening = findViewById(R.id.eveningBtn);
        Button done = findViewById(R.id.doneBtn);
        morningTime = findViewById(R.id.morningTv);
        final Switch morningSwitch = findViewById(R.id.mSwitch);
        afternoonTime = findViewById(R.id.afternoonTv);
        final Switch afternoonSwitch = findViewById(R.id.aSwitch);
        eveningTime = findViewById(R.id.eveningTv);
        final Switch eveningSwitch = findViewById(R.id.nSwitch);

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlarmManager alarmManager;
                alarmManager = (AlarmManager) view.getContext().getSystemService(Context.ALARM_SERVICE);
                System.out.println(calendar.getTime());
                Intent dueDateNotificationIntent = new Intent(view.getContext(), AlarmReceiver.class);
                dueDateNotificationIntent.putExtra("time", "morning");
                PendingIntent dueDateIntent = PendingIntent.getBroadcast(view.getContext(), 0, dueDateNotificationIntent, 0);
                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, dueDateIntent);
                

                //end
                finish();

            }
        });

        setMorning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                int minute = calendar.get(Calendar.MINUTE);
                int hour = calendar.get(Calendar.HOUR);

                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        SettingsActivity.this,
                        onSetTime,
                        minute, hour, DateFormat.is24HourFormat(SettingsActivity.this));
                timePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
                timePickerDialog.show();
            }
        });

        /**
         * Method to set the time
         * will set the time from the hour and minute of the time picker
         */
        onSetTime = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minute) {

                Log.d("AddEditToDoItem", "onTimeSet: hh:mm " + hour + ":" + minute);

                System.out.println(calendar.get(Calendar.DAY_OF_MONTH));
                calendar.set(Calendar.MINUTE, minute);
                calendar.set(Calendar.HOUR, hour);
                calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH));
                morningTime.setText(hour + ":" + minute);
            }
        };

    }

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_settings);
//        calendar = Calendar.getInstance();
//
//        final Button setMorning = findViewById(R.id.morningBtn);
//        final Button setAfternoon = findViewById(R.id.afternoonBtn);
//        final Button setEvening = findViewById(R.id.eveningBtn);
//        Button done = findViewById(R.id.doneBtn);
//        morningTime = findViewById(R.id.morningTv);
//        final Switch morningSwitch = findViewById(R.id.mSwitch);
//        afternoonTime = findViewById(R.id.afternoonTv);
//        final Switch afternoonSwitch = findViewById(R.id.aSwitch);
//        eveningTime = findViewById(R.id.eveningTv);
//        final Switch eveningSwitch = findViewById(R.id.nSwitch);
//
//
//
//        done.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                AlarmManager alarmManager = (AlarmManager) view.getContext().getSystemService(Context.ALARM_SERVICE);
//                //create alarm and notification
//                Intent dueDateNotificationIntent = new Intent(view.getContext(), AlarmReceiver.class);
//                dueDateNotificationIntent.putExtra("time", "morning");
//                PendingIntent dueDateIntent = PendingIntent.getBroadcast(view.getContext(), 0, dueDateNotificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//                System.out.println(calendar.getTime());
//                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 0, dueDateIntent);
//                finish();
//            }
//
//
//        });
//
//        /**
//         * Method for when morning time is to be set
//         * will show the date and time pickers
//         */
//        setMorning.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Calendar calendar = Calendar.getInstance();
//                int minute = calendar.get(Calendar.MINUTE);
//                int hour = calendar.get(Calendar.HOUR);
//
//                TimePickerDialog timePickerDialog = new TimePickerDialog(
//                        SettingsActivity.this,
//                        onSetTime,
//                        minute, hour, DateFormat.is24HourFormat(SettingsActivity.this));
//                timePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
//                timePickerDialog.show();
//
//            }
//        });
//
//
//        /**
//         * Method to set the time
//         * will set the time from the hour and minute of the time picker
//         */
//        onSetTime = new TimePickerDialog.OnTimeSetListener() {
//            @Override
//            public void onTimeSet(TimePicker timePicker, int hour, int minute) {
//
//                Log.d("AddEditToDoItem", "onTimeSet: hh:mm " + hour + ":" + minute);
//
//                    calendar.set(Calendar.MINUTE, minute);
//                    calendar.set(Calendar.HOUR, hour);
//                    calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR));
//                    calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH));
//                    calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) - 1);
//                    morningTime.setText(hour + ":" + minute);
//                    morningSwitch.setChecked(true);
//
//
//            }
//        };
//
//    }
//
//    public void cancelAlarmIfExists(Context mContext,int requestCode,Intent intent){
//        try {
//            PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext, requestCode, intent,0);
//            AlarmManager am=(AlarmManager)mContext.getSystemService(Context.ALARM_SERVICE);
//            am.cancel(pendingIntent);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
}


