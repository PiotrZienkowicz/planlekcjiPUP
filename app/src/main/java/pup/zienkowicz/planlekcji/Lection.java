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
    private int id;
    private Calendar startTime;
    private Calendar endTime;
    private String name;
    private String room;
    private int dayOfWeek;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");

    public Lection(int id, Calendar startTime, Calendar endTime, String name, String room, int dayOfWeek) {
        this.id = id;
        this.startTime = startTime;
        this.endTime = endTime;
        this.name = name;
        this.room = room;
        this.dayOfWeek = dayOfWeek;
    }

    public Lection(int id, String startTime, String endTime, String name, String room, int dayOfWeek) {
        this.id = id;
        setStartTime(startTime);
        setEndTime(endTime);
        this.name = name;
        this.room = room;
        this.dayOfWeek = dayOfWeek;
    }

    public String getStartTime() {
        return dateFormat.format(startTime.getTime());
    }
    public Calendar getStartTimeCalendar() {
        return startTime;
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
    public Calendar getEndTimeCalendar() {
        return endTime;
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

    public int getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(int dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    @Override
    public String toString() {
        return new StringBuilder()
                .append("{Name: ").append(name)
                .append(", Room=").append(room)
                .append(", Day=").append(dayOfWeek)
                .append(", Start=").append(getStartTime())
                .append(", End=").append(getEndTime())
                .append("}").toString();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
