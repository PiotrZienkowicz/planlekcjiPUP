package pup.zienkowicz.planlekcji;

import android.app.ActionBar;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private DbHelper db;
    private long selectedDay;
    private String[] days;
    private Spinner daySpinner;
    ArrayList<Lection> lectionList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DbHelper(this);

        days = getResources().getStringArray(R.array.days_of_week);
        selectedDay = Calendar.getInstance().get(Calendar.DAY_OF_WEEK) - 1;

        daySpinner = (Spinner) findViewById(R.id.day);
        ArrayAdapter<String> dayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, days);
        dayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        daySpinner.setAdapter(dayAdapter);
        daySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedDay = id;
                Log.v("MainLog", "Selection changed");
                Refresh(view);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // nic nie rob
            }
        });
        daySpinner.setSelection((int)selectedDay);

        refreshView();
    }

    public void Edit(View view, Lection lection) {
        Log.v("MainLog", "Edit: " + lection.getId());
        Intent edit = new Intent(this, EditLectionActivity.class);
        edit.putExtra("EDIT", true);
        edit.putExtra("ID", lection.getId());
        startActivity(edit);
    }

    public  void Add(View view) {
        Log.v("MainLog", "Add new");
        Intent edit = new Intent(this, EditLectionActivity.class);
        edit.putExtra("EDIT", false);
        edit.putExtra("DAY", selectedDay);
        startActivity(edit);
    }

    public void Refresh(View view) {
        refreshView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshView();
    }

    private void refreshView() {
        getLectionByDay();
        LinearLayout container = (LinearLayout) findViewById(R.id.container);
        container.removeAllViews();

        for(final Lection item: lectionList)
        {
            Button button = new Button(this);
            button.setText(new StringBuilder()
                    .append(item.getStartTime())
                    .append(" - ").append(item.getEndTime())
                    .append("\n").append(item.getName())
                    .append("\n").append(item.getRoom())
                    .toString()
            );
            button.setGravity(Gravity.LEFT);
            button.setPadding(5,5,5,5);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(0,0,0,5);

            button.setLayoutParams(params);

            switch (checkLectionStatus(item))
            {
                case BEFORE:
                    button.setBackgroundColor(getResources().getColor(R.color.blue));
                    break;
                case AFTER:
                    button.setBackgroundColor(getResources().getColor(R.color.gray));
                    break;
                case IN_PROGRESS:
                    button.setBackgroundColor(getResources().getColor(R.color.green));
                    break;
            }
            button.setTextColor(getResources().getColor(R.color.white));

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Edit(v, item);
                }
            });
            container.addView(button);

            // dodaj elemennty i zmien ich kolor
            Log.v("MainLog", item.getName());
        }
    }


    private enum LECTION_STATUS { BEFORE, IN_PROGRESS, AFTER }

    private LECTION_STATUS checkLectionStatus(Lection lection)
    {
        //czas teraz
        Calendar now = Calendar.getInstance();
        int h = now.get(Calendar.HOUR_OF_DAY);
        int m = now.get(Calendar.MINUTE);

        // czas rozpoczecia
        int sh = lection.getStartTimeCalendar().get(Calendar.HOUR_OF_DAY);
        int sm = lection.getStartTimeCalendar().get(Calendar.MINUTE);

        // czas zakonczenia
        int eh = lection.getEndTimeCalendar().get(Calendar.HOUR_OF_DAY);
        int em = lection.getEndTimeCalendar().get(Calendar.MINUTE);


        if(h < sh)
        {
            return LECTION_STATUS.BEFORE;
        }
        else
        if( h == sh )
        {
            if(m < sm)
                return LECTION_STATUS.BEFORE;
            else
                return LECTION_STATUS.IN_PROGRESS;
        }
        else
        if(h > eh)
        {
            return LECTION_STATUS.AFTER;
        }
        else
        if (h == eh)
        {
            if(m > em)
                return LECTION_STATUS.AFTER;
            else
                return LECTION_STATUS.IN_PROGRESS;
        }

        // nie powinno dotad dojsc ale niech sobie juz bedzie
        return LECTION_STATUS.IN_PROGRESS;
    }



    private void getLectionByDay()
    {
        lectionList = db.SelectLection(
                new StringBuilder()
                .append(LectionTable.DAY_OF_WEEK)
                .append("='")
                .append(selectedDay).append("'").toString()
        );
        Log.v("MainLog", String.valueOf("Selected: " + lectionList.size()));
    }
}