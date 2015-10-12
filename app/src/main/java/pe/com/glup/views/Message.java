package pe.com.glup.views;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.TextView;
import android.widget.Toast;

import pe.com.glup.R;
import pe.com.glup.utils.Util_Fonts;

/**
 * Created by Glup on 21/09/15.
 */
public class Message extends Toast{
    /**
     * Construct an empty Toast object.  You must call {@link #setView} before you
     * can call {@link #show}.
     *
     * @param context The context to use.  Usually your {@link Application}
     *                or {@link Activity} object.
     */
    public Message(Context context, String msg, int duration) {
        super(context);
        this.setDuration(duration);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        TextView textView = new TextView(context);
        textView.setTextColor(context.getResources().getColor(R.color.celeste_glup));
        textView.setBackgroundColor(Color.TRANSPARENT);
        textView.setGravity(Gravity.CENTER);
        textView.setTextSize(18);
        textView.setText(msg);
        this.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        this.setView(textView);
    }
}
