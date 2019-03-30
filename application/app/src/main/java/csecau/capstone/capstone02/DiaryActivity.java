package csecau.capstone.capstone02;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

public class DiaryActivity extends AppCompatActivity {

    private Button newdiaryButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dairy);

        newdiaryButton = (Button)findViewById(R.id.newdiarybutton);

        newdiaryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), NewdiaryActivity.class);
                startActivity(intent);
            }
        });

        ListView listview;
        diary_listviewAdapter adapter;

        adapter = new diary_listviewAdapter();

        listview = (ListView) findViewById(R.id. dairylistview);
        listview.setAdapter(adapter);

        // 첫 번째 아이템 추가.
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.image_good),
                "First Dairy content", "2019.3.29") ;
        // 두 번째 아이템 추가.
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.image_soso),
                "Second Dairy content", "2019.3.30") ;
        // 세 번째 아이템 추가.
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.image_bad),
                "Third Dairy content", "2019.3.31") ;
    }
}
