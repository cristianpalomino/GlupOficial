package pe.com.glup.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;

import pe.com.glup.R;
import pe.com.glup.utils.FastBlur;

/**
 * Created by Glup on 23/06/15.
 */
public class Footer extends LinearLayout implements View.OnClickListener {

    private ImageView home,closet,probador,camara,reserva;

    private ImageView fondo;

    private View view;
    private OnChangeTab onChangeTab;

    public void setOnChangeTab(OnChangeTab onChangeTab) {
        this.onChangeTab = onChangeTab;
    }

    public Footer(Context context) {
        super(context);
    }

    public Footer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public Footer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public Footer(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void initView() {
        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = layoutInflater.inflate(R.layout.footer, this, true);

        home = (ImageView) findViewById(R.id.tabhome);
        closet = (ImageView) findViewById(R.id.tabcloset);
        probador = (ImageView) findViewById(R.id.tabprobador);
        camara = (ImageView) findViewById(R.id.tabcamera);
        reserva = (ImageView) findViewById(R.id.tabreserva);

        home.setOnClickListener(this);
        closet.setOnClickListener(this);
        probador.setOnClickListener(this);
        camara.setOnClickListener(this);
        reserva.setOnClickListener(this);

        onChangeTab.currentTab(0);
        activeDefaultTab();
    }

    @Override
    public void onClick(View v) {
        if (v.equals(home)) {
            onChangeTab.onChangeTab(0);
//            home.setBackgroundResource(R.drawable.glup_tab_off);
//            closet.setBackgroundResource(R.drawable.glup_tab_on);
//            probador.setBackgroundResource(R.drawable.glup_tab_on);
//            camara.setBackgroundResource(R.drawable.glup_tab_on);
        } else if (v.equals(closet)) {
            onChangeTab.onChangeTab(1);
//            home.setBackgroundResource(R.drawable.glup_tab_on);
//            closet.setBackgroundResource(R.drawable.glup_tab_off);
//            probador.setBackgroundResource(R.drawable.glup_tab_on);
//            camara.setBackgroundResource(R.drawable.glup_tab_on);
        } else if (v.equals(probador)) {
            onChangeTab.onChangeTab(2);
//            home.setBackgroundResource(R.drawable.glup_tab_on);
//            closet.setBackgroundResource(R.drawable.glup_tab_on);
//            probador.setBackgroundResource(R.drawable.glup_tab_off);
//            camara.setBackgroundResource(R.drawable.glup_tab_on);
        } else if (v.equals(camara)){
            onChangeTab.onChangeTab(3);
        } else if (v.equals(reserva)) {
            onChangeTab.onChangeTab(4);
//            home.setBackgroundResource(R.drawable.glup_tab_on);
//            closet.setBackgroundResource(R.drawable.glup_tab_on);
//            probador.setBackgroundResource(R.drawable.glup_tab_on);
//            camara.setBackgroundResource(R.drawable.glup_tab_off);
        }
    }

    private void activeDefaultTab() {
//        home.setBackgroundResource(R.drawable.glup_tab_off);
//        closet.setBackgroundResource(R.drawable.glup_tab_on);
//        probador.setBackgroundResource(R.drawable.glup_tab_on);
//        camara.setBackgroundResource(R.drawable.glup_tab_on);
    }

    public interface OnChangeTab {
        void onChangeTab(int position);

        void currentTab(int current);
    }
}
