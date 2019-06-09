package csecau.capstone.capstone02;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class glucose_listviewAdapter extends BaseAdapter{
    private ArrayList<glucose_listview> listviewItemList = new ArrayList<>();

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
        final int pos = position;
        final Context context = parent.getContext();

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.layout_glucose, parent, false);
        }

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        TextView glucose_valueTextView = (TextView) view.findViewById(R.id.glucose_value) ;
        TextView glucose_memoTextView = (TextView) view.findViewById(R.id.glucose_memo_content) ;
        TextView glucose_timeTextView = (TextView) view.findViewById(R.id.glucose_time) ;

        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        glucose_listview listViewItem = listviewItemList.get(position);

        // 아이템 내 각 위젯에 데이터 반영
        glucose_valueTextView.setText(listViewItem.getGlucose_value());
        glucose_memoTextView.setText(listViewItem.getGlucose_memo());
        glucose_timeTextView.setText(listViewItem.getGlucose_time());

        return view;
    }

    public void addItem(String glucose_value, String glucose_memo, String glucose_time){
        glucose_listview item = new glucose_listview();

        item.setGlucose_value(glucose_value);
        item.setGlucose_memo(glucose_memo);
        item.setGlucose_time(glucose_time);

        listviewItemList.add(item);
    }
}
