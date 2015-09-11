package pe.com.glup.glup;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import pe.com.glup.R;
import pe.com.glup.bus.BusHolder;
import pe.com.glup.datasource.DSUsuario;
import pe.com.glup.fragments.FCatalogo;
import pe.com.glup.fragments.FCloset;
import pe.com.glup.fragments.FMenuLeft;
import pe.com.glup.fragments.FMenuRigth;
import pe.com.glup.fragments.FProbador;
import pe.com.glup.fragments.Fragment_Home;
import pe.com.glup.interfaces.OnSuccessDetalleUsuario;
import pe.com.glup.views.Footer;
import pe.com.glup.views.Header;

public class Principal extends Glup implements Footer.OnChangeTab,FCloset.FragmentIterationListener {

    private boolean flagChangeTab = false;
    private final String[] MESSAGES = {"HOME", "CLOSET", "PROBADOR", "CAMERA"};
    private final Fragment[] FRAGMENTS = {
            FCatalogo.newInstance(),
            FCloset.newInstance(),
            FProbador.newInstance(),
            Fragment_Home.newInstance(MESSAGES[3], MESSAGES[3])
    };
    private static String CURRENT_FRAGMENT_TAG;


    private Footer footer;
    private Header header;
    private OnChangeTab onChangeTab;
    private SlidingMenu menuright;
    private OnSuccessDetalleUsuario onSuccessDetalleUsuario;

    public void setOnChangeTab(OnChangeTab onChangeTab) {
        this.onChangeTab = onChangeTab;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BusHolder.getInstance().register(this);

        setContentView(R.layout.principal);
        setupUI(findViewById(R.id.container_principal));

        footer = (Footer) findViewById(R.id.glutab);
        header = (Header) findViewById(R.id.header);
        header.initView(Principal.this);

        footer.setOnChangeTab(this);
        footer.initView();

        menuright = new SlidingMenu(this);
        menuright.setMode(SlidingMenu.LEFT_RIGHT);
        menuright.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
//        menu.setShadowWidthRes(R.dimen.shadow_width);
//        menu.setShadowDrawable(R.drawable.shadow);
        menuright.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        menuright.setFadeDegree(0.35f);
        menuright.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);

        menuright.setMenu(R.layout.menu_left);
        menuright.setSecondaryMenu(R.layout.menu_right);
        menuright.setSlidingEnabled(false);
        /*
        Temporal
         */

//        getSupportFragmentManager().beginTransaction()
//                .replace(R.id.menu_left, FMenuLeft.newInstance(), FMenuLeft.class.getName())
//                .commit();
//        getSupportFragmentManager().beginTransaction()
//                .replace(R.id.menu_rigth, FMenuRigth.newInstance(), FMenuRigth.class.getName())
//                .commit();
    }

    @Override
    public void onChangeTab(int position) {
        onChangeTab.onChangeTab(position);

        CURRENT_FRAGMENT_TAG = FRAGMENTS[position].getClass().getName().toString();
        Fragment current = getSupportFragmentManager().findFragmentByTag(CURRENT_FRAGMENT_TAG);
        if (current != null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frame_principal, FRAGMENTS[position], CURRENT_FRAGMENT_TAG)
                    .commit();
        } else {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frame_principal, FRAGMENTS[position], CURRENT_FRAGMENT_TAG)
                    .addToBackStack(CURRENT_FRAGMENT_TAG)
                    .commit();
        }

        Log.e("FRAGMENTS", getSupportFragmentManager().getBackStackEntryCount() + "");
        Log.e("FRAGMENTS", CURRENT_FRAGMENT_TAG + "");
        //((ViewGroup) namebar.getParent()).removeView(namebar);
        if (!CURRENT_FRAGMENT_TAG.equals("pe.com.glup.fragments.FProbador")){
            Log.e("!Probador", "deberia cerrarse");
            menuright.setSlidingEnabled(false);
        } else {
            menuright.setSlidingEnabled(true);
        }
        flagChangeTab = true;

    }



    @Override
    public void currentTab(int current) {
        onChangeTab.currentTab(current);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame_principal, FRAGMENTS[current])
                .commit();
    }

    @Override
    public void onFragmentIteration(Bundle parameters) {
        Log.e("Click profile",parameters.getString("datos"));
    }

    public interface OnChangeTab {
        void onChangeTab(int position);

        void currentTab(int current);
    }

    public Header getHeader() {
        return header;
    }

    @Override
    public void onBackPressed() {

        if(getFragmentManager().getBackStackEntryCount() > 0)
            getFragmentManager().popBackStack();
        else
            super.onBackPressed();

        //super.onBackPressed();
    }

    @Override

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

        }
        return super.onKeyDown(keyCode, event);
    }
    public void detectedAfterFragment(){

    }

    }
