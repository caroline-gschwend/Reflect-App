package reflect.data;

import android.database.Cursor;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface MoodEntryItemDao {
    /**
     * Insert a todoitem into the table
     *
     * @return row ID for newly inserted data
     */
    @Insert
    long insert(MoodEntryItem item);

    /**
     * select all todoitems
     *
     * @return A {@link Cursor} of all todoitems in the table
     */
    @Query("SELECT * FROM MoodEntryItem")
    Cursor findAll();

    /**
     * Delete a todoitem by ID
     *
     * @return A number of todoitems deleted
     */
    @Query("DELETE FROM MoodEntryItem WHERE id = :id ")
    int delete(long id);

    /**
     * Update the todoitem
     *
     * @return A number of todoitems updated
     */
    @Update
    int update(MoodEntryItem todoitem);
}