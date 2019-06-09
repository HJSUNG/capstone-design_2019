package csecau.capstone.capstone02;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class exercise_listviewAdapter extends BaseAdapter {
    private ArrayList<exercise_listview> listviewItemList = new ArrayList<exercise_listview>();

    @Override
    public int getCount() {
        return listviewItemList.size();
    }

    @Override
    public Object getItem(int i) {
        return listviewItemList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.layout_exercise, parent, false);
        }

        TextView activity_TextView = (TextView) convertView.findViewById(R.id.exercise_Activity);
        activity_TextView.setMaxLines(1);
        activity_TextView.setEllipsize(TextUtils.TruncateAt.END);
        TextView value_TextView = (TextView) convertView.findViewById(R.id.exercise_value);
        TextView time_TextView = (TextView) convertView.findViewById(R.id.exercise_time);

        exercise_listview listViewItem = listviewItemList.get(position);

        activity_TextView.setText(listViewItem.getActivity());
        value_TextView.setText(listViewItem.getValue());
        time_TextView.setText(listViewItem.getTime());

        return convertView;
    }

    public void addItem(String Activity, String value, String time){
        exercise_listview item = new exercise_listview();

        item.setActivity(Activity);
        item.setValue(value);
        item.setTime(time);

        listviewItemList.add(item);
    }
}
