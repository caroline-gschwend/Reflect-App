package reflect.data;

import android.content.ContentValues;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;
/**
 * ToDoItem class
 * Implements serializable for easy pass through between intents
 * Includes Room annotations for five columns for each of five private members
 */
@Entity
public class AlarmItem implements Serializable {

    //Static strings for the column names usable by other classes
    public static final String ALARMITEM_ID = "id";
    public static final String ALARMITEM_TYPE = "type";
    public static final String ALARMITEM_SET = "set";
    public static final String ALARMITEM_TIME = "time";

    @PrimaryKey(autoGenerate = true)
    private Integer id;

    @ColumnInfo(name = "set")
    private boolean set;

    @ColumnInfo(name = "type")
    private String type;

    @ColumnInfo(name = "time")
    private String time;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public boolean isSet() {
        return set;
    }

    public void setSet(boolean set) {
        this.set = set;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    //Create a ToDoItem from a ContentValues object
    public static AlarmItem fromContentValues(ContentValues contentValues) {
        AlarmItem item = new AlarmItem();
        if (contentValues.containsKey(ALARMITEM_ID)) {
            item.setId(contentValues.getAsInteger(ALARMITEM_ID));
        }
        if (contentValues.containsKey(ALARMITEM_SET)) {
            item.setSet(contentValues.getAsBoolean(ALARMITEM_SET));
        }
        if (contentValues.containsKey(ALARMITEM_TYPE)) {
            item.setType(contentValues.getAsString(ALARMITEM_TYPE));
        }
        if (contentValues.containsKey(ALARMITEM_TIME)) {
            item.setTime(contentValues.getAsString(ALARMITEM_TIME));
        }
        return item;
    }
}