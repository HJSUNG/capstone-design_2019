package csecau.capstone.capstone02;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class meal_listviewAdapter extends BaseAdapter {
    private ArrayList<meal_listview> listviewItemList = new ArrayList<>();

    @Override
    public Object getItem(int position) {
        return listviewItemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getCount() {
        return listviewItemList.size();
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        final  int pos = position;
        final Context context = parent.getContext();

        if (view == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.layout_meal, parent, false);
        }

        TextView meal1 = (TextView) view.findViewById(R.id.meal1);
        TextView meal2 = (TextView) view.findViewById(R.id.meal2);
        TextView meal3 = (TextView) view.findViewById(R.id.meal3);
        TextView meal4 = (TextView) view.findViewById(R.id.meal4);
        TextView meal5 = (TextView) view.findViewById(R.id.meal5);
        TextView time = (TextView) view.findViewById(R.id.meal_time);

        meal_listview listViewItem = listviewItemList.get(position);

        meal1.setText(listViewItem.getMeal1());
        meal2.setText(listViewItem.getMeal2());
        meal3.setText(listViewItem.getMeal3());
        meal4.setText(listViewItem.getMeal4());
        meal5.setText(listViewItem.getMeal5());
        time.setText(listViewItem.getTime());

        return view;
    }

    public void addItem(String meal1, String meal2, String meal3, String meal4, String meal5, String time){
        meal_listview item = new meal_listview();

        item.setMeal1(meal1);
        item.setMeal2(meal2);
        item.setMeal3(meal3);
        item.setMeal4(meal4);
        item.setMeal5(meal5);
        item.setTime(time);

        listviewItemList.add(item);
    }
}
