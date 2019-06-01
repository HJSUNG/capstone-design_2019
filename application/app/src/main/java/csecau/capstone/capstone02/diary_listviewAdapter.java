package csecau.capstone.capstone02;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class diary_listviewAdapter extends BaseAdapter {
    private ArrayList<diary_listview> listviewItemList = new ArrayList<diary_listview>();

    @Override
    public  Object getItem(int position) {
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
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        // "listview_item" Layout을 inflate하여 convertView 참조 획득.
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.layout_diary, parent, false);
        }

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        TextView analysis_scoreTextView = (TextView) convertView.findViewById(R.id.dairy_text_analysis_score) ;
        TextView contentTextView = (TextView) convertView.findViewById(R.id.dairy_text_content) ;
        contentTextView.setMaxLines(1);
        contentTextView.setEllipsize(TextUtils.TruncateAt.END);
        TextView timeTextView = (TextView) convertView.findViewById(R.id.dairy_text_time) ;

        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        diary_listview listViewItem = listviewItemList.get(position);

        // 아이템 내 각 위젯에 데이터 반영
        analysis_scoreTextView.setText(listViewItem.getAnalysis_score());
        contentTextView.setText(listViewItem.getContent());
        timeTextView.setText(listViewItem.getTime());

        return convertView;
    }

    public void addItem(String analysis_score, String content, String time) {
        diary_listview item = new diary_listview();

        item.setAnalysis_score(analysis_score);
        item.setContent(content);
        item.setTime(time);

        listviewItemList.add(item);
    }


    public void clear() {
        listviewItemList.clear();
    }
}