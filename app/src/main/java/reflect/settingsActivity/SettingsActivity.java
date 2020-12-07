package reflect.settingsActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.reflect.manifests.R;

import java.util.Calendar;
import java.util.List;

import reflect.data.AlarmDataSource;
import reflect.data.AlarmItem;
import reflect.data.AlarmItemRepository;
import util.AppExecutors;

/**
 * Settings Activity
 * User can set/cancel alarms or notifications to be sent three times a day
 */
public class SettingsActivity extends AppCompatActivity {

    //variables used throughout this class
    private static Calendar calendar;
    private static Calendar afternoonCalendar;
    private static Calendar eveningCalendar;
    private TimePickerDialog.OnTimeSetListener onSetTime;
    private TextView morningTime;
    private TextView afternoonTime;
    private TextView eveningTime;
    private String timeOfDay;
    private AlarmItemRepository alarmItemRepository;
    private AlarmItem morningItem;
    private AlarmItem afternoonItem;
    private AlarmItem eveningItem;

    //when Activity is accessed
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        //new instance of all three calendars
        calendar = Calendar.getInstance();
        afternoonCalendar = Calendar.getInstance();
        eveningCalendar = Calendar.getInstance();
        //new instance of alarm repository
        alarmItemRepository = new AlarmItemRepository(new AppExecutors(),getApplicationContext());

        //declare all buttons, switches and text views
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


        //get morning alarmItem by id
        alarmItemRepository.getAlarmItem(1, new AlarmDataSource.GetAlarmItemCallback() {
            @Override
            public void onAlarmItemLoaded(AlarmItem item) {
                morningTime.setText(item.getTime());
                if(item.isSet()){
                    morningSwitch.setChecked(true);
                } else {
                    morningSwitch.setChecked(false);
                }

                morningItem = item;
            }

            @Override
            public void onDataNotAvailable() {

            }
        });

        //get afternoon alarmItem by id
        alarmItemRepository.getAlarmItem(2, new AlarmDataSource.GetAlarmItemCallback() {
            @Override
            public void onAlarmItemLoaded(AlarmItem item) {
                afternoonTime.setText(item.getTime());
                if(item.isSet()){
                    afternoonSwitch.setChecked(true);
                } else {
                    afternoonSwitch.setChecked(false);
                }

                afternoonItem = item;
            }

            @Override
            public void onDataNotAvailable() {

            }
        });

        //get evening alarmItem by id
        alarmItemRepository.getAlarmItem(3, new AlarmDataSource.GetAlarmItemCallback() {
            @Override
            public void onAlarmItemLoaded(AlarmItem item) {
                eveningTime.setText(item.getTime());
                if(item.isSet()){
                    eveningSwitch.setChecked(true);
                } else {
                    eveningSwitch.setChecked(false);
                }

                eveningItem = item;
            }

            @Override
            public void onDataNotAvailable() {

            }
        });

        /**
         * OnClick listener for "done" button
         * will set the alarms
         * will cancel alarms if user chose to
         * will end activity and return user to main activity
         */
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //instantiate alarm manager
                AlarmManager alarmManager;
                alarmManager = (AlarmManager) view.getContext().getSystemService(Context.ALARM_SERVICE);
                //set alarm for morning
                Intent dueDateNotificationIntent = new Intent(view.getContext(), AlarmReceiver.class);
                dueDateNotificationIntent.putExtra("time", "morning");
                PendingIntent dueDateIntent = PendingIntent.getBroadcast(view.getContext(), 0, dueDateNotificationIntent, 0);
                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, dueDateIntent);
                //set alarm for afternoon
                Intent afternoonIntent = new Intent(view.getContext(), AlarmReceiver.class);
                afternoonIntent.putExtra("time", "afternoon");
                PendingIntent afternoonPendingIntent = PendingIntent.getBroadcast(view.getContext(), 1, afternoonIntent, 0);
                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, afternoonCalendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, afternoonPendingIntent);
                //set alarm for evening
                Intent eveningIntent = new Intent(view.getContext(), AlarmReceiver.class);
                eveningIntent.putExtra("time", "evening");
                PendingIntent eveningPendingIntent = PendingIntent.getBroadcast(view.getContext(), 2, eveningIntent, 0);
                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, eveningCalendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, eveningPendingIntent);
                //if switch is off cancel alarm and save to db
                if(!morningSwitch.isChecked()){
                    dueDateIntent.cancel();
                    morningItem.setSet(false);
                    alarmItemRepository.saveAlarmItem(morningItem);
                }

                if(!afternoonSwitch.isChecked()){
                    afternoonPendingIntent.cancel();
                    afternoonItem.setSet(false);
                    alarmItemRepository.saveAlarmItem(afternoonItem);
                }

                if(!eveningSwitch.isChecked()){
                    eveningPendingIntent.cancel();
                    eveningItem.setSet(false);
                    alarmItemRepository.saveAlarmItem(eveningItem);
                }
                //end
                finish();

            }
        });

        /**
         * OnClick listener morning "set" button
         * will launch the time picker
         */
        setMorning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timeOfDay = "morning";
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
         * OnClick listener afternoon "set" button
         * will launch the time picker
         */
        setAfternoon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timeOfDay = "afternoon";
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
         * OnClick listener evening "set" button
         * will launch the time picker
         */

        setEvening.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timeOfDay = "evening";
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
         * will update alarmItem
         * will add "0" before the minute if the minute is < 10
         * sets switch to true
         * sets time text view
         */
        onSetTime = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, final int hour, final int minute) {
                final String sMinute;
                if(minute < 10){
                    sMinute = "0"+minute;
                } else {
                    sMinute = String.valueOf(minute);
                }
                Log.d("AddEditToDoItem", "onTimeSet: hh:mm " + hour + ":" + minute);
                switch(timeOfDay) {
                    case "morning":
                        calendar.setTimeInMillis(System.currentTimeMillis());
                        calendar.set(Calendar.MINUTE, minute);
                        calendar.set(Calendar.HOUR_OF_DAY, hour);
                        morningTime.setText(hour + ":" + sMinute);
                        morningSwitch.setChecked(true);
                        morningItem.setTime(hour + ":" + sMinute);
                        morningItem.setSet(true);
                        alarmItemRepository.saveAlarmItem(morningItem);
                        break;
                    case "afternoon":
                        System.out.println(afternoonCalendar.get(Calendar.DAY_OF_MONTH));
                        afternoonCalendar.setTimeInMillis(System.currentTimeMillis());
                        afternoonCalendar.set(Calendar.MINUTE, minute);
                        afternoonCalendar.set(Calendar.HOUR_OF_DAY, hour);
                        afternoonTime.setText(hour + ":" + sMinute);
                        afternoonSwitch.setChecked(true);
                        afternoonItem.setTime(hour + ":" + sMinute);
                        afternoonItem.setSet(true);
                        alarmItemRepository.saveAlarmItem(afternoonItem);
                        break;
                    case "evening":
                        System.out.println(eveningCalendar.get(Calendar.DAY_OF_MONTH));
                        eveningCalendar.setTimeInMillis(System.currentTimeMillis());
                        eveningCalendar.set(Calendar.MINUTE, minute);
                        eveningCalendar.set(Calendar.HOUR_OF_DAY, hour);
                        eveningTime.setText(hour + ":" + sMinute);
                        eveningSwitch.setChecked(true);
                        eveningItem.setTime(hour + ":" + sMinute);
                        eveningItem.setSet(true);
                        alarmItemRepository.saveAlarmItem(eveningItem);
                        break;
                    default:
                        System.out.println("not working");
                }

            }
        };

    }


}


