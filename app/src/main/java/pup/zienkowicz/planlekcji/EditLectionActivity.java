package pup.zienkowicz.planlekcji;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;

import java.util.ArrayList;
import java.util.Calendar;

public class EditLectionActivity extends AppCompatActivity {

    private Calendar start;
    private Calendar end;
    private Spinner startHourSpinner;
    private Spinner startMinuteSpinner;
    private Spinner endHourSpinner;
    private Spinner endMinuteSpinner;
    private Spinner dayOfWeekSpinner;

    private DbHelper db;
    private Lection lection;
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_lection);
        db = new DbHelper(this);

        // godziny do wyboru
        String[] hours = new String[24];
        for (int i = 0; i < 24; i++)
        {
            hours[i] = String.valueOf(i);
        }

        // minuty do wyboru
        String[] minutes = new String[60];
        for (int i = 0; i < 60; i++)
        {
            minutes[i] = String.valueOf(i);
        }

        String [] days = getResources().getStringArray(R.array.days_of_week);
        dayOfWeekSpinner = (Spinner) findViewById(R.id.day);
        ArrayAdapter<CharSequence> dayAdapter = new ArrayAdapter<CharSequence>(this,
                android.R.layout.simple_spinner_item, days);
        dayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dayOfWeekSpinner.setAdapter(dayAdapter);

        startHourSpinner = (Spinner) findViewById(R.id.startHour);
        startMinuteSpinner = (Spinner) findViewById(R.id.startMinute);
        endHourSpinner = (Spinner) findViewById(R.id.endHour);
        endMinuteSpinner = (Spinner) findViewById(R.id.endMinute);

        ArrayAdapter<CharSequence> hoursAdapter = new ArrayAdapter<CharSequence>(this,
                android.R.layout.simple_spinner_item, hours);
        hoursAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ArrayAdapter<CharSequence> minutesAdapter = new ArrayAdapter<CharSequence>(this,
                android.R.layout.simple_spinner_item, minutes);
        minutesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        start = Calendar.getInstance();
        start.set(Calendar.SECOND, 0);

        end = (Calendar) start.clone();
        end.add(Calendar.HOUR, 1);
        end.add(Calendar.MINUTE, 30);

        startHourSpinner.setAdapter(hoursAdapter);
        startMinuteSpinner.setAdapter(minutesAdapter);
        endHourSpinner.setAdapter(hoursAdapter);
        endMinuteSpinner.setAdapter(minutesAdapter);


        Intent intent = getIntent();
        Boolean editMode = intent.getBooleanExtra("EDIT", false);
        if(editMode)
        {
            Log.v("MainLog", "InEditMode");
            id = intent.getIntExtra("ID", -1);
            ArrayList<Lection> list = db.SelectLection(
                    new StringBuilder()
                            .append(LectionTable._ID)
                            .append("='")
                            .append(id).append("'").toString()
            );

            for(Lection item: list)
            {
                lection = item;
                ((EditText) findViewById(R.id.nameText)).setText(item.getName());
                ((EditText) findViewById(R.id.roomText)).setText(item.getRoom());

                startHourSpinner.setSelection(item.getStartTimeCalendar().get(Calendar.HOUR_OF_DAY));
                startMinuteSpinner.setSelection(item.getStartTimeCalendar().get(Calendar.MINUTE));
                endHourSpinner.setSelection(item.getEndTimeCalendar().get(Calendar.HOUR_OF_DAY));
                endMinuteSpinner.setSelection(item.getEndTimeCalendar().get(Calendar.MINUTE));

                dayOfWeekSpinner.setSelection(item.getDayOfWeek());
            }

            findViewById(R.id.addButton).setVisibility(View.GONE);
        }
        else
        {
            Log.v("MainLog", "InAddMode");
            startHourSpinner.setSelection(start.get(Calendar.HOUR_OF_DAY));
            startMinuteSpinner.setSelection(start.get(Calendar.MINUTE));
            endHourSpinner.setSelection(end.get(Calendar.HOUR_OF_DAY));
            endMinuteSpinner.setSelection(end.get(Calendar.MINUTE));

            int pos = (int)intent.getLongExtra("DAY", 0);
            dayOfWeekSpinner.setSelection(pos);
            findViewById(R.id.editButton).setVisibility(View.GONE);
            findViewById(R.id.deleteButton).setVisibility(View.GONE);
        }
    }



    private Boolean readFromForm(){
        String name = ((EditText) findViewById(R.id.nameText)).getText().toString();
        String room = ((EditText) findViewById(R.id.roomText)).getText().toString();

        if (name.isEmpty())
        {
            Toast.makeText(this, R.string.nameEmptyError, Toast.LENGTH_SHORT).show();
            return false;
        } else
        if (room.isEmpty())
        {
            Toast.makeText(this, R.string.roomEmptyError, Toast.LENGTH_SHORT).show();
            return false;
        }
        else {
            readTime();
            int day = dayOfWeekSpinner.getSelectedItemPosition();
            lection = new Lection(id, start, end, name, room, day);
            return true;
        }
    }

    private void readTime() {
        int startHour = startHourSpinner.getSelectedItemPosition() + 1;
        int startMinute = startMinuteSpinner.getSelectedItemPosition() + 1;
        start.set(Calendar.HOUR_OF_DAY, startHour);
        start.set(Calendar.MINUTE, startMinute);

        int endHour = endHourSpinner.getSelectedItemPosition() + 1;
        int endMinute = endHourSpinner.getSelectedItemPosition() + 1;
        end.set(Calendar.HOUR_OF_DAY, endHour);
        end.set(Calendar.MINUTE, endMinute);
    }

    public void addToDb(View view)
    {
        readFromForm();
        if(lection != null && db.InsertLection(lection) > 0)
        {
            Log.v("MainLog", "Added");
            Toast.makeText(this, R.string.added, Toast.LENGTH_SHORT).show();
            this.finish();
        }
        else
        {
            Log.e("MainLog", "Adding error");
        }

    }

    private void delete()
    {
        readFromForm();
        if(lection != null && db.DeleteLection(lection) > 0)
        {
            Log.v("MainLog", "Deleted");
            this.finish();
        }
        else
        {
            Log.e("MainLog", "Deleted error");
        }
    }
    public void deleteFromDb(View view) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setMessage(R.string.deleteQuestion);
        dialog.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                delete();
            }
        });

        dialog.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // nic nie rob
            }
        });
        dialog.show();
    }

    public void editInDb(View view) {
        readFromForm();
        if(lection != null && db.UpdateLection(lection) > 0)
        {
            Log.v("MainLog", "Edited");
            Toast.makeText(this, R.string.edited, Toast.LENGTH_SHORT).show();
            this.finish();
        }
        else
        {
            Log.e("MainLog", "Edited error");
        }

    }
}
