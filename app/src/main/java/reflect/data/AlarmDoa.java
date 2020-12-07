package reflect.data;

import android.database.Cursor;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface AlarmDoa {
    /**
     * Insert a alarmItem into the table
     *
     * @return row ID for newly inserted data
     */
    @Insert
    long insert(AlarmItem alarm);

    /**
     * select all AlarmItem
     *
     * @return A {@link Cursor} of all alarmItem in the table
     */
    @Query("SELECT * FROM AlarmItem")
    Cursor findAll();

    /**
     * Delete a alarmItem by ID
     *
     * @return A number of alarmItems deleted
     */
    @Query("DELETE FROM AlarmItem WHERE id = :id ")
    int delete(long id);

    /**
     * Update the pictreItem
     *
     * @return A number of alarmItem updated
     */
    @Update
    int update(AlarmItem alarm);
}