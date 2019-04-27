package csecau.capstone.capstone02;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;

import java.util.Calendar;

public class MedicationActivity extends AppCompatActivity{

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

        Addbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerFragment mTimePickerFragment = new TimePickerFragment();
                mTimePickerFragment.show(getSupportFragmentManager(), TimePickerFragment.FRAGMENT_TAG);
            }
        });
    }
}
