package pup.zienkowicz.planlekcji;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * Created by Acer on 02.01.2017.
 */
public class Lection {
    private Calendar startTime;
    private Calendar endTime;
    private String name;
    private String room;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public Lection(Calendar startTime, Calendar endTime, String name, String room) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.name = name;
        this.room = room;
    }

    public String getStartTime() {
        return dateFormat.format(startTime.getTime());
    }

    public void setStartTime(String startTime) {
        Calendar calendar = new GregorianCalendar();
        try {
            calendar.setTime(dateFormat.parse(startTime));
        } catch (ParseException e) {
            Log.e("Lection", "Wrong data format");
            e.printStackTrace();
        }

        this.startTime = calendar;
    }

    public String getEndTime() {
        return dateFormat.format(endTime.getTime());
    }

    public void setEndTime(String endTime) {
        Calendar calendar = new GregorianCalendar();

        try {
            calendar.setTime(dateFormat.parse(endTime));
        } catch (ParseException e) {
            Log.e("Lection", "Wrong data format");
            e.printStackTrace();
        }
        this.endTime = calendar;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }
}
