package reflect.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import util.AppExecutors;


/**
 * AlarmItemRepository class - implements the AlarmDataSource interface
 */
public class AlarmItemRepository implements AlarmDataSource {

    //Memory leak here by including the context - Fix this at some point
    private static volatile AlarmItemRepository INSTANCE;

    //Thread pool for execution on other threads
    private AppExecutors mAppExecutors;
    //Context for calling AlarmProvider
    private Context mContext;

    /**
     * private constructor - prevent direct instantiation
     * @param appExecutors - thread pool
     * @param context
     */
    public AlarmItemRepository(@NonNull AppExecutors appExecutors, @NonNull Context context){
        mAppExecutors = appExecutors;
        mContext = context;
    }

    /**
     * public constructor - prevent creation of instance if one already exists
     * @param appExecutors
     * @param context
     * @return
     */
    public static AlarmItemRepository getInstance(@NonNull AppExecutors appExecutors, @NonNull Context context){
        if(INSTANCE == null){
            synchronized (AlarmItemRepository.class){
                if(INSTANCE == null){
                    INSTANCE = new AlarmItemRepository(appExecutors, context);
                }
            }
        }
        return INSTANCE;
    }

    /**
     * getAlarmItems runs query in a separate thread, and on success loads data from cursor into a list
     * @param callback
     */
    @Override
    public void getAlarmItems(@NonNull final LoadAlarmItemsCallback callback) {
        Log.d("REPOSITORY","Loading...");
        Runnable runnable = new Runnable(){
            @Override
            public void run() {
                String[] projection = {
                        AlarmItem.ALARMITEM_ID,
                        AlarmItem.ALARMITEM_TYPE,
                        AlarmItem.ALARMITEM_SET,
                        AlarmItem.ALARMITEM_TIME
                };
                final Cursor c = mContext.getContentResolver().query(Uri.parse("content://" + AlarmItemProvider.AUTHORITY + "/" + AlarmItemProvider.ALARMITEM_TABLE_NAME), projection, null, null, null);
                final List<AlarmItem> alarmItems = new ArrayList<AlarmItem>(0);
                mAppExecutors.mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        if(c == null){
                            callback.onDataNotAvailable();
                        } else{
                            while(c.moveToNext()) {
                                AlarmItem item = new AlarmItem();
                                item.setId(c.getInt(c.getColumnIndex(AlarmItem.ALARMITEM_ID)));
                                item.setSet(c.getInt(c.getColumnIndex(AlarmItem.ALARMITEM_SET)) > 0);
                                item.setType(c.getString(c.getColumnIndex(AlarmItem.ALARMITEM_TYPE)));
                                item.setTime(c.getString(c.getColumnIndex(AlarmItem.ALARMITEM_TIME)));
                                alarmItems.add(item);
                            }
                            c.close();
                            try {
                                callback.onAlarmItemsLoaded(alarmItems);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });

            }
        };
        mAppExecutors.diskIO().execute(runnable);
    }

    /**
     * Not implemented yet
     * @param alarmItemId
     * @param callback
     */
    @Override
    public void getAlarmItem(@NonNull final int alarmItemId, @NonNull final GetAlarmItemCallback callback) {
        Log.d("REPOSITORY","GetAlarmItem");
        Runnable runnable = new Runnable(){
            @Override
            public void run() {
                String[] projection = {
                        AlarmItem.ALARMITEM_ID,
                        AlarmItem.ALARMITEM_TYPE,
                        AlarmItem.ALARMITEM_SET,
                        AlarmItem.ALARMITEM_TIME
                };
                final Cursor c = mContext.getContentResolver().query(Uri.parse("content://" + AlarmItemProvider.AUTHORITY + "/" + AlarmItemProvider.ALARMITEM_TABLE_NAME), projection, null, null, null);
                final AlarmItem alarmItem = new AlarmItem();
                mAppExecutors.mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        if(c == null){
                            callback.onDataNotAvailable();
                        } else{
                            while(c.moveToNext()) {
                                if(c.getInt(c.getColumnIndex(AlarmItem.ALARMITEM_ID)) == alarmItemId){
                                    alarmItem.setId(c.getInt(c.getColumnIndex(AlarmItem.ALARMITEM_ID)));
                                    alarmItem.setSet(c.getInt(c.getColumnIndex(AlarmItem.ALARMITEM_SET)) > 0);
                                    alarmItem.setType(c.getString(c.getColumnIndex(AlarmItem.ALARMITEM_TYPE)));
                                    alarmItem.setTime(c.getString(c.getColumnIndex(AlarmItem.ALARMITEM_TIME)));
                                }
                            }
                            c.close();
                            callback.onAlarmItemLoaded(alarmItem);
                        }
                    }
                });

            }
        };
        mAppExecutors.diskIO().execute(runnable);
    }

    /**
     * saveAlarmItem runs contentProvider update in separate thread
     * @param alarmItem
     */
    @Override
    public void saveAlarmItem(@NonNull final AlarmItem alarmItem) {
        Log.d("REPOSITORY","SaveAlarmItem");
        Runnable runnable = new Runnable(){
            @Override
            public void run() {
                ContentValues myCV = new ContentValues();
                myCV.put(AlarmItem.ALARMITEM_ID, alarmItem.getId());
                myCV.put(AlarmItem.ALARMITEM_SET, alarmItem.isSet());
                myCV.put(AlarmItem.ALARMITEM_TYPE, alarmItem.getType());
                myCV.put(AlarmItem.ALARMITEM_TIME, alarmItem.getTime());
                final int numUpdated = mContext.getContentResolver().update(Uri.parse("content://" + AlarmItemProvider.AUTHORITY + "/" + AlarmItemProvider.ALARMITEM_TABLE_NAME), myCV,null,null);
                Log.d("REPOSITORY","Update Alarm updated " + String.valueOf(numUpdated) + " rows");
            }
        };
        mAppExecutors.diskIO().execute(runnable);

    }

    /**
     * createAlarmItem runs contentProvider insert in separate thread
     * @param alarmItem
     */
    @Override
    public void createAlarmItem(@NonNull final AlarmItem alarmItem) {
        Log.d("REPOSITORY","CreateAlarmItem");
        Runnable runnable = new Runnable(){
            @Override
            public void run() {
                ContentValues myCV = new ContentValues();
                myCV.put(AlarmItem.ALARMITEM_ID, alarmItem.getId());
                myCV.put(AlarmItem.ALARMITEM_SET, alarmItem.isSet());
                myCV.put(AlarmItem.ALARMITEM_TYPE, alarmItem.getType());
                myCV.put(AlarmItem.ALARMITEM_TIME, alarmItem.getTime());
                final Uri uri = mContext.getContentResolver().insert(Uri.parse("content://" + AlarmItemProvider.AUTHORITY + "/" + AlarmItemProvider.ALARMITEM_TABLE_NAME), myCV);
                Log.d("REPOSITORY","Create Alarm finished with URI" + uri.toString());
            }
        };
        mAppExecutors.diskIO().execute(runnable);

    }

    @Override
    public void deleteAlarmItem(@NonNull final long id) {
        Log.d("REPOSITORY","DeleteAlarmItem");
        Runnable runnable = new Runnable(){
            @Override
            public void run() {
                Uri uri = Uri.parse("content://" + AlarmItemProvider.AUTHORITY + "/" + AlarmItemProvider.ALARMITEM_TABLE_NAME + "/" + id);
                int isDeleted = mContext.getContentResolver().delete(uri, null, null);
                if (isDeleted == 0) {
                    Log.d("REPOSITORY", "DeleteAlarmItem did not delete");
                } else {
                    Log.d("REPOSITORY", "DeleteAlarmItem did delete");
                }
            }
        };
        mAppExecutors.diskIO().execute(runnable);
    }
}
