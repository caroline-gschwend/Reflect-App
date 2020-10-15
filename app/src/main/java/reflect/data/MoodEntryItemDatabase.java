package reflect.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

//Room Database implementation
//Don't touch unless you know what you are doing.
@Database(entities = {MoodEntryItem.class}, version = 1, exportSchema = false)
public abstract class MoodEntryItemDatabase extends RoomDatabase {
    public static final String DATABASE_NAME = "moodentry_db";
    private static MoodEntryItemDatabase INSTANCE;

    public static MoodEntryItemDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context, MoodEntryItemDatabase.class, DATABASE_NAME).build();
        }
        return INSTANCE;
    }

    public abstract MoodEntryItemDao getToDoItemDao();

}
