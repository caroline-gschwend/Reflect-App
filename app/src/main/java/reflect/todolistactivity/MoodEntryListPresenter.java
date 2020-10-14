package reflect.todolistactivity;

import android.app.Activity;
import android.util.Log;

import androidx.annotation.NonNull;
import reflect.data.MoodEntryItem;
import reflect.data.MoodEntryItemRepository;
import reflect.data.MoodEntryListDataSource;

import java.util.List;

/**
 * ToDoListPresenter -- Implements the Presenter interface from ToDoListContract Presenter
 */
public class MoodEntryListPresenter implements MoodEntryListContract.Presenter {

    //Data repository instance
    //Currently has a memory leak -- need to refactor context passing
    private static MoodEntryItemRepository mMoodEntryItemRepository;
    //View instance
    private final MoodEntryListContract.View mToDoItemView;

    // Integer request codes for creating or updating through the result method
    private static final int CREATE_TODO_REQUEST = 0;
    private static final int UPDATE_TODO_REQUEST = 1;

    /**
     * ToDoListPresenter constructor
     *
     * @param moodEntryItemRepository - Data repository instance
     * @param toDoItemView       - ToDoListContract.View instance
     */
    public MoodEntryListPresenter(@NonNull MoodEntryItemRepository moodEntryItemRepository, @NonNull MoodEntryListContract.View toDoItemView) {
        mMoodEntryItemRepository = moodEntryItemRepository;
        mToDoItemView = toDoItemView;
        //Make sure to pass the presenter into the view!
        mToDoItemView.setPresenter(this);
    }

    @Override
    public void start() {
        //Load all toDoItems
        loadToDoItems();
    }


    @Override
    public void addNewMoodEntryItem() {
        //Create stub ToDoItem with temporary data
        MoodEntryItem item = new MoodEntryItem();
        item.setTitle("Title");
        item.setContent("Content");
        item.setCompleted(false);
        item.setDueDate(0);
        item.setId(-1);
        //Show AddEditToDoItemActivity with a create request and temporary item
        mToDoItemView.showAddEditToDoItem(item, CREATE_TODO_REQUEST);
    }

    @Override
    public void showExistingMoodEntryItem(MoodEntryItem item) {
        //Show AddEditToDoItemActivity with a edit request, passing through an item
        Log.d("ToDoListPresenter", "TODO: Show Existing ToDoItem");
        mToDoItemView.showAddEditToDoItem(item, UPDATE_TODO_REQUEST);
    }

    @Override
    public void result(int requestCode, int resultCode, MoodEntryItem item) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == CREATE_TODO_REQUEST) {
                createToDoItem(item);
            } else if (requestCode == UPDATE_TODO_REQUEST) {
                updateMoodEntryItem(item);
            } else {
                Log.e("ToDoPresenter", "No such request!");
            }
        }
    }

    /**
     * Create ToDoItem in repository from ToDoItem and reload data
     *
     * @param item - item to be placed in the data repository
     */
    private void createToDoItem(MoodEntryItem item) {
        Log.d("ToDoListPresenter", "Create Item");
    }

    /**
     * Update ToDoItem in repository from ToDoItem and reload data
     *
     * @param item -- ToDoItem to be updated in the ToDoItemRepository
     */
    @Override
    public void updateMoodEntryItem(MoodEntryItem item) {
        Log.d("ToDoListPresenter", "TODO: Update Item");
    }

    /**
     * loadToDoItems -- loads all items from ToDoItemRepository
     * Two callbacks -- success/failure
     */
    @Override
    public void loadToDoItems() {
        Log.d("ToDoListPresenter", "Loading ToDoItems");
        mMoodEntryItemRepository.getToDoItems(new MoodEntryListDataSource.LoadToDoItemsCallback() {
            @Override
            public void onToDoItemsLoaded(List<MoodEntryItem> moodEntryItems) {
                Log.d("PRESENTER", "Loaded");
                ////////////////////////////////////
                // Remove the following lines
                // Just for demonstration
                ////////////////////////////////////
                if (moodEntryItems.size() == 0) {
                    MoodEntryItem temp = new MoodEntryItem();
                    temp.setId(-1);
                    temp.setDueDate(-1);
                    temp.setCompleted(false);
                    temp.setTitle("Temporary To-Do Item");
                    temp.setContent("Temporary Content");
                    moodEntryItems.add(temp);
                }
                mToDoItemView.showToDoItems(moodEntryItems);
            }

            @Override
            public void onDataNotAvailable() {
                Log.d("PRESENTER", "Not Loaded");
            }
        });
    }
}
