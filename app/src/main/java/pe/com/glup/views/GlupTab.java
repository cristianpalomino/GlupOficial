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
public class GlupTab extends LinearLayout implements View.OnClickListener {

    private ImageView home;
    private ImageView closet;
    private ImageView probador;
    private ImageView camara;

    private View view;
    private OnChangeTab onChangeTab;

    public void setOnChangeTab(OnChangeTab onChangeTab) {
        this.onChangeTab = onChangeTab;
    }

    public GlupTab(Context context) {
        super(context);
    }

    public GlupTab(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GlupTab(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public GlupTab(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void initView() {
        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = layoutInflater.inflate(R.layout.footer, this, true);

        home = (ImageView) findViewById(R.id.tabhome);
        closet = (ImageView) findViewById(R.id.tabcloset);
        probador = (ImageView) findViewById(R.id.tabprobador);
        camara = (ImageView) findViewById(R.id.tabcamera);

        home.setOnClickListener(this);
        closet.setOnClickListener(this);
        probador.setOnClickListener(this);
        camara.setOnClickListener(this);

        onChangeTab.currentTab(0);
        activeDefaultTab();
    }

    @Override
    public void onClick(View v) {
        if (v.equals(home)) {
            onChangeTab.onChangeTab(0);
            home.setBackgroundResource(R.drawable.glup_tab_off);
            closet.setBackgroundResource(R.drawable.glup_tab_on);
            probador.setBackgroundResource(R.drawable.glup_tab_on);
            camara.setBackgroundResource(R.drawable.glup_tab_on);
        } else if (v.equals(closet)) {
            onChangeTab.onChangeTab(1);
            home.setBackgroundResource(R.drawable.glup_tab_on);
            closet.setBackgroundResource(R.drawable.glup_tab_off);
            probador.setBackgroundResource(R.drawable.glup_tab_on);
            camara.setBackgroundResource(R.drawable.glup_tab_on);
        } else if (v.equals(probador)) {
            onChangeTab.onChangeTab(2);
            home.setBackgroundResource(R.drawable.glup_tab_on);
            closet.setBackgroundResource(R.drawable.glup_tab_on);
            probador.setBackgroundResource(R.drawable.glup_tab_off);
            camara.setBackgroundResource(R.drawable.glup_tab_on);
        } else if (v.equals(camara)) {
            onChangeTab.onChangeTab(3);
            home.setBackgroundResource(R.drawable.glup_tab_on);
            closet.setBackgroundResource(R.drawable.glup_tab_on);
            probador.setBackgroundResource(R.drawable.glup_tab_on);
            camara.setBackgroundResource(R.drawable.glup_tab_off);
        }
    }

    private void activeDefaultTab() {
        home.setBackgroundResource(R.drawable.glup_tab_off);
        closet.setBackgroundResource(R.drawable.glup_tab_on);
        probador.setBackgroundResource(R.drawable.glup_tab_on);
        camara.setBackgroundResource(R.drawable.glup_tab_on);
    }

    public interface OnChangeTab {
        void onChangeTab(int position);
        void currentTab(int current);
    }
}
