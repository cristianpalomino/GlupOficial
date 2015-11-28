package pe.com.glup.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.otto.Subscribe;

import java.util.ArrayList;

import pe.com.glup.R;
import pe.com.glup.adapters.PagerBottomAdapter;
import pe.com.glup.adapters.PagerTopAdapter;
import pe.com.glup.adapters.PrendaAdapterMenu;
import pe.com.glup.models.Prenda;
import pe.com.glup.managers.bus.BusHolder;
import pe.com.glup.network.DSProbador;
import pe.com.glup.dialog.DetailActivity;
import pe.com.glup.dialog.GlupDialog;
import pe.com.glup.dialog.GlupDialogNew;
import pe.com.glup.models.interfaces.OnClickProbador;
import pe.com.glup.models.interfaces.OnSuccessPrendas;
import pe.com.glup.models.interfaces.OnSuccessProbador;
import pe.com.glup.managers.session.Session_Manager;
import pe.com.glup.utils.Util_Fonts;


public class FProbador extends Fragment implements View.OnClickListener,OnSuccessPrendas,OnSuccessProbador{

    private ImageButton superior,medio;
    private ViewPager pagerTop;
    private ViewPager pagerBotton;
    private ArrayList<Prenda> prendasTop;
    private ArrayList<Prenda> prendasBottom;
    private OnClickProbador onClickProbador;
    private PagerTopAdapter pagerTopAdapter;
    private PagerBottomAdapter pagerBottomAdapter;
    private int posCurrentTop,posCurrentBottom;
    protected GlupDialogNew gd;
    private DrawerLayout drawerLayout;
    private TextView titleProbador;

    public static FProbador newInstance() {
        FProbador fragment = new FProbador();
        return fragment;
    }

    public FProbador() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BusHolder.getInstance().register(this);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_probador, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        BusHolder.getInstance().register(this);
        titleProbador = (TextView) getView().findViewById(R.id.title_probador);
        titleProbador.setTypeface(Util_Fonts.setLatoRegular(getActivity()));
        drawerLayout = (DrawerLayout) getView().findViewById(R.id.drawer_layout);
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        drawerLayout.setDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                Log.e("slide", drawerView.getTag().toString() + " offset " + slideOffset);

            }

            @Override
            public void onDrawerOpened(View drawerView) {
                Log.e("Drawerabierto", " " + drawerView.getTag());
                if (drawerView.getTag().toString().equals("Superior")){
                    drawerLayout.bringToFront();
                    ((FrameLayout) getView().findViewById(R.id.menu_left2)).bringToFront();
                }else{
                    drawerLayout.bringToFront();
                    ((FrameLayout) getView().findViewById(R.id.menu_rigth2)).bringToFront();
                }
                drawerLayout.requestLayout();
                changePosButton("Drawerabierto", superior, medio);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                Log.e("Drawercerrado", " " + drawerView.getTag());
                FooterVisible footerVisible=new FooterVisible();
                BusHolder.getInstance().post(footerVisible);
                changePosButton("Drawercerrado", superior, medio);
            }

            @Override
            public void onDrawerStateChanged(int newState) {
                Log.e("state", newState + "");
            }
        });
        /*menuright = new SlidingMenu(getActivity());
        menuright.setMode(SlidingMenu.LEFT_RIGHT);
        menuright.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
//        menu.setShadowWidthRes(R.dimen.shadow_width);
//        menuright.setShadowDrawable(R.drawable.shadow);
        menuright.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        menuright.setFadeDegree(0.35f);
        menuright.attachToActivity(getActivity(), SlidingMenu.SLIDING_CONTENT);
        menuright.setMenu(R.layout.menu_left);
        menuright.setSecondaryMenu(R.layout.menu_right);*/
/*
        String name = getActivity().getSupportFragmentManager().getBackStackEntryAt(getActivity().getSupportFragmentManager().getBackStackEntryCount()-1).getName();
        Fragment fragment=getActivity().getSupportFragmentManager().findFragmentByTag(name);*/
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.menu_left2, FMenuLeft.newInstance(), FMenuLeft.class.getSimpleName())
                .commit();
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.menu_rigth2, FMenuRigth.newInstance(), FMenuRigth.class.getSimpleName())
                .commit();

        superior =(ImageButton) getView().findViewById(R.id.superior);
        medio = (ImageButton) getView().findViewById(R.id.medio);
        if (new Session_Manager(getActivity()).getCurrentUserSexo().equals("H")){
            superior.setImageResource(R.drawable.superiorh_on);
            medio.setImageResource(R.drawable.medioh_on);
        }else {
            superior.setImageResource(R.drawable.superior_on);
            superior.setImageResource(R.drawable.medio_on);
        }
        superior.setOnClickListener(this);
        medio.setOnClickListener(this);


        pagerTop = (ViewPager) getView().findViewById(R.id.scroll_top);
        pagerBotton = (ViewPager) getView().findViewById(R.id.scroll_bottom);

        getView().findViewById(R.id.button_previous_top).setOnClickListener(this);
        getView().findViewById(R.id.button_next_top).setOnClickListener(this);
        getView().findViewById(R.id.button_previous_bottom).setOnClickListener(this);
        getView().findViewById(R.id.button_next_bottom).setOnClickListener(this);

        pagerTop.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (prendasTop.get(position).getTipo().toUpperCase().equals("VESTIDO")) {
                    Log.e("TOP", ((Prenda) pagerTopAdapter.getItem(position)).getCod_prenda() + " " +
                            ((Prenda) pagerTopAdapter.getItem(position)).getIndProbador());
                    pagerBotton.setVisibility(View.GONE);
                    posCurrentTop = position;
                } else {
                    pagerBotton.setVisibility(View.VISIBLE);
                    posCurrentBottom = position;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        DSProbador dsProbadorA = new DSProbador(getActivity());

        try{
            dsProbadorA.setOnSuccessProbador(FProbador.this);
        }catch (ClassCastException c){
            Log.e("errorProb",c.toString());
        }
        dsProbadorA.getGlobalPrendasProbador("A", "1", "20");

        DSProbador dsProbadorB = new DSProbador(getActivity());
        try{
            dsProbadorB.setOnSuccessProbador(FProbador.this);
        }catch (ClassCastException c){
            Log.e("errorProb",c.toString());
        }

        dsProbadorB.getGlobalPrendasProbador("B", "1", "20");

        android.support.v4.app.FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        gd = new GlupDialogNew();
        //gd.setCancelable(false);
        gd.show(fragmentManager,GlupDialog.class.getSimpleName());
    }

    @Override
    public void succesPrendas(DSProbador.ResponseCatalogo responseCatalogo) {
       Log.e(null, "ya no deberia mostrar esto");
        /*if success ==1
            for (int position=0;position<prendasTop.size();position++){
                if (prendasTop.get(position).getTipo().toUpperCase().equals("VESTIDO")) {
                    Log.e("TOP", ((Prenda) pagerTopAdapter.getItem(position)).getCod_prenda() + " " +
                            ((Prenda) pagerTopAdapter.getItem(position)).getIndProbador());
                    pagerBotton.setVisibility(View.GONE);
                    posCurrentTop = position;
                } else {
                    pagerBotton.setVisibility(View.VISIBLE);
                    posCurrentBottom = position;
                }
            }*/

    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.superior:
                Log.e(null, "superior");
                FooterGone footerGone=new FooterGone();
                BusHolder.getInstance().post(footerGone);
                drawerLayout.openDrawer(GravityCompat.START);
                //menuright.toggle();
                //drawerLayout.openDrawer(GravityCompat.START);
                break;
            case R.id.medio:
                Log.e(null, "medio");
                FooterGone footerGone1=new FooterGone();
                BusHolder.getInstance().post(footerGone1);
                drawerLayout.openDrawer(GravityCompat.END);
                //menuright.showSecondaryMenu(true);
                break;
            case R.id.button_previous_top:
                Log.e(null, "previos_top");
                if (pagerTop!=null && pagerTop.getAdapter()!=null)
                    previousPageTop();
                break;
            case R.id.button_next_top:
                Log.e(null, "next_top");
                if (pagerTop!=null && pagerTop.getAdapter()!=null)
                    nextPageTop();
                break;
            case R.id.button_previous_bottom:
                Log.e(null, "previos_bottom");
                if (pagerBotton!=null && pagerBotton.getAdapter()!=null)
                    previousPageBottom();
                break;
            case R.id.button_next_bottom:
                Log.e(null,"next_bottom");
                if (pagerBotton!=null)
                    nextPageBottom();
                break;
        }
    }

    private void nextPageTop() {
        int currentPage = pagerTop.getCurrentItem();
        int totalPages = pagerTop.getAdapter().getCount();

        int nextPage = currentPage+1;
        if (nextPage >= totalPages) {
            // We can't go forward anymore.
            // Loop to the first page. If you don't want looping just
            // return here.
            nextPage = 0;
        }

        pagerTop.setCurrentItem(nextPage, true);
    }

    private void previousPageTop() {
        int currentPage = pagerTop.getCurrentItem();
        int totalPages = pagerTop.getAdapter().getCount();

        int previousPage = currentPage-1;
        if (previousPage < 0) {
            // We can't go back anymore.
            // Loop to the last page. If you don't want looping just
            // return here.
            previousPage = totalPages - 1;
        }

        pagerTop.setCurrentItem(previousPage, true);
    }

    private void nextPageBottom() {
        int currentPage = pagerBotton.getCurrentItem();
        int totalPages = pagerBotton.getAdapter().getCount();

        int nextPage = currentPage+1;
        if (nextPage >= totalPages) {
            // We can't go forward anymore.
            // Loop to the first page. If you don't want looping just
            // return here.
            nextPage = 0;
        }

        pagerBotton.setCurrentItem(nextPage, true);
    }

    private void previousPageBottom() {
        int currentPage = pagerBotton.getCurrentItem();
        int totalPages = pagerBotton.getAdapter().getCount();

        int previousPage = currentPage-1;
        if (previousPage < 0) {
            // We can't go back anymore.
            // Loop to the last page. If you don't want looping just
            // return here.
            previousPage = totalPages - 1;
        }

        pagerBotton.setCurrentItem(previousPage, true);
    }

    @Subscribe
    public void getIndProbador(String indProb) {
        Log.e("enFProbador", indProb);

    }
    @Subscribe
    public  void getReloadPrendas(PrendaAdapterMenu.Holder holder ){
        Log.e("check", String.valueOf(holder.corazon.isChecked()));
        DSProbador dsProbadorA = new DSProbador(getActivity());

        try{
            dsProbadorA.setOnSuccessProbador(FProbador.this);
        }catch (ClassCastException c){
            Log.e("errorProb",c.toString());
        }
        dsProbadorA.getGlobalPrendasProbador("A", "1", "20");

        DSProbador dsProbadorB = new DSProbador(getActivity());
        try{
            dsProbadorB.setOnSuccessProbador(FProbador.this);
        }catch (ClassCastException c){
            Log.e("errorProb",c.toString());
        }

        dsProbadorB.getGlobalPrendasProbador("B", "1", "20");

    }

    @Subscribe
    public void addProbador(Prenda prenda){
        Log.e("codProbador",prenda.getCod_prenda());
        Log.e("filtroPos", prenda.getFiltroPosicion());
        if (prenda.getFiltroPosicion().equals("A")){
            this.prendasTop.add(prenda);
            pagerTopAdapter.notifyDataSetChanged();
        }else {
            this.prendasBottom.add(prenda);
            pagerBottomAdapter.notifyDataSetChanged();
        }
    }

    @Subscribe
    public void addReservaTop(PagerTopAdapter.SuccessTopLongClick successLongClick){
       Log.e(null, successLongClick.tag + " " + successLongClick.succcess+ " "+successLongClick.codigo_prenda);
        Intent intent = new Intent(getActivity().getApplicationContext(),DetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("codigoPrenda", successLongClick.codigo_prenda);
        intent.putExtras(bundle);
        startActivity(intent);

    }

    @Subscribe
    public void addReservaBottom(PagerBottomAdapter.SuccessBottomLongClick successLongClick){
        Log.e(null, successLongClick.tag + " " + successLongClick.succcess+ " "+successLongClick.codigo_prenda);
        Intent intent = new Intent(getActivity().getApplicationContext(),DetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("codigoPrenda", successLongClick.codigo_prenda);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void succesPrendas(DSProbador.ResponseProbador responseCatalogo) {
        Log.e(null,"Recargando prendas para probador ..."+ responseCatalogo.success);
        gd.dismiss();
        if (responseCatalogo.tipo.equals("A"))
        {   Log.e(null, responseCatalogo.toString());
            this.prendasTop = new ArrayList<Prenda>();
            for (int i=0;i< responseCatalogo.prendas.size();i++){
                if (responseCatalogo.prendas.get(i).getIndProbador().equals("1")){
                    this.prendasTop.add(responseCatalogo.prendas.get(i));
                }
            }
            pagerTopAdapter = new PagerTopAdapter(getActivity(), this.prendasTop);
            pagerTop.setAdapter(pagerTopAdapter);


        } else if (responseCatalogo.tipo.equals("B"))
        {
            this.prendasBottom = new ArrayList<Prenda>();
            for (int i=0;i< responseCatalogo.prendas.size();i++){
                if (responseCatalogo.prendas.get(i).getIndProbador().equals("1")){
                    this.prendasBottom.add(responseCatalogo.prendas.get(i));
                }
            }
            pagerBottomAdapter = new PagerBottomAdapter(getActivity(), this.prendasBottom);
            pagerBotton.setAdapter(pagerBottomAdapter);
        }
        if (responseCatalogo.success==1 && prendasTop!=null){
            for (int position=0;position<prendasTop.size();position++){
                if (prendasTop.get(position).getTipo().toUpperCase().equals("VESTIDO")) {
                    Log.e("TOP", ((Prenda) pagerTopAdapter.getItem(position)).getCod_prenda() + " " +
                            ((Prenda) pagerTopAdapter.getItem(position)).getIndProbador());
                    pagerBotton.setVisibility(View.GONE);
                    posCurrentTop = position;
                } else {
                    pagerBotton.setVisibility(View.VISIBLE);
                    posCurrentBottom = position;
                }
            }
        }
    }

    public class FooterVisible{}
    public class FooterGone{}

    private void changePosButton(String s, ImageButton izq, ImageButton der) {
        if (s.equals("Drawerabierto")) {
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)izq.getLayoutParams();
            params.addRule(RelativeLayout.ALIGN_PARENT_LEFT, 0);
            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, 1);
            izq.setLayoutParams(params);
            RelativeLayout.LayoutParams params1 = (RelativeLayout.LayoutParams)der.getLayoutParams();
            params1.addRule(RelativeLayout.ALIGN_PARENT_LEFT,1);
            params1.addRule(RelativeLayout.ALIGN_PARENT_RIGHT,0);
            der.setLayoutParams(params1);
        } else {
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)izq.getLayoutParams();
            params.addRule(RelativeLayout.ALIGN_PARENT_LEFT,1);
            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, 0);
            izq.setLayoutParams(params);
            RelativeLayout.LayoutParams params1 = (RelativeLayout.LayoutParams)der.getLayoutParams();
            params1.addRule(RelativeLayout.ALIGN_PARENT_RIGHT,1);
            params1.addRule(RelativeLayout.ALIGN_PARENT_LEFT,0);
            der.setLayoutParams(params1);
        }
    }
}
