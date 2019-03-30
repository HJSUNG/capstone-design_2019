package csecau.capstone.capstone02;

import android.graphics.drawable.Drawable;

public class diary_listview {
    private Drawable icon;
    private String content;
    private String Time;

    public Drawable getIcon() {
        return icon;
    }

    public String getContent() {
        return content;
    }

    public String getTime() {
        return Time;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setTime(String time) {
        Time = time;
    }
}
