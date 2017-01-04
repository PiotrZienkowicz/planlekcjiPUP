package pup.zienkowicz.planlekcji;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.sql.Date;
import java.text.SimpleDateFormat;

/**
 * Created by Acer on 04.01.2017.
 */
public class DbHelper extends SQLiteOpenHelper {

    static final String DB_NAME = "lection.db";
    static final int VERSION = 1;
    static final String CREATE_LECTION_TABLE = String.format(
            "CREATE TABLE %s (%s INTEGER PRIMARY KEY, %s TEXT, %s TEXT, %s TEXT, %s TEXT)",
            LectionTable.TABLE_NAME,
            LectionTable._ID,
            LectionTable.START_TIME,
            LectionTable.END_TIME,
            LectionTable.NAME,
            LectionTable.ROOM
    );
    static final String DROP_LECTION_TABLE = String.format(
            "DROP TABLE IF EXISTS %s",
            LectionTable.TABLE_NAME
    );

    public DbHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_LECTION_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_LECTION_TABLE);
        onCreate(db);
    }

    public long InsertLection(Lection lection)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues content = new ContentValues();

        content.put(LectionTable.START_TIME, lection.getStartTime());
        content.put(LectionTable.END_TIME, lection.getEndTime());
        content.put(LectionTable.NAME, lection.getName());
        content.put(LectionTable.ROOM, lection.getRoom());

        return db.insert(LectionTable.TABLE_NAME, null, content);
    }
}

