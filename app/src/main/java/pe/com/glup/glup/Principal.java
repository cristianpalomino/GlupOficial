package pe.com.glup.glup;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import pe.com.glup.R;
import pe.com.glup.bus.BusHolder;
import pe.com.glup.fragments.FCatalogo;
import pe.com.glup.fragments.FCloset;
import pe.com.glup.fragments.FMenuLeft;
import pe.com.glup.fragments.FMenuRigth;
import pe.com.glup.fragments.FProbador;
import pe.com.glup.fragments.Fragment_Home;
import pe.com.glup.interfaces.OnClickProbador;
import pe.com.glup.interfaces.OnSuccessDetalleUsuario;
import pe.com.glup.interfaces.OnSuccessDisableSliding;
import pe.com.glup.views.Footer;
import pe.com.glup.views.Header;

public class Principal extends Glup implements Footer.OnChangeTab,
        FCloset.FragmentIterationListener {

    private OnSuccessDisableSliding onSuccessDisableSliding;
    private boolean flagChangeTab = false;
    private final String[] MESSAGES = {"HOME", "CLOSET", "PROBADOR","RESERVA", "CAMERA"};
    private final Fragment[] FRAGMENTS = {
            FCatalogo.newInstance(),
            FCloset.newInstance(),
            FProbador.newInstance(),
            Fragment_Home.newInstance(MESSAGES[4], MESSAGES[4])
    };
    private static String CURRENT_FRAGMENT_TAG;


    private Footer footer;
    private Header header;
    private OnChangeTab onChangeTab;
    private SlidingMenu menuright;
    private OnSuccessDetalleUsuario onSuccessDetalleUsuario;

    public void setOnSuccessDisableSliding(OnSuccessDisableSliding onSuccessDisableSliding) {
        this.onSuccessDisableSliding = onSuccessDisableSliding;
    }

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
            //menuright.setSlidingEnabled(false);
            //onSuccessDisableSliding.onSuccessDisableSliding(true);

        } else {

            //menuright.setSlidingEnabled(true);
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


    

}
