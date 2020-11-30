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
public class MoodEntryItem implements Serializable {

    //Static strings for the column names usable by other classes
    public static final String MOODENTRYITEM_ID = "id";
    public static final String MOODENTRYITEM_COLOR = "color";
    public static final String MOODENTRYITEM_MOOD = "mood";
    public static final String MOODENTRYITEM_CONTENT = "content";
    public static final String MOODENTRYITEM_TIMESTAMP = "timestamp";

    @PrimaryKey(autoGenerate = true)
    private Integer id;

    @ColumnInfo(name = "color")
    private int color;

    @ColumnInfo(name = "mood")
    private String mood;

    @ColumnInfo(name = "content")
    private String content;

    @ColumnInfo(name = "timestamp")
    private long timestamp;

    //Following are getters and setters for all five member variables
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getColor() {
        return color;
    }

    public String getMood() {
        return mood;
    }

    public void setMood(String mood) {
        this.mood = mood;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }

    public long getTimestamp() { return timestamp; }

    //Create a ToDoItem from a ContentValues object
    public static MoodEntryItem fromContentValues(ContentValues contentValues) {
        MoodEntryItem item = new MoodEntryItem();
        if (contentValues.containsKey(MOODENTRYITEM_ID)) {
            item.setId(contentValues.getAsInteger(MOODENTRYITEM_ID));
        }
        if (contentValues.containsKey(MOODENTRYITEM_COLOR)) {
            item.setColor(contentValues.getAsInteger(MOODENTRYITEM_COLOR));
        }
        if (contentValues.containsKey(MOODENTRYITEM_MOOD)) {
            item.setMood(contentValues.getAsString(MOODENTRYITEM_MOOD));
        }
        if (contentValues.containsKey(MOODENTRYITEM_CONTENT)) {
            item.setContent(contentValues.getAsString(MOODENTRYITEM_CONTENT));
        }
        if(contentValues.containsKey(MOODENTRYITEM_TIMESTAMP)) {
            item.setTimestamp(contentValues.getAsLong(MOODENTRYITEM_TIMESTAMP));
        }
        return item;
    }
}
