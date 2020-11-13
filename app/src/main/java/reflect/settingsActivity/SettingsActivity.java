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
import android.net.Uri;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.reflect.manifests.R;

import java.util.Calendar;

import reflect.todolistactivity.MoodEntryListActivity;

public class SettingsActivity extends AppCompatActivity {

    private static Calendar calendar;
    private TimePickerDialog.OnTimeSetListener onSetTime;
    private AlarmManager alarmMgr;
    private PendingIntent alarmIntent;
    private int alarmTime;
    private View thisView;
    private TextView morningTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        calendar = Calendar.getInstance();

        Button setMorning = findViewById(R.id.morningBtn);
        Button done = findViewById(R.id.doneBtn);
        morningTime = findViewById(R.id.morningTv);
        Switch morningSwitch = findViewById(R.id.mSwitch);


        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlarmManager alarmManager;
                alarmManager = (AlarmManager) view.getContext().getSystemService(Context.ALARM_SERVICE);
                Intent intent = new Intent(view.getContext(), AlarmReceiver.class);
                final int notificationId = 0;
                intent.putExtra("RequestID", notificationId);
                PendingIntent dueDateIntent = PendingIntent.getBroadcast(view.getContext(), 0, intent, 0);
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), dueDateIntent);
                finish();
            }
        });
        /**
         * Method for when date/time is to be set
         * will show the date and time pickers
         */
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

                calendar.set(Calendar.MINUTE, minute);
                calendar.set(Calendar.HOUR, hour);
                morningTime.setText(calendar.getTime().toString());
            }
        };

    }

}

