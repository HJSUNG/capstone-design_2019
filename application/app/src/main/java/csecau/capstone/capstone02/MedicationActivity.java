package csecau.capstone.capstone02;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class MedicationActivity extends AppCompatActivity{

    AlarmManager alarm_manager;
    TimePicker alarm_timepicker;
    Context context;
    PendingIntent pendingIntent;

    private Button Addbtn, Deletebtn;

//    @Override
//    public void onClick(View v){
//        TimePickerFragment mTimePickerFragment = new TimePickerFragment();
//        mTimePickerFragment.show(getSupportFragmentManager(), TimePickerFragment.FRAGMENT_TAG);
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medication);

        Addbtn = (Button)findViewById(R.id.btn_add);
        Deletebtn = (Button)findViewById(R.id.btn_delete) ;

//        final Calendar calendar = Calendar.getInstance();
//        final Intent my_intent = new Intent(this.context, alarm_Receiver.class);

        Addbtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                String UserID = "6";

//                calendar.set(Calendar.HOUR_OF_DAY, alarm_timepicker.getHour());
//                calendar.set(Calendar.MINUTE, alarm_timepicker.getMinute());

//                int hour = alarm_timepicker.getHour();
//                int minute = alarm_timepicker.getMinute();
//                Toast.makeText(MedicationActivity.this, "Alarm 예정"+hour+"시"+minute+"분",Toast.LENGTH_LONG);

//                my_intent.putExtra("state","alarm on");

//                pendingIntent = PendingIntent.getBroadcast(MedicationActivity.this,0,my_intent,PendingIntent.FLAG_UPDATE_CURRENT);

//                alarm_manager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

                //Alarm task = new Alarm();
                //task.execute("http://capstone02.cafe24.com/insert_glucose.php", UserID, glucose, comment);

                //TimePickerFragment mTimePickerFragment = new TimePickerFragment();
                //mTimePickerFragment.show(getSupportFragmentManager(), TimePickerFragment.FRAGMENT_TAG);
            }
        });

        ListView listview;
        alarm_listviewAdapter adapter;

        adapter = new alarm_listviewAdapter();

        listview = (ListView) findViewById(R.id. alarmlistview);
        listview.setAdapter(adapter);

        adapter.addItem("오전 8:30");
        adapter.addItem("오전 11:30");
        adapter.addItem("오후 5:30");
    }
}
