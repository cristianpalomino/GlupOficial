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
import android.widget.FrameLayout;
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
    private FrameLayout frameHome,frameCloset,frameProbador,frameCamara,frameReserva;

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

        frameHome = (FrameLayout) findViewById(R.id.frameHome);
        frameCloset = (FrameLayout) findViewById(R.id.frameCloset);
        frameProbador = (FrameLayout) findViewById(R.id.frameProbador);
        frameCamara  = (FrameLayout) findViewById(R.id.frameCamara);
        frameReserva = (FrameLayout) findViewById(R.id.frameReserva);

        frameHome.setOnClickListener(this);
        frameCloset.setOnClickListener(this);
        frameProbador.setOnClickListener(this);
        frameCamara.setOnClickListener(this);
        frameReserva.setOnClickListener(this);

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
        if (v.equals(home) || v.equals(frameHome)) {
            onChangeTab.onChangeTab(0);
            home.setEnabled(false);
            closet.setEnabled(true);
            probador.setEnabled(true);
            camara.setEnabled(true);
            reserva.setEnabled(true);
            home.setImageResource(R.drawable.catalogo_on);
            closet.setImageResource(R.drawable.closet_off);
            camara.setImageResource(R.drawable.camara_off);
            probador.setImageResource(R.drawable.probador_off);
            reserva.setImageResource(R.drawable.reserva_off);
        } else if (v.equals(closet) || v.equals(frameCloset)) {
            onChangeTab.onChangeTab(1);
            home.setEnabled(true);
            closet.setEnabled(false);
            probador.setEnabled(true);
            camara.setEnabled(true);
            reserva.setEnabled(true);
            home.setImageResource(R.drawable.catalogo_off);
            closet.setImageResource(R.drawable.closet_on);
            camara.setImageResource(R.drawable.camara_off);
            probador.setImageResource(R.drawable.probador_off);
            reserva.setImageResource(R.drawable.reserva_off);
        } else if (v.equals(probador) || v.equals(frameProbador)) {
            onChangeTab.onChangeTab(2);
            home.setEnabled(true);
            closet.setEnabled(true);
            probador.setEnabled(false);
            camara.setEnabled(true);
            reserva.setEnabled(true);
            home.setImageResource(R.drawable.catalogo_off);
            closet.setImageResource(R.drawable.closet_off);
            camara.setImageResource(R.drawable.camara_off);
            probador.setImageResource(R.drawable.probador_on);
            reserva.setImageResource(R.drawable.reserva_off);
        } else if (v.equals(camara) || v.equals(frameCamara)){
            onChangeTab.onChangeTab(3);
            home.setEnabled(true);
            closet.setEnabled(true);
            probador.setEnabled(true);
            camara.setEnabled(true);
            reserva.setEnabled(true);
            home.setImageResource(R.drawable.catalogo_off);
            closet.setImageResource(R.drawable.closet_off);
            camara.setImageResource(R.drawable.camara_on);
            probador.setImageResource(R.drawable.probador_off);
            reserva.setImageResource(R.drawable.reserva_off);
            //this.setVisibility(GONE);
        } else if (v.equals(reserva) || v.equals(frameReserva)) {
            onChangeTab.onChangeTab(4);
            home.setEnabled(true);
            closet.setEnabled(true);
            probador.setEnabled(true);
            camara.setEnabled(true);
            reserva.setEnabled(false);
            home.setImageResource(R.drawable.catalogo_off);
            closet.setImageResource(R.drawable.closet_off);
            camara.setImageResource(R.drawable.camara_off);
            probador.setImageResource(R.drawable.probador_off);
            reserva.setImageResource(R.drawable.reserva_on);
            /*home.setImageResource(R.drawable.glup_tab_off);
            closet.setImageResource(R.drawable.glup_tab_off);
            probador.setImageResource(R.drawable.glup_tab_off);
            reserva.setImageResource(R.drawable.glup_tab_on);*/
        }
        //activeDefaultTab();
    }

    private void activeDefaultTab() {
        home.setImageResource(R.drawable.catalogo_on);
        /*Principal context=(Principal)getContext();
        int count= context.getSupportFragmentManager().getBackStackEntryCount();
        Log.e("stackCont",count+"");
        if (count>0){
            String TAG= context.getSupportFragmentManager().getBackStackEntryAt(count-1).getName();
            Log.e("stackFragment",TAG);
        }*/
//        home.setImageResource(R.drawable.glup_tab_off);
//        closet.setImageResource(R.drawable.glup_tab_on);
//        probador.setImageResource(R.drawable.glup_tab_on);
//        camara.setImageResource(R.drawable.glup_tab_on);
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
                home.setImageResource(R.drawable.catalogo_on);
                closet.setImageResource(R.drawable.closet_off);
                camara.setImageResource(R.drawable.camara_off);
                probador.setImageResource(R.drawable.probador_off);
                reserva.setImageResource(R.drawable.reserva_off);
                break;
            case "FCloset":
                closet.setEnabled(false);
                home.setImageResource(R.drawable.catalogo_off);
                closet.setImageResource(R.drawable.closet_on);
                camara.setImageResource(R.drawable.camara_off);
                probador.setImageResource(R.drawable.probador_off);
                reserva.setImageResource(R.drawable.reserva_off);
                break;
            case "FProbador":
                probador.setEnabled(false);
                home.setImageResource(R.drawable.catalogo_off);
                closet.setImageResource(R.drawable.closet_off);
                camara.setImageResource(R.drawable.camara_off);
                probador.setImageResource(R.drawable.probador_on);
                reserva.setImageResource(R.drawable.reserva_off);
                break;
            case "FReserva":
                reserva.setEnabled(false);
                home.setImageResource(R.drawable.catalogo_off);
                closet.setImageResource(R.drawable.closet_off);
                camara.setImageResource(R.drawable.camara_off);
                probador.setImageResource(R.drawable.probador_off);
                reserva.setImageResource(R.drawable.reserva_on);
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
