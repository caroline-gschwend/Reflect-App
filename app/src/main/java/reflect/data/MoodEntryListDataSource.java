package reflect.data;

import androidx.annotation.NonNull;

import java.util.List;

/**
 * Interface for any implementation of a ToDoListDataSource
 * (Currently only have one - a local ContentProvider based implementation (@ToDoItemRepository)
 */
public interface MoodEntryListDataSource {

    /**
     * LoadToDoItemsCallback interface
     * Example of how to implement callback functions depending on the result of functions in interfaces
     * Currently, onDataNotAvailable is not implemented
     */
    interface LoadToDoItemsCallback {

        void onToDoItemsLoaded(List<MoodEntryItem> moodEntryItems);

        void onDataNotAvailable();
    }

    /**
     * GetToDoItemsCallback interface
     * Not currently implementd
     */
    interface GetToDoItemCallback {

        void onToDoItemLoaded(MoodEntryItem task);

        void onDataNotAvailable();
    }

    /**
     * getToDoItems loads all ToDoItems, calls either success or failure fuction above
     *
     * @param callback - Callback function
     */
    void getToDoItems(@NonNull LoadToDoItemsCallback callback);

    /**
     * getToDoItem - Get a single ToDoItem - currently not implemented
     *
     * @param toDoItemId - String of the current ItemID to be retrieved
     * @param callback   - Callback function
     */
    void getToDoItem(@NonNull int toDoItemId, @NonNull GetToDoItemCallback callback);

    /**
     * SaveToDoItem saves a toDoItem to the database - No callback (should be implemented for
     * remote databases)
     *
     * @param moodEntryItem
     */
    void saveToDoItem(@NonNull final MoodEntryItem moodEntryItem);

    /**
     * CreateToDoItem adds a toDoItem to the database - No callback (should be implemented for
     * remote databases)
     *
     * @param moodEntryItem
     */
    void createToDoItem(@NonNull MoodEntryItem moodEntryItem);

}
