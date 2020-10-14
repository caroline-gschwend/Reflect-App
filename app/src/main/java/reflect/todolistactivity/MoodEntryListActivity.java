package reflect.todolistactivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import static com.google.common.base.Preconditions.checkNotNull;

import android.os.Bundle;

import com.example.reflect.manifests.R;

import reflect.data.MoodEntryItemRepository;
import util.AppExecutors;

/**
 * ToDoListActivity - Main Activity for the Application
 */
public class MoodEntryListActivity extends AppCompatActivity {

    //local instance of the toDoListPresenter, passed through into the toDoListFragment
    private MoodEntryListPresenter mMoodEntryListPresenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Set view to use the main activity layout - a content frame that holds a single fragment
        setContentView(R.layout.activity_main);
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

    }
}