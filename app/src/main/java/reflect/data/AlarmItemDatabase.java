package reflect.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {AlarmItem.class}, version = 1, exportSchema = false)
public abstract class AlarmItemDatabase extends RoomDatabase {
    public static final String DATABASE_NAME = "alarm_db";
    private static AlarmItemDatabase INSTANCE;


    public static AlarmItemDatabase getInstance(Context context){
        if(INSTANCE == null){
            INSTANCE = Room.databaseBuilder(context, AlarmItemDatabase.class,DATABASE_NAME).build();
        }
        return INSTANCE;
    }

    public abstract AlarmDoa getAlarmDao();

}