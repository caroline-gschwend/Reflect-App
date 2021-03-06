package reflect.todolistactivity;

import java.util.List;

import reflect.data.MoodEntryItem;

/**
 * ToDoListContract
 * Two inner interfaces, a View and a Presenter for the ToDoListActivity
 */
public interface MoodEntryListContract {

    interface View {
        /**
         * setPresenter - sets the presenter associated with a View
         *
         * @param presenter - the ToDoListContract.presenter instance
         */
        void setPresenter(MoodEntryListContract.Presenter presenter);

        /**
         * showToDoItems - takes a list of toDoItems and populates a ListView
         *
         * @param moodEntryItemList - List of ToDoItems
         */
        void showToDoItems(List<MoodEntryItem> moodEntryItemList);

        /**
         * show settings activity
         */
        void showSettingsActivity();
        /**
         * showAddEditToDoItem - Creates an intent object to launch add or edit to do item activity
         *
         * @param item        - Item to be added/modified
         * @param requestCode - Integer code referencing whether a ToDoItem is being added or edited
         */
        void showAddEditToDoItem(MoodEntryItem item, int requestCode);
    }

    interface Presenter {
        /**
         * loadToDoItems - Loads all ToDoItems from the ToDoItemsRepository
         */
        void loadMoodEntryItems();

        /**
         * start -- All procedures that need to be started
         * Ideally, should be coupled with a stop if any running tasks need to be destroyed.
         */
        void start();

        /**
         * addNewToDoItem -- Create a new ToDoItem with stub values
         * Calls showAddEditToDoItem with created item and adding item integer
         */
        void addNewMoodEntryItem();

        /**
         * showExistingToDoItem -- Edit an existing toDoItem
         * Calls showAddEditToDoItem with existing item and editing item integer
         *
         * @param item - Item to be edited
         */
        void showExistingMoodEntryItem(MoodEntryItem item);

        /**
         * show the settings activity
         */
        void showSettingsActivity();
        /**
         * updateToDoItem -- Item to be updated in the dataRepository
         *
         * @param item -- ToDoItem to be updated in the ToDoItemRepository
         */
        void updateMoodEntryItem(MoodEntryItem item);

        void getMoodEntryItems();

        /**
         * result -- Passthrough from View
         * Takes the requestCode, resultCode, and the returned ToDoItem from a call to showAddEditToDoItem
         * on an OK result, and either creates or updates item in the repository
         *
         * @param requestCode -- Integer code identifying whether it was an update or edit call
         * @param resultCode  -- Integer code identifying the result from the Intent
         * @param item        -- ToDoItem returned from the AddEditToDoItemActivity
         */
        void result(int requestCode, int resultCode, MoodEntryItem item);
    }

}
