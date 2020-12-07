package reflect.data;

import androidx.annotation.NonNull;

import java.text.ParseException;
import java.util.List;

/**
 * Interface for any implementation of a AlarmListDataSource
 * (Currently only have one - a local ContentProvider based implementation (@AlarmItemRepository)
 */
public interface AlarmDataSource {

    /**
     * LoadAlarmItemsCallback interface
     * Example of how to implement callback functions depending on the result of functions in interfaces
     * Currently, onDataNotAvailable is not implemented
     */
    interface LoadAlarmItemsCallback {

        void onAlarmItemsLoaded(List<AlarmItem> alarmItems) throws ParseException;

        void onDataNotAvailable();
    }

    /**
     * GetAlarmItemsCallback interface
     * Not currently implementd
     */
    interface GetAlarmItemCallback {

        void onAlarmItemLoaded(AlarmItem task);

        void onDataNotAvailable();
    }

    /**
     * getAlarmItems loads all AlarmItems, calls either success or failure fuction above
     * @param callback - Callback function
     */
    void getAlarmItems(@NonNull LoadAlarmItemsCallback callback);

    /**
     * getAlarmItem - Get a single AlarmItem - currently not implemented
     * @param alarmItemId - String of the current ItemID to be retrieved
     * @param callback - Callback function
     */
    void getAlarmItem(@NonNull int alarmItemId, @NonNull GetAlarmItemCallback callback);

    /**
     * SaveAlarmItem saves a toDoItem to the database - No callback (should be implemented for
     * remote databases)
     * @param alarmItem
     */
    void saveAlarmItem(@NonNull final AlarmItem alarmItem);

    /**
     * CreateAlarmItem adds a toDoItem to the database - No callback (should be implemented for
     * remote databases)
     * @param alarmItem
     */
    void createAlarmItem(@NonNull AlarmItem alarmItem);

    /**
     * deleteAlarmItem deletes a toDoItem from the database - No callback (should be implemented for
     *      * remote databases)
     * @param id
     */
    void deleteAlarmItem(@NonNull long id);
}