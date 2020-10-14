package reflect.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import util.AppExecutors;

/**
 * ToDoItemRepository class - implements the ToDoDataSource interface
 */
public class MoodEntryItemRepository implements MoodEntryListDataSource {

    //Memory leak here by including the context - Fix this at some point
    private static volatile MoodEntryItemRepository INSTANCE;

    //Thread pool for execution on other threads
    private AppExecutors mAppExecutors;
    //Context for calling ToDoProvider
    private Context mContext;

    /**
     * private constructor - prevent direct instantiation
     *
     * @param appExecutors - thread pool
     * @param context
     */
    private MoodEntryItemRepository(@NonNull AppExecutors appExecutors, @NonNull Context context) {
        mAppExecutors = appExecutors;
        mContext = context;
    }

    /**
     * public constructor - prevent creation of instance if one already exists
     *
     * @param appExecutors
     * @param context
     * @return
     */
    public static MoodEntryItemRepository getInstance(@NonNull AppExecutors appExecutors, @NonNull Context context) {
        if (INSTANCE == null) {
            synchronized (MoodEntryItemRepository.class) {
                if (INSTANCE == null) {
                    INSTANCE = new MoodEntryItemRepository(appExecutors, context);
                }
            }
        }
        return INSTANCE;
    }

    /**
     * getToDoItems runs query in a separate thread, and on success loads data from cursor into a list
     *
     * @param callback
     */
    @Override
    public void getToDoItems(@NonNull final LoadToDoItemsCallback callback) {
        Log.d("REPOSITORY", "Loading...");
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                String[] projection = {
                        MoodEntryItem.TODOITEM_ID,
                        MoodEntryItem.TODOITEM_TITLE,
                        MoodEntryItem.TODOITEM_CONTENT,
                        MoodEntryItem.TODOITEM_DUEDATE,
                        MoodEntryItem.TODOITEM_COMPLETED};
                final Cursor c = mContext.getContentResolver().query(Uri.parse("content://" + MoodEntryProvider.AUTHORITY + "/" + MoodEntryProvider.TODOITEM_TABLE_NAME), projection, null, null, null);
                final List<MoodEntryItem> moodEntryItems = new ArrayList<MoodEntryItem>(0);
                mAppExecutors.mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        if (c == null) {
                            callback.onDataNotAvailable();
                        } else {
                            while (c.moveToNext()) {
                                MoodEntryItem item = new MoodEntryItem();
                                item.setId(c.getInt(c.getColumnIndex(MoodEntryItem.TODOITEM_ID)));
                                item.setTitle(c.getString(c.getColumnIndex(MoodEntryItem.TODOITEM_TITLE)));
                                item.setContent(c.getString(c.getColumnIndex(MoodEntryItem.TODOITEM_CONTENT)));
                                item.setDueDate(c.getLong(c.getColumnIndex(MoodEntryItem.TODOITEM_DUEDATE)));
                                item.setCompleted(c.getInt(c.getColumnIndex(MoodEntryItem.TODOITEM_COMPLETED)) > 0);
                                moodEntryItems.add(item);
                            }
                            c.close();
                            callback.onToDoItemsLoaded(moodEntryItems);
                        }
                    }
                });

            }
        };
        mAppExecutors.diskIO().execute(runnable);
    }

    /**
     * Not implemented yet
     *
     * @param toDoItemId
     * @param callback
     */
    @Override
    public void getToDoItem(@NonNull String toDoItemId, @NonNull GetToDoItemCallback callback) {
        Log.d("REPOSITORY", "GetToDoItem");
    }

    /**
     * saveToDoItem runs contentProvider update in separate thread
     *
     * @param moodEntryItem
     */
    @Override
    public void saveToDoItem(@NonNull final MoodEntryItem moodEntryItem) {
        Log.d("REPOSITORY", "SaveToDoItem");
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                ContentValues myCV = new ContentValues();
                myCV.put(MoodEntryItem.TODOITEM_ID, moodEntryItem.getId());
                myCV.put(MoodEntryItem.TODOITEM_TITLE, moodEntryItem.getTitle());
                myCV.put(MoodEntryItem.TODOITEM_CONTENT, moodEntryItem.getContent());
                myCV.put(MoodEntryItem.TODOITEM_DUEDATE, moodEntryItem.getDueDate());
                myCV.put(MoodEntryItem.TODOITEM_COMPLETED, moodEntryItem.getCompleted());
                final int numUpdated = mContext.getContentResolver().update(Uri.parse("content://" + MoodEntryProvider.AUTHORITY + "/" + MoodEntryProvider.TODOITEM_TABLE_NAME), myCV, null, null);
                Log.d("REPOSITORY", "Update ToDo updated " + String.valueOf(numUpdated) + " rows");
            }
        };
        mAppExecutors.diskIO().execute(runnable);

    }

    /**
     * createToDoItem runs contentProvider insert in separate thread
     *
     * @param moodEntryItem
     */
    @Override
    public void createToDoItem(@NonNull final MoodEntryItem moodEntryItem) {
        Log.d("REPOSITORY", "CreateToDoItem");
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                ContentValues myCV = new ContentValues();
                myCV.put(MoodEntryItem.TODOITEM_TITLE, moodEntryItem.getTitle());
                myCV.put(MoodEntryItem.TODOITEM_CONTENT, moodEntryItem.getContent());
                myCV.put(MoodEntryItem.TODOITEM_DUEDATE, moodEntryItem.getDueDate());
                myCV.put(MoodEntryItem.TODOITEM_COMPLETED, moodEntryItem.getCompleted());
                final Uri uri = mContext.getContentResolver().insert(Uri.parse("content://" + MoodEntryProvider.AUTHORITY + "/" + MoodEntryProvider.TODOITEM_TABLE_NAME), myCV);
                Log.d("REPOSITORY", "Create ToDo finished with URI" + uri.toString());
            }
        };
        mAppExecutors.diskIO().execute(runnable);

    }
}
