package reflect.todolistactivity;

import androidx.appcompat.app.AppCompatActivity;
import reflect.data.MoodEntryItem;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.reflect.manifests.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Calendar;
import java.util.Date;

public class AddViewMoodEntryItemActivity extends AppCompatActivity {

    int id = -1;
    private static final int UPDATE_TODO_REQUEST = 1;
    int color = 0;
    String moodText = "";
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = getIntent();
        setResult(Activity.RESULT_CANCELED, intent);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_view_mood_entry_item);
        //change the title to a drop down of moods/colors
        final TextView tvColor = findViewById(R.id.tvItemColor);
        tvColor.setClickable(false);

        Spinner spinner = findViewById(R.id.spMoodPicker);
        final EditText moodDescription = findViewById(R.id.etMoodDescription);
        Button saveMood = findViewById(R.id.btnSaveMoodItem);

        final Intent intent = getIntent();
        if(intent.hasExtra("RequestCode")) {
            if(intent.getIntExtra("RequestCode", -1)==UPDATE_TODO_REQUEST) {
                Log.i("we have extra", "extra");
                MoodEntryItem item = (MoodEntryItem)intent.getSerializableExtra("MoodEntryItem");
                id = item.getId();
                tvColor.setBackgroundResource(item.getColor());
                moodText = item.getMood();
                tvColor.setText(moodText);
                moodDescription.setText(item.getContent());

            }
        }
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.spinnerChoices, android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        if(!moodText.isEmpty()) {
            int pos = adapter.getPosition(moodText);
            spinner.setSelection(pos);
        }

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String mood = adapterView.getItemAtPosition(i).toString();
                tvColor.setText(mood);
                moodText = mood;
                color = getColorForMood(mood);
                tvColor.setBackgroundResource(color);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //save to db
        saveMood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MoodEntryItem item = new MoodEntryItem();
                item.setContent(moodDescription.getText().toString());
                item.setColor(color);
                item.setMood(moodText);
                 if(id != -1) item.setId(id);
                intent.putExtra("MoodEntryItem", item);
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });
    }
    private int getColorForMood(String mood) {
        Resources res = getResources();
        String[] darkPinkMoods = res.getStringArray(R.array.darkPinkMoods);
        String[] lightPinkMoods = res.getStringArray(R.array.lightPinkMoods);
        String[] darkBlueMoods = res.getStringArray(R.array.darkBlueMoods);
        String[] lightBlueMoods = res.getStringArray(R.array.lightBlueMoods);
        String[] darkYellowMoods = res.getStringArray(R.array.darkYellowMoods);
        String[] lightYellowMoods = res.getStringArray(R.array.lightYellowMoods);
        String[] darkGreenMoods = res.getStringArray(R.array.darkGreenMoods);
        String[] lightGreenMoods = res.getStringArray(R.array.lightGreenMoods);

        for(String dpm: darkPinkMoods)
            if(mood.equals(dpm)) return R.color.darkPink;
        for(String lpm: lightPinkMoods)
            if(mood.equals(lpm)) return R.color.lightPink;
        for(String dbm: darkBlueMoods)
            if(mood.equals(dbm)) return R.color.darkBlue;
        for(String lbm: lightBlueMoods)
            if(mood.equals(lbm)) return R.color.lightBlue;
        for(String dym: darkYellowMoods)
            if(mood.equals(dym)) return R.color.darkYellow;
        for(String lym: lightYellowMoods)
            if(mood.equals(lym)) return R.color.lightYellow;
        for(String dgm: darkGreenMoods)
            if(mood.equals(dgm)) return R.color.darkGreen;
        for(String lgm: lightGreenMoods)
            if(mood.equals(lgm)) return R.color.lightGreen;
        return R.color.darkBlue;
    }
}