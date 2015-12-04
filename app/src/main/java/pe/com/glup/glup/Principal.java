package pe.com.glup.glup;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.View;

import com.squareup.otto.Subscribe;

import android.widget.Button;
import android.widget.FrameLayout;

import java.util.ArrayList;

import pe.com.glup.R;
import pe.com.glup.fragments.FClosetNew;
import pe.com.glup.fragments.FClosetProfileNew;
import pe.com.glup.managers.bus.BusHolder;
import pe.com.glup.fragments.FCamera;
import pe.com.glup.fragments.FCatalogoNew;
import pe.com.glup.fragments.FMenuLeft;
import pe.com.glup.fragments.FMenuRigth;
import pe.com.glup.fragments.FProbador;
import pe.com.glup.fragments.FReserva;
import pe.com.glup.managers.session.Session_Manager;
import pe.com.glup.models.Prenda;
import pe.com.glup.models.interfaces.OnSuccessDetalleUsuario;
import pe.com.glup.models.interfaces.OnSuccessDisableSliding;
import pe.com.glup.views.Footer;
import pe.com.glup.views.Header;

public class Principal extends Glup implements Footer.OnChangeTab {

    private OnSuccessDisableSliding onSuccessDisableSliding;
    private boolean flagChangeTab = false;
    private final String[] MESSAGES = {"HOME", "CLOSET", "PROBADOR", "CAMERA", "RESERVA"};
    private final Fragment[] FRAGMENTS = {
            FCatalogoNew.newInstance(),
            FClosetNew.newInstance(),
            FProbador.newInstance(),
            FCamera.newInstance(), FReserva.newInstance()
    };
    private static String CURRENT_FRAGMENT_TAG;
    private Fragment current;
    private FrameLayout framePrincipal;
    private Button prueba;

    private Footer footer;
    private Header header;
    private OnChangeTab onChangeTab;
    private OnSuccessDetalleUsuario onSuccessDetalleUsuario;
    private int position;
    private DrawerLayout drawerLayout;
    private ArrayList<Prenda> aPrendas;
    private Session_Manager session_manager;
    private Glup glup;

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
        glup=(Glup)this;
        setContentView(R.layout.principal);
        //setupUI(findViewById(R.id.drawer_layout));
        session_manager= new Session_Manager(this);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

        prueba = (Button) findViewById(R.id.clic);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.menu_left1, FMenuLeft.newInstance(), FMenuLeft.class.getSimpleName())
                .commit();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.menu_rigth1, FMenuRigth.newInstance(), FMenuRigth.class.getSimpleName())
                .commit();
        prueba.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);

            }
        });
        drawerLayout.setDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                Log.e("slide", drawerView.getTag().toString() + " offset " + slideOffset);

            }

            @Override
            public void onDrawerOpened(View drawerView) {
                Log.e("Drawerabierto", " " + drawerView.getTag());
                ((FrameLayout) findViewById(R.id.menu_left1)).bringToFront();
                drawerLayout.requestLayout();
                //changePosButton("Drawerabierto", superior, medio);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                Log.e("Drawercerrado", " " + drawerView.getTag());
                //changePosButton("Drawercerrado", superior, medio);
            }

            @Override
            public void onDrawerStateChanged(int newState) {
                Log.e("state", newState + "");
            }
        });
        framePrincipal = (FrameLayout) findViewById(R.id.frame_principal);
        footer = (Footer) findViewById(R.id.glutab);
        header = (Header) findViewById(R.id.header);
        header.initView(Principal.this);

        footer.setOnChangeTab(this);
        footer.initView();

        CURRENT_FRAGMENT_TAG = FCatalogoNew.class.getSimpleName();
        current = FCatalogoNew.newInstance();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.frame_principal, current, CURRENT_FRAGMENT_TAG)
                .addToBackStack(CURRENT_FRAGMENT_TAG)
                .commit();
        framePrincipal.setPadding(0, 0, 0, (int) convertDpToPixel(60, this));
        Log.e("FRAGMENTS", CURRENT_FRAGMENT_TAG + "");
        //getIntent().putExtra("numberPage", 1);
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
        Log.e("position", position + "");
        this.position = position;
        Log.e("FRAGMENTS", getSupportFragmentManager().getBackStackEntryCount() + "");
        CURRENT_FRAGMENT_TAG = FRAGMENTS[position].getClass().getSimpleName();
        current = getSupportFragmentManager().findFragmentByTag(CURRENT_FRAGMENT_TAG);
        if (current != null) {
            Log.e(null, "currenT not null" + current.toString());
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frame_principal, FRAGMENTS[position], CURRENT_FRAGMENT_TAG)
                    .addToBackStack(CURRENT_FRAGMENT_TAG)
                    .commit();
        } else {
            Log.e(null, "current null");
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frame_principal, FRAGMENTS[position], CURRENT_FRAGMENT_TAG)
                    .addToBackStack(CURRENT_FRAGMENT_TAG)
                    .commit();
        }

        Log.e("FRAGMENTS", getSupportFragmentManager().getBackStackEntryCount() + "");
        Log.e("FRAGMENTS", CURRENT_FRAGMENT_TAG + "");
        //((ViewGroup) namebar.getParent()).removeView(namebar);
        if (!CURRENT_FRAGMENT_TAG.equals("FProbador")) {
            Log.e("!Probador", "deberia cerrarsel los sliders");
            //menuright.setSlidingEnabled(false);
            //onSuccessDisableSliding.onSuccessDisableSliding(true);

        } else {

            //menuright.setSlidingEnabled(true);
        }
        if (CURRENT_FRAGMENT_TAG.equals("FCamera")) {
            footer.setVisibility(View.GONE);
            framePrincipal.setPadding(0, 0, 0, 0);
            /*LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            framePrincipal.setLayoutParams(lp);*/
        } else {
            footer.setVisibility(View.VISIBLE);
            framePrincipal.setPadding(0, 0, 0, (int) convertDpToPixel(60, this));
            Log.e("Margin", (int) convertDpToPixel(60, this) + "");
            /*LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            lp.setMargins(0, 0, 0,(int) convertDpToPixel(60,this));

            framePrincipal.setLayoutParams(lp);*/
        }

        if (CURRENT_FRAGMENT_TAG.equals("FCatalogoNew")) {
            ArrayList<Prenda> inicializacion = new ArrayList<Prenda>();
            glup.setPrendas(inicializacion);
            glup.setPrendasHombre(inicializacion);
            glup.setPrendasMujer(inicializacion);
            //ResponseUpdateGeneroCatalogo responseUpdateGeneroCatalogo = new ResponseUpdateGeneroCatalogo();
            //responseUpdateGeneroCatalogo.success=1;
            //BusHolder.getInstance().post(responseUpdateGeneroCatalogo);
            session_manager.setFlagReload(true);
            session_manager.setTotalPrendTodos(0);
            session_manager.setTotalPrendGenm(0);
            session_manager.setTotalPrendGenh(0);
            session_manager.setNumPages(1);
            session_manager.setNumPagesHombre(1);
            session_manager.setNumPagesMujer(1);
        }else {
            session_manager.setFlagReload(false);

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

    public interface OnChangeTab {
        void onChangeTab(int position);

        void currentTab(int current);
    }

    public Header getHeader() {
        return header;
    }

    @Override
    public void onBackPressed() {

        int count = getSupportFragmentManager().getBackStackEntryCount();
        if (count > 0) {
            Log.e("null", "entro a back pila");
            getSupportFragmentManager().popBackStack();
            //getSupportFragmentManager().popBackStackImmediate();
            Log.e("ULTIMO", getSupportFragmentManager().getBackStackEntryAt(count - 1).getName());
            String ULTI_FRAGMENT_TAG = getSupportFragmentManager().getBackStackEntryAt(count - 1).getName();
            if (count - 2 >= 0) {
                Log.e("PENULTIMO", getSupportFragmentManager().getBackStackEntryAt(count - 2).getName());
                String LAST_FRAGMENT_TAG = getSupportFragmentManager().getBackStackEntryAt(count - 2).getName();
                int flag = 1;
                /*switch(ULTI_FRAGMENT_TAG){
                    case "FCatalogoNew":
                        ReloadUnLockFooter reloadUnLockFooter = new ReloadUnLockFooter();
                        reloadUnLockFooter.tag=ULTI_FRAGMENT_TAG;
                        BusHolder.getInstance().post(reloadUnLockFooter);
                        chekLastFragment(LAST_FRAGMENT_TAG);
                        break;
                    case "FClosetNew":
                        ReloadUnLockFooter reloadUnLockFooter1 = new ReloadUnLockFooter();
                        reloadUnLockFooter1.tag =ULTI_FRAGMENT_TAG;
                        BusHolder.getInstance().post(reloadUnLockFooter1);
                        chekLastFragment(LAST_FRAGMENT_TAG);
                        break;
                    case "FProbador":
                        ReloadUnLockFooter reloadUnLockFooter2= new ReloadUnLockFooter();
                        reloadUnLockFooter2.tag=ULTI_FRAGMENT_TAG;
                        BusHolder.getInstance().post(reloadUnLockFooter2);
                        chekLastFragment(LAST_FRAGMENT_TAG);
                        break;
                    case "FReserva":
                        ReloadUnLockFooter reloadUnLockFooter3 = new ReloadUnLockFooter();
                        reloadUnLockFooter3.tag=ULTI_FRAGMENT_TAG;
                        BusHolder.getInstance().post(reloadUnLockFooter3);
                        chekLastFragment(LAST_FRAGMENT_TAG);
                        break;
                    default:Log.e("Entro","defaul");chekLastFragment(LAST_FRAGMENT_TAG);break;
                }*/
                ReloadUnLockFooter reloadUnLockFooter = new ReloadUnLockFooter();
                reloadUnLockFooter.tag = ULTI_FRAGMENT_TAG;
                BusHolder.getInstance().post(reloadUnLockFooter);
                chekLastFragment(LAST_FRAGMENT_TAG);

                Log.e("name", LAST_FRAGMENT_TAG);
                if (!LAST_FRAGMENT_TAG.equals("FCamera")) {
                    footer.setVisibility(View.VISIBLE);
                    footer.invalidate();
                    framePrincipal.setPadding(0, 0, 0, (int) convertDpToPixel(60, this));
                    /*LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                    lp.setMargins(0, 0, 0,(int) convertDpToPixel(60,this));
                    Log.e("Margin", (int) convertDpToPixel(60, this) + "");
                    framePrincipal.setLayoutParams(lp);*/
                } else {
                    footer.setVisibility(View.GONE);
                    footer.invalidate();
                    framePrincipal.setPadding(0, 0, 0, 0);
                    /*LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                    framePrincipal.setLayoutParams(lp);*/
                }
                /*if (getSupportFragmentManager().getBackStackEntryAt(count-1).getName().equals("FClosetProfile")){
                    flag=0;
                    ButtonUpdateProfile buttonUpdateProfile= new ButtonUpdateProfile();
                    buttonUpdateProfile.flag=flag;
                    BusHolder.getInstance().post(buttonUpdateProfile);
                }*/
                if (LAST_FRAGMENT_TAG.equals("FCatalogoNew")) {
                    ArrayList<Prenda> inicializacion = new ArrayList<Prenda>();
                    glup.setPrendas(inicializacion);
                    glup.setPrendasHombre(inicializacion);
                    glup.setPrendasMujer(inicializacion);
                    /*SignalUploadPrendas signalUploadPrendas = new SignalUploadPrendas();
                    BusHolder.getInstance().post(signalUploadPrendas);*/
                    session_manager.setFlagReload(true);
                    session_manager.setTotalPrendTodos(0);
                    session_manager.setTotalPrendGenm(0);
                    session_manager.setTotalPrendGenh(0);
                    session_manager.setNumPages(1);
                    session_manager.setNumPagesHombre(1);
                    session_manager.setNumPagesMujer(1);
                }

            }
            if (count == 1 && ULTI_FRAGMENT_TAG.equals("FCatalogoNew")) {
                ArrayList<Prenda> inicializacion = new ArrayList<Prenda>();
                glup.setPrendas(inicializacion);
                glup.setPrendasHombre(inicializacion);
                glup.setPrendasMujer(inicializacion);

                session_manager.setFlagReload(true);
                session_manager.setTotalPrendTodos(0);
                session_manager.setTotalPrendGenm(0);
                session_manager.setTotalPrendGenh(0);
                session_manager.setNumPages(1);
                session_manager.setNumPagesHombre(1);
                session_manager.setNumPagesMujer(1);
                this.finish();
            }


        } else {
            /*Cuando navega por el menu footer muy rapido y no carga algunos componentes*/
           /*Fragment current = getSupportFragmentManager().findFragmentByTag(CURRENT_FRAGMENT_TAG);

            if (current==null){
                Log.e("enBack","current null");
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.frame_principal, FRAGMENTS[position], CURRENT_FRAGMENT_TAG)
                        .addToBackStack(CURRENT_FRAGMENT_TAG)
                        .commit();
                if (CURRENT_FRAGMENT_TAG.equals("FCamera")){
                    footer.setVisibility(View.GONE);
                }else {
                    footer.setVisibility(View.VISIBLE);
                }
            } else {

                Log.e("null","entro a super back");
                super.onBackPressed();
            }*/
            super.onBackPressed();//
        }


        //super.onBackPressed();
    }

    public class ResponseUpdateGeneroCatalogo {
        public int success = 0;
    }

    public class ButtonUpdateProfile {
        public int flag = 1;
    }

    public float convertDpToPixel(float dp, Context context) {
        float px = dp * (context.getResources().getDisplayMetrics().densityDpi / 160);
        return px;
    }

    public class ReloadBlockFooter {
        public String tag;
    }

    public class ReloadUnLockFooter {
        public String tag;
    }

    public void chekLastFragment(String tag) {
        switch (tag) {
            case "FCatalogoNew":
                ReloadBlockFooter reloadBlockFooter = new ReloadBlockFooter();
                reloadBlockFooter.tag = tag;
                BusHolder.getInstance().post(reloadBlockFooter);
                break;
            case "FClosetNew":
                Log.e("entro", "FClosetNew");
                ReloadBlockFooter reloadBlockFooter1 = new ReloadBlockFooter();
                reloadBlockFooter1.tag = tag;
                BusHolder.getInstance().post(reloadBlockFooter1);
                break;
            case "FProbador":
                ReloadBlockFooter reloadBlockFooter2 = new ReloadBlockFooter();
                reloadBlockFooter2.tag = tag;
                BusHolder.getInstance().post(reloadBlockFooter2);
                break;
            case "FReserva":
                ReloadBlockFooter reloadBlockFooter3 = new ReloadBlockFooter();
                reloadBlockFooter3.tag = tag;
                BusHolder.getInstance().post(reloadBlockFooter3);
                break;
        }
    }


    @Subscribe
    public void fromProbadorVisibleFooter(FProbador.FooterVisible footerVisible) {
        footer.setVisibility(View.VISIBLE);
        footer.invalidate();
        framePrincipal.setPadding(0, 0, 0, (int) convertDpToPixel(60, this));
    }

    @Subscribe
    public void fromProbadorGoneFooter(FProbador.FooterGone footerGone) {
        footer.setVisibility(View.GONE);
        footer.invalidate();
        framePrincipal.setPadding(0, 0, 0, 0);
    }

    @Subscribe
    public void openProfile(FClosetNew.OpenProfile openProfile) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame_principal, FClosetProfileNew.newInstance(), FClosetProfileNew.class.getSimpleName());
        fragmentTransaction.addToBackStack(FClosetProfileNew.class.getSimpleName());
        fragmentTransaction.commit();
    }

    @Subscribe
    public void setListPrendas(DetalleNew.UploadPrendas uploadPrendas) {
        //NO FUNCIONA AL RETROCEDE SE VUELVE NULL
        //aPrendas = uploadPrendas.listPrendas;
        //Log.e("TamañoA", aPrendas.size() + "");
    }

    public class SignalUploadPrendas {
        public ArrayList<Prenda> listPrendas;public int numberPag;
    }

    ;
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
            if (resultCode == RESULT_OK) {
                ArrayList<Prenda> stredittext = (ArrayList<Prenda>) data.getExtras().get("listPrendas");
                Log.e("updateLisPren", stredittext.size() + "");
                SignalUploadPrendas signalUploadPrendas=new SignalUploadPrendas();
                signalUploadPrendas.listPrendas=stredittext;
                signalUploadPrendas.numberPag= (int) data.getExtras().get("numberPage");
                BusHolder.getInstance().post(signalUploadPrendas);
            }
    }

    @Override
    public void onPause(){
        ArrayList<Prenda> inicializacion = new ArrayList<Prenda>();
        glup.setPrendas(inicializacion);
        glup.setPrendasHombre(inicializacion);
        glup.setPrendasMujer(inicializacion);

        session_manager.setFlagReload(true);
        session_manager.setTotalPrendTodos(0);
        session_manager.setTotalPrendGenm(0);
        session_manager.setTotalPrendGenh(0);
        session_manager.setNumPages(1);
        session_manager.setNumPagesHombre(1);
        session_manager.setNumPagesMujer(1);

        super.onPause();
    }
}