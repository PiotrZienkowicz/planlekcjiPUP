package pup.zienkowicz.planlekcji;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Acer on 04.01.2017.
 */
public class DbHelper extends SQLiteOpenHelper {

    static final String DB_NAME = "lection.db";
    static final int VERSION = 1;
    static final String CREATE_LECTION_TABLE = String.format(
            "CREATE TABLE %s (%s INTEGER PRIMARY KEY, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s INTEGER)",
            LectionTable.TABLE_NAME,
            LectionTable._ID,
            LectionTable.START_TIME,
            LectionTable.END_TIME,
            LectionTable.NAME,
            LectionTable.ROOM,
            LectionTable.DAY_OF_WEEK
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
        if(lection == null) throw new NullPointerException();

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues content = new ContentValues();

        content.put(LectionTable.START_TIME, lection.getStartTime());
        content.put(LectionTable.END_TIME, lection.getEndTime());
        content.put(LectionTable.NAME, lection.getName());
        content.put(LectionTable.ROOM, lection.getRoom());
        content.put(LectionTable.DAY_OF_WEEK, lection.getDayOfWeek());

        return db.insert(LectionTable.TABLE_NAME, null, content);
    }

    public ArrayList<Lection> SelectLection(String selection) {
        ArrayList<Lection> allLections = new ArrayList<Lection>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.query(
                LectionTable.TABLE_NAME,
                new String[]{
                        LectionTable._ID,
                        LectionTable.NAME,
                        LectionTable.ROOM,
                        LectionTable.DAY_OF_WEEK,
                        LectionTable.START_TIME,
                        LectionTable.END_TIME
                }, selection, null, null, null, LectionTable.START_TIME + " ASC"
        );
        while (c.moveToNext()) {
            allLections.add(new Lection(
                    c.getInt(c.getColumnIndex(LectionTable._ID)),
                    c.getString(c.getColumnIndex(LectionTable.START_TIME)),
                    c.getString(c.getColumnIndex(LectionTable.END_TIME)),
                    c.getString(c.getColumnIndex(LectionTable.NAME)),
                    c.getString(c.getColumnIndex(LectionTable.ROOM)),
                    c.getInt(c.getColumnIndex(LectionTable.DAY_OF_WEEK))
            ));
        }
        return allLections;
    }
        public int DeleteLection(Lection lection)
        {
            if(lection == null) throw new NullPointerException();
            SQLiteDatabase db = this.getWritableDatabase();
            int x = db.delete(
                    LectionTable.TABLE_NAME,
                    LectionTable._ID + "=?",
                    new String[] { String.valueOf(lection.getId()) }
            );
            return x;
        }

        public int UpdateLection(Lection lection)
        {
            if(lection == null) throw new NullPointerException();
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues content = new ContentValues();

            content.put(LectionTable.START_TIME, lection.getStartTime());
            content.put(LectionTable.END_TIME, lection.getEndTime());
            content.put(LectionTable.NAME, lection.getName());
            content.put(LectionTable.ROOM, lection.getRoom());
            content.put(LectionTable.DAY_OF_WEEK, lection.getDayOfWeek());

            int x = db.update(
                    LectionTable.TABLE_NAME,
                    content,
                    LectionTable._ID + "=?",
                    new String[] { String.valueOf(lection.getId()) }
            );
            return x;
        }
    }






