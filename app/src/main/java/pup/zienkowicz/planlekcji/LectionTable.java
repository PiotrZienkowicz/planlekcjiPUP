package pup.zienkowicz.planlekcji;

import android.provider.BaseColumns;

/**
 * Created by Acer on 04.01.2017.
 */
public class LectionTable implements BaseColumns {
    static final String TABLE_NAME = "lection";
    static final String START_TIME = "start_time";
    static final String END_TIME = "end_time";
    static final String NAME = "name";
    static final String ROOM = "room";
    static final String DAY_OF_WEEK = "day_of_week";
}
