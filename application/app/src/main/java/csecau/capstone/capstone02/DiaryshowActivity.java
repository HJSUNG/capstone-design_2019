package csecau.capstone.capstone02;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import org.w3c.dom.Text;

public class DiaryshowActivity extends AppCompatActivity {

    private TextView timeTextview;
    private TextView scoreTextview;
    private TextView contentTextview;

    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diaryshow);

        timeTextview = (TextView) findViewById(R.id.date_textview);
        scoreTextview = (TextView) findViewById(R.id.score_textview);
        contentTextview = (TextView) findViewById(R.id.content_textview);

        intent = getIntent();

        timeTextview.setText(intent.getStringExtra("time"));
        scoreTextview.setText(intent.getStringExtra("score"));
        contentTextview.setText(intent.getStringExtra("content"));
    }
}
