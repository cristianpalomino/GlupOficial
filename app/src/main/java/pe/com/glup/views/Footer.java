package pe.com.glup.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.squareup.otto.Subscribe;

import pe.com.glup.R;
import pe.com.glup.bus.BusHolder;
import pe.com.glup.fragments.FCatalogoNew;
import pe.com.glup.glup.Principal;
import pe.com.glup.utils.FastBlur;

/**
 * Created by Glup on 23/06/15.
 */
public class Footer extends LinearLayout implements View.OnClickListener {

    private ImageView home,closet,probador,camara,reserva;

    private ImageView fondo;

    private View view;
    private OnChangeTab onChangeTab;
    private Context context;

    public void setOnChangeTab(OnChangeTab onChangeTab) {
        this.onChangeTab = onChangeTab;
    }

    public Footer(Context context) {
        super(context);
        this.context=context;
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

        BusHolder.getInstance().register(this);


        //onChangeTab.currentTab(0);
        /*String CURRENT_FRAGMENT_TAG = FCatalogoNew.class.getSimpleName();
        Fragment current= FCatalogoNew.newInstance();
        ((Principal)context).getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame_principal, current, CURRENT_FRAGMENT_TAG)
                .commit();

        Log.e("FRAGMENTS", CURRENT_FRAGMENT_TAG + "");*/
        activeDefaultTab();
        /*home.setEnabled(false);
        closet.setEnabled(true);
        probador.setEnabled(true);
        camara.setEnabled(true);
        reserva.setEnabled(true);*/

    }

    @Override
    public void onClick(View v) {
        if (v.equals(home)) {
            onChangeTab.onChangeTab(0);
            home.setEnabled(false);
            closet.setEnabled(true);
            probador.setEnabled(true);
            camara.setEnabled(true);
            reserva.setEnabled(true);
            /*home.setBackgroundResource(R.drawable.glup_tab_on);
            closet.setBackgroundResource(R.drawable.glup_tab_off);
            probador.setBackgroundResource(R.drawable.glup_tab_off);
            reserva.setBackgroundResource(R.drawable.glup_tab_off);*/
        } else if (v.equals(closet)) {
            onChangeTab.onChangeTab(1);
            home.setEnabled(true);
            closet.setEnabled(false);
            probador.setEnabled(true);
            camara.setEnabled(true);
            reserva.setEnabled(true);
            /*home.setBackgroundResource(R.drawable.glup_tab_off);
            closet.setBackgroundResource(R.drawable.glup_tab_on);
            probador.setBackgroundResource(R.drawable.glup_tab_off);
            reserva.setBackgroundResource(R.drawable.glup_tab_off);*/
        } else if (v.equals(probador)) {
            onChangeTab.onChangeTab(2);
            home.setEnabled(true);
            closet.setEnabled(true);
            probador.setEnabled(false);
            camara.setEnabled(true);
            reserva.setEnabled(true);
            /*home.setBackgroundResource(R.drawable.glup_tab_off);
            closet.setBackgroundResource(R.drawable.glup_tab_off);
            probador.setBackgroundResource(R.drawable.glup_tab_on);
            reserva.setBackgroundResource(R.drawable.glup_tab_off);*/
        } else if (v.equals(camara)){
            onChangeTab.onChangeTab(3);
            home.setEnabled(true);
            closet.setEnabled(true);
            probador.setEnabled(true);
            camara.setEnabled(true);
            reserva.setEnabled(true);
            //this.setVisibility(GONE);
        } else if (v.equals(reserva)) {
            onChangeTab.onChangeTab(4);
            home.setEnabled(true);
            closet.setEnabled(true);
            probador.setEnabled(true);
            camara.setEnabled(true);
            reserva.setEnabled(false);
            /*home.setBackgroundResource(R.drawable.glup_tab_off);
            closet.setBackgroundResource(R.drawable.glup_tab_off);
            probador.setBackgroundResource(R.drawable.glup_tab_off);
            reserva.setBackgroundResource(R.drawable.glup_tab_on);*/
        }
        //activeDefaultTab();
    }

    private void activeDefaultTab() {
        /*Principal context=(Principal)getContext();
        int count= context.getSupportFragmentManager().getBackStackEntryCount();
        Log.e("stackCont",count+"");
        if (count>0){
            String TAG= context.getSupportFragmentManager().getBackStackEntryAt(count-1).getName();
            Log.e("stackFragment",TAG);
        }*/
//        home.setBackgroundResource(R.drawable.glup_tab_off);
//        closet.setBackgroundResource(R.drawable.glup_tab_on);
//        probador.setBackgroundResource(R.drawable.glup_tab_on);
//        camara.setBackgroundResource(R.drawable.glup_tab_on);
    }

    public interface OnChangeTab {
        void onChangeTab(int position);

        void currentTab(int current);
    }

    @Subscribe
    public void updateBlockTab(Principal.ReloadBlockFooter reloadBlockFooter){
        switch (reloadBlockFooter.tag){
            case "FCatalogoNew":
                home.setEnabled(false);
                break;
            case "FCloset":
                closet.setEnabled(false);
                break;
            case "FProbador":
                probador.setEnabled(false);
                break;
            case "FReserva":
                reserva.setEnabled(false);
                break;
        }

    }

    @Subscribe
    public void updateUnlockTab(Principal.ReloadUnLockFooter reloadUnLockFooter){
        switch (reloadUnLockFooter.tag){
            case "FCatalogoNew":
                home.setEnabled(true);
                break;
            case "FCloset":
                closet.setEnabled(true);
                break;
            case "FProbador":
                probador.setEnabled(true);
                break;
            case "FReserva":
                reserva.setEnabled(true);
                break;
        }
    }
}
