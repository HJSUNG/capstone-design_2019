package csecau.capstone.capstone02;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class alarm_listviewAdapter extends BaseAdapter {
    private ArrayList<alarm_listview> listviewalarmList = new ArrayList<alarm_listview>();

    @Override
    public int getCount() {
        return listviewalarmList.size();
    }

    @Override
    public Object getItem(int position) {
        return listviewalarmList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        final int pos = position;
        final Context context = viewGroup.getContext();

        if(view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.layout_alarm, viewGroup, false);
        }

        TextView timeTextView = (TextView) view.findViewById(R.id.alarm_time) ;

        alarm_listview listview = listviewalarmList.get(position);

        timeTextView.setText(listview.getTime());

        return view;
    }

    public  void addItem(String time) {
        alarm_listview item = new alarm_listview();

        item.setTime(time);

        listviewalarmList.add(item);
    }

    public void removeItem(int itemnum) {
        listviewalarmList.remove(itemnum);
    }
}
