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
                        MoodEntryItem.MOODENTRYITEM_ID,
                        MoodEntryItem.MOODENTRYITEM_COLOR,
                        MoodEntryItem.MOODENTRYITEM_MOOD,
                        MoodEntryItem.MOODENTRYITEM_CONTENT};
                final Cursor c = mContext.getContentResolver().query(Uri.parse("content://" + MoodEntryProvider.AUTHORITY + "/" + MoodEntryProvider.MOODENTRYITEM_TABLE_NAME), projection, null, null, null);
                final List<MoodEntryItem> moodEntryItems = new ArrayList<MoodEntryItem>(0);
                mAppExecutors.mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        if (c == null) {
                            callback.onDataNotAvailable();
                        } else {
                            while (c.moveToNext()) {
                                MoodEntryItem item = new MoodEntryItem();
                                item.setId(c.getInt(c.getColumnIndex(MoodEntryItem.MOODENTRYITEM_ID)));
                                item.setColor(c.getInt(c.getColumnIndex(MoodEntryItem.MOODENTRYITEM_COLOR)));
                                item.setMood(c.getString(c.getColumnIndex(MoodEntryItem.MOODENTRYITEM_MOOD)));
                                item.setContent(c.getString(c.getColumnIndex(MoodEntryItem.MOODENTRYITEM_CONTENT)));
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
     * @param itemId
     * @param callback
     */
    @Override
    public void getToDoItem(@NonNull final int itemId, @NonNull final GetToDoItemCallback callback) {
        Log.d("REPOSITORY", "GetToDoItem");
        Runnable runnable = new Runnable(){
            @Override
            public void run() {
                String[] projection = {
                        MoodEntryItem.MOODENTRYITEM_ID,
                        MoodEntryItem.MOODENTRYITEM_COLOR,
                        MoodEntryItem.MOODENTRYITEM_MOOD,
                        MoodEntryItem.MOODENTRYITEM_CONTENT};
                final Cursor c = mContext.getContentResolver().query(Uri.parse("content://" + MoodEntryProvider.AUTHORITY + "/" + MoodEntryProvider.MOODENTRYITEM_TABLE_NAME),
                        projection, null, null, null);
                final MoodEntryItem item = new MoodEntryItem();
                mAppExecutors.mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        if(c == null){
                            callback.onDataNotAvailable();
                        } else{
                            while(c.moveToNext()) {
                                if(c.getInt(c.getColumnIndex(MoodEntryItem.MOODENTRYITEM_ID))== itemId) {
                                    item.setId(c.getInt(c.getColumnIndex(MoodEntryItem.MOODENTRYITEM_ID)));
                                    item.setColor(c.getInt(c.getColumnIndex(MoodEntryItem.MOODENTRYITEM_COLOR)));
                                    item.setMood(c.getString(c.getColumnIndex(MoodEntryItem.MOODENTRYITEM_MOOD)));
                                    item.setContent(c.getString(c.getColumnIndex(MoodEntryItem.MOODENTRYITEM_CONTENT)));
                                }
                            }
                            c.close();
                            callback.onToDoItemLoaded(item);
                        }
                    }
                });

            }
        };
        mAppExecutors.diskIO().execute(runnable);
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
                myCV.put(MoodEntryItem.MOODENTRYITEM_ID, moodEntryItem.getId());
                myCV.put(MoodEntryItem.MOODENTRYITEM_COLOR, moodEntryItem.getColor());
                myCV.put(MoodEntryItem.MOODENTRYITEM_MOOD, moodEntryItem.getMood());
                myCV.put(MoodEntryItem.MOODENTRYITEM_CONTENT, moodEntryItem.getContent());
                final int numUpdated = mContext.getContentResolver().update(Uri.parse("content://" + MoodEntryProvider.AUTHORITY + "/" + MoodEntryProvider.MOODENTRYITEM_TABLE_NAME), myCV, null, null);
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
                myCV.put(MoodEntryItem.MOODENTRYITEM_COLOR, moodEntryItem.getColor());
                myCV.put(MoodEntryItem.MOODENTRYITEM_MOOD, moodEntryItem.getMood());
                myCV.put(MoodEntryItem.MOODENTRYITEM_CONTENT, moodEntryItem.getContent());
                final Uri uri = mContext.getContentResolver().insert(Uri.parse("content://" + MoodEntryProvider.AUTHORITY + "/" + MoodEntryProvider.MOODENTRYITEM_TABLE_NAME), myCV);
                Log.d("REPOSITORY", "Create ToDo finished with URI" + uri.toString());
            }
        };
        mAppExecutors.diskIO().execute(runnable);

    }
}
