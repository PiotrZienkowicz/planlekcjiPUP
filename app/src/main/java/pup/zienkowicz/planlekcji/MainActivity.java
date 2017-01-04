package pup.zienkowicz.planlekcji;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class MainActivity extends AppCompatActivity {

    private DbHelper db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DbHelper(this);
    }

    public void addLection(View view) {
        Calendar now = Calendar.getInstance();
        Calendar start = new GregorianCalendar(
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH),
                now.get(Calendar.HOUR_OF_DAY),
                now.get(Calendar.MINUTE)
        );

        Calendar end = new GregorianCalendar(
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH),
                now.get(Calendar.HOUR_OF_DAY),
                now.get(Calendar.MINUTE)
        );

        end.add(Calendar.HOUR, 1);
        end.add(Calendar.MINUTE, 30);

        Lection test = new Lection(start, end, "Zajecia", "102A");
        Log.v("test", test.getStartTime());
        Log.v("test", test.getEndTime());
        
        long zajecia = db.InsertLection(new Lection(start, end, "Zajecia", "102A"));
    }
}
