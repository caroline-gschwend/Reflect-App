package reflect.todolistactivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;

import static android.hardware.biometrics.BiometricManager.Authenticators.BIOMETRIC_STRONG;
import static android.hardware.biometrics.BiometricManager.Authenticators.DEVICE_CREDENTIAL;
import static com.google.common.base.Preconditions.checkNotNull;

import android.content.Intent;
import 	androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.reflect.manifests.R;

import java.util.concurrent.Executor;

import reflect.data.AlarmDataSource;
import reflect.data.AlarmItem;
import reflect.data.AlarmItemRepository;
import reflect.data.MoodEntryItemRepository;
import util.AppExecutors;

/**
 * ToDoListActivity - Main Activity for the Application
 */
public class MoodEntryListActivity extends AppCompatActivity {

    //local instance of the toDoListPresenter, passed through into the toDoListFragment
    private MoodEntryListPresenter mMoodEntryListPresenter;

    private Executor executor;
    private BiometricPrompt biometricPrompt;
    private BiometricPrompt.PromptInfo promptInfo;
    private AlarmItemRepository alarmItemRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Set view to use the main activity layout - a content frame that holds a single fragment
        setContentView(R.layout.activity_main);

        //new instance of alarm repo
        alarmItemRepository = new AlarmItemRepository(new AppExecutors(),getApplicationContext());
        executor = ContextCompat.getMainExecutor(this);
        //will show prompt only if there is a fingerprint created for device
        //will use toast messages to communicate with the user if there is an issue or success
        biometricPrompt = new BiometricPrompt(MoodEntryListActivity.this,
                executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode,
                                              @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                Toast.makeText(getApplicationContext(),
                        "Authentication error: " + errString, Toast.LENGTH_SHORT)
                        .show();
            }

            @Override
            public void onAuthenticationSucceeded(
                    @NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                Toast.makeText(getApplicationContext(),
                        "Authentication succeeded!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                Toast.makeText(getApplicationContext(), "Authentication failed",
                        Toast.LENGTH_SHORT)
                        .show();
            }
        });

        //this is what is displayed on the prompt
        promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Welcome to Reflect")
                .setSubtitle("Log in using your fingerprint")
                .setNegativeButtonText("login with passcode")
                .build();

        //upon opening the app the user will be prompted to login with their finger print
        biometricPrompt.authenticate(promptInfo);

        //ToDoListFragment -- Main view for the ToDoListActivity
        MoodEntryListFragment moodEntryListFragment =
                (MoodEntryListFragment) getSupportFragmentManager().findFragmentById(R.id.toDoListFragmentFrame);
        if (moodEntryListFragment == null) {
            // Create the fragment
            moodEntryListFragment = MoodEntryListFragment.newInstance();
            // Check that it is not null
            checkNotNull(moodEntryListFragment);
            // Populate the fragment into the activity
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.toDoListFragmentFrame, moodEntryListFragment);
            transaction.commit();
        }

        //Get an instance of the ToDoListPresenter
        //Parameters - ToDoListRepository - Instance of the toDoListRepository
        //toDoListFragment - the View to be communicated to by the presenter
        // ToDoListRepository needs a thread pool to execute database/network calls in other threads
        // ToDoListRepository needs the application context to be able to make calls to the ContentProvider
        mMoodEntryListPresenter = new MoodEntryListPresenter(MoodEntryItemRepository.getInstance(new AppExecutors(), getApplicationContext()), moodEntryListFragment);


        //create alarm types when app first loads
        alarmItemRepository.getAlarmItem(1, new AlarmDataSource.GetAlarmItemCallback() {
            @Override
            public void onAlarmItemLoaded(AlarmItem item) {
               Log.d("Main Activity: ", "Alarm items already in the database");
                if(item.getId() == null) {
                    //create morningAlarmItem
                    AlarmItem morningAlarm = new AlarmItem();
                    morningAlarm.setType("morning");
                    morningAlarm.setTime("");
                    morningAlarm.setSet(false);
                    alarmItemRepository.createAlarmItem(morningAlarm);

                    //create afternoonAlarmItem
                    AlarmItem afternoonAlarm = new AlarmItem();
                    afternoonAlarm.setType("afternoon");
                    afternoonAlarm.setTime("");
                    afternoonAlarm.setSet(false);
                    alarmItemRepository.createAlarmItem(afternoonAlarm);

                    //create eveningAlarmItem
                    AlarmItem eveningAlarm = new AlarmItem();
                    eveningAlarm.setType("evening");
                    eveningAlarm.setTime("");
                    eveningAlarm.setSet(false);
                    alarmItemRepository.createAlarmItem(eveningAlarm);
                }
            }

            @Override
            public void onDataNotAvailable() {
                //create morningAlarmItem
                AlarmItem morningAlarm = new AlarmItem();
                morningAlarm.setType("morning");
                morningAlarm.setTime("");
                morningAlarm.setSet(false);
                alarmItemRepository.createAlarmItem(morningAlarm);

                //create afternoonAlarmItem
                AlarmItem afternoonAlarm = new AlarmItem();
                afternoonAlarm.setType("afternoon");
                afternoonAlarm.setTime("");
                afternoonAlarm.setSet(false);
                alarmItemRepository.createAlarmItem(afternoonAlarm);

                //create eveningAlarmItem
                AlarmItem eveningAlarm = new AlarmItem();
                eveningAlarm.setType("evening");
                eveningAlarm.setTime("");
                eveningAlarm.setSet(false);
                alarmItemRepository.createAlarmItem(eveningAlarm);

            }
        });

    }
}
