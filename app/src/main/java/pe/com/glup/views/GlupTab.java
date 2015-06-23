package pe.com.glup.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import pe.com.glup.R;

/**
 * Created by Glup on 23/06/15.
 */
public class GlupTab extends LinearLayout {

    private ImageView home;
    private ImageView closet;
    private ImageView probador;
    private ImageView camara;

    private View view;

    public GlupTab(Context context) {
        super(context);
        initView();
    }

    public GlupTab(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public GlupTab(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public GlupTab(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView();
    }

    private void initView() {
        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = layoutInflater.inflate(R.layout.footer, this, true);
    }
}
