package csecau.capstone.capstone02;

import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Build;
import android.support.v4.app.FragmentManager;
import android.widget.TimePicker;

import java.util.Calendar;

public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

    private TimePicker mTimePicker;
    private Calendar mCalendar;
    public static final String FRAGMENT_TAG = "MedicationFragment";

    @Override
    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
        mCalendar = Calendar.getInstance();
        int hour, min;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            hour = mTimePicker.getHour();
            min = mTimePicker.getMinute();
        } else {
            hour = mTimePicker.getCurrentHour();
            min =mTimePicker.getCurrentMinute();
        }
    }

    public void show(FragmentManager supportFragmentManager, String fragmentTag) {
    }
}
