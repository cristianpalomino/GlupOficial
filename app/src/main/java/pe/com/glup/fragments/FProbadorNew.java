package pe.com.glup.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.IllegalFormatCodePointException;
import java.util.Iterator;

import pe.com.glup.R;
import pe.com.glup.adapters.PagerBottomAdapter;
import pe.com.glup.adapters.PagerTopAdapter;
import pe.com.glup.adapters.PrendaAdapterMenu;
import pe.com.glup.dialog.DetailActivity;
import pe.com.glup.dialog.GlupDialogNew;
import pe.com.glup.glup.Principal;
import pe.com.glup.managers.bus.BusHolder;
import pe.com.glup.managers.session.Session_Manager;
import pe.com.glup.models.Prenda;
import pe.com.glup.network.DSProbadorNew;
import pe.com.glup.utils.FastBlur;
import pe.com.glup.utils.Util_Fonts;

/**
 * Created by Glup on 10/12/15.
 */
public class FProbadorNew extends Fragment implements View.OnClickListener,
    DrawerLayout.DrawerListener{
    private TextView titleProbador;
    private ImageView superior,medio,empty;
    private ViewPager pagerTop,pagerBotton;
    private RelativeLayout frameHead,frameTop,frameBottom,framePreviousTop,frameNextTop,framePreviousBottom,frameNextBottom,emptyView;
    private LinearLayout frameProbador;
    private DrawerLayout drawerLayout;
    private boolean conexionTop,conexionBottom,hayPrendaTop,hayPrendaBottom,vestido,reloadTop,reloadBot,addTop,delTop,addBot,delBot;
    private DSProbadorNew dsProbadorNew;
    private PagerTopAdapter pagerTopAdapter=null;
    private PagerBottomAdapter pagerBottomAdapter=null;
    private ArrayList<Prenda> prendasTop=null,prendasBottom=null;
    private GlupDialogNew gd;
    private int numPagTop,numPageBottom=9,rangInfTop=9,rangSupTop,rangInfBottom,rangSupBottom;
    private  int positionTop;
    public static FProbadorNew newInstance(){
        FProbadorNew fragment = new FProbadorNew();
        return fragment;
    }
    @Override
    public void onCreate(Bundle saveInstance){super.onCreate(saveInstance);}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstance){
        return inflater.inflate(R.layout.fragment_probador,container,false);
    }
    @Override
    public void onActivityCreated(Bundle savedInstance){
        super.onActivityCreated(savedInstance);
        BusHolder.getInstance().register(this);
        frameProbador = (LinearLayout) getView().findViewById(R.id.frame_probador);
        titleProbador = (TextView) getView().findViewById(R.id.title_probador);
        titleProbador.setTypeface(Util_Fonts.setLatoRegular(getActivity()));
        //btns sliders
        superior = (ImageView) getView().findViewById(R.id.superior);
        medio = (ImageView) getView().findViewById(R.id.medio);
        initSliderButtons();
        superior.setOnClickListener(this);
        medio.setOnClickListener(this);
        //pagers init
        frameTop = (RelativeLayout) getView().findViewById(R.id.frame_top);
        frameBottom = (RelativeLayout) getView().findViewById(R.id.frame_bottom);
        framePreviousTop = (RelativeLayout) getView().findViewById(R.id.button_previous_top);
        frameNextTop = (RelativeLayout) getView().findViewById(R.id.button_next_top);
        framePreviousBottom = (RelativeLayout) getView().findViewById(R.id.button_previous_bottom);
        frameNextBottom = (RelativeLayout) getView().findViewById(R.id.button_next_bottom);
        pagerTop = (ViewPager) getView().findViewById(R.id.scroll_top);
        pagerBotton = (ViewPager) getView().findViewById(R.id.scroll_bottom);

        framePreviousTop.setOnClickListener(this);
        frameNextTop.setOnClickListener(this);
        framePreviousBottom.setOnClickListener(this);
        frameNextBottom.setOnClickListener(this);
        //DrawerLayout
        drawerLayout = (DrawerLayout) getView().findViewById(R.id.drawer_layout);
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        drawerLayout.setDrawerListener(this);

        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.menu_left2, FMenuLeft.newInstance(), FMenuLeft.class.getSimpleName())
                .commit();
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.menu_rigth2, FMenuRigth.newInstance(), FMenuRigth.class.getSimpleName())
                .commit();
        //call services prendas top
        conexionTop=false;conexionTop=false;
        reloadBot=false;reloadTop=false;
        hayPrendaTop=false;hayPrendaBottom=false;vestido=false;
        numPagTop=0;numPageBottom=0;rangSupTop=0;rangSupBottom=0;
        addTop=false;addBot=false;delBot=false;delTop=false;
        positionTop=0;
        dsProbadorNew = new DSProbadorNew(getActivity());
        dsProbadorNew.getGlobalPrendasProbador("A",numPagTop+1,"10");
        //empty view
        emptyView=(RelativeLayout) getView().findViewById(R.id.empty_view_probador);
        empty= (ImageView) getView().findViewById(R.id.image_empty);
        empty.setImageResource(R.drawable.vista_probador);
        //pager listener

            pagerTop.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }
                @Override
                public void onPageSelected(int position) {
                    for (int i=0;i<prendasTop.size();i++){
                        Log.e("CodPrendas:",prendasTop.get(i).getCod_prenda());
                    }
                    //verificar si llego a la prenda 10
                        if (reloadTop && position==rangInfTop && prendasTop!=null && position!=rangSupTop){//calcular el maximo de probador para no cargar
                            Log.e("position9:", position + "");
                            dsProbadorNew.getGlobalPrendasProbador("A", numPagTop + 1, "10");
                            gd = new GlupDialogNew();
                            gd.setCancelable(false);
                            gd.show(getActivity().getSupportFragmentManager(), GlupDialogNew.class.getSimpleName());
                        }
                        Log.e("position",position+" rangoinftop:"+rangInfTop+" rangoSupTop:"+ rangSupTop+" numPagTop:"+numPagTop);
                        if (prendasTop!=null){
                            //visibilidad prendas vestidos
                            if (isVestido(prendasTop.get(position))){
                                frameTop.setVisibility(View.VISIBLE);
                                frameBottom.setVisibility(View.GONE);
                            }else{
                                if(hayPrendaBottom){
                                    frameBottom.setVisibility(View.VISIBLE);
                                }else{
                                    frameBottom.setVisibility(View.INVISIBLE);
                                }
                            }
                            //visibilidad navegacion hacia atras
                            if(position>=1){//a partir de segunda posicion
                                framePreviousTop.setVisibility(View.VISIBLE);
                            }else if(position==0){
                                framePreviousTop.setVisibility(View.GONE);
                            }

                            if (position==rangSupTop){
                                frameNextTop.setVisibility(View.GONE);
                            }else if(prendasTop.size()>=2){
                                frameNextTop.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });


        pagerBotton.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                positionTop=position;
                Log.e("position",position+" rangoinftop:"+rangInfBottom+" rangoSuptop:"+ rangSupBottom+" numPagBot:"+numPageBottom);
                //verificar si llego a la prenda 10 bottom
                if (reloadBot && position==rangInfBottom && prendasBottom!=null && position!=rangSupBottom){
                    dsProbadorNew.getGlobalPrendasProbador("B",numPageBottom+1,"10");
                    gd = new GlupDialogNew();
                    gd.setCancelable(false);
                    gd.show(getActivity().getSupportFragmentManager(), GlupDialogNew.class.getSimpleName());
                }
                //if (conexionBottom && hayPrendaBottom){
                if (prendasBottom!=null){
                    //visibilidad navegacion hacia atras
                    if(position>=1){//a partir de segunda posicion
                        framePreviousBottom.setVisibility(View.VISIBLE);
                    }else if(position==0){
                        framePreviousBottom.setVisibility(View.GONE);
                    }
                    if (position==rangSupBottom){
                        frameNextBottom.setVisibility(View.GONE);
                    }else if(prendasBottom.size()>=2){
                        frameNextBottom.setVisibility(View.VISIBLE);
                    }
                }
                //}
            }
            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        android.support.v4.app.FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        gd = new GlupDialogNew();
        gd.setCancelable(false);
        gd.show(fragmentManager, GlupDialogNew.class.getSimpleName());
    }
    @Subscribe
    public void successPrendasTop(DSProbadorNew.ResponseProbadorTop responseProbadorTop){
        Log.e("esto", "tri");
        if (responseProbadorTop.success==1){//conexion hecha
            Log.e("Top","conexion hecha");
            conexionTop=true;
            if (!responseProbadorTop.prendas.isEmpty()){//verica si hay prendas
                Log.e("Top", "si hay prendas");
                numPagTop++;
                hayPrendaTop=true;
                rangSupTop=(responseProbadorTop.prendas.get(0).getNumPrendProb()-1);
                if (numPagTop==1){
                    if (isVestido(responseProbadorTop.prendas.get(0))){//verifica si primera prenda es vestido
                        vestido=true;
                        frameTop.setVisibility(View.VISIBLE);
                        frameBottom.setVisibility(View.GONE);
                    }else{
                        vestido=false;
                        frameTop.setVisibility(View.VISIBLE);
                        //frameBottom.setVisibility(View.INVISIBLE);
                    }
                    rangInfTop=9;
                    prendasTop=responseProbadorTop.prendas;
                    pagerTopAdapter= new PagerTopAdapter(getActivity(),prendasTop);
                    pagerTop.setAdapter(pagerTopAdapter);

                }else{//numPagTop>1 no puede tomar valor 0 y prendasTop=ya tomo el valor de carga inicial
                    //reloadtop true
                    boolean siEsta=false;
                    for (Prenda prendLlegan:responseProbadorTop.prendas){
                        for (Prenda prendActual:prendasTop){
                            siEsta=false;
                            if(prendLlegan.getCod_prenda().equals(prendActual.getCod_prenda())){
                                siEsta=true;
                                break;
                            }
                        }
                        if (siEsta){
                            Log.e("encontro","mismo codigo");
                        }else{
                            prendasTop.add(prendLlegan);
                        }
                    }
                    rangInfTop=prendasTop.size()-1;
                    //prendasTop.addAll(responseProbadorTop.prendas);
                    pagerTopAdapter.notifyDataSetChanged();
                    //pagerTopAdapter= new PagerTopAdapter(getActivity(),prendasTop);
                    //pagerTop.setAdapter(pagerTopAdapter);
                }
                if (rangInfTop==9){
                    pagerTop.setCurrentItem(0);
                }
                if (prendasTop.size()>=2){
                    frameNextTop.setVisibility(View.VISIBLE);
                }
            }else{
                Log.e("Top","no hay prendas");
                vestido=false;
                hayPrendaTop=false;
                if (prendasTop==null){
                    frameTop.setVisibility(View.INVISIBLE);
                }else {
                    frameTop.setVisibility(View.VISIBLE);
                }
                //-->estado posible actualizado en el siguiente servicio
                //frameBottom estado segun siguiente servicio
            }
            Log.e("numPagTop:",numPagTop+"");
        }else{
            //si fallo la conexion de prendas top cargar de todas maneras prendas bottom
            conexionTop=false;
        }
        if(numPagTop<=1){
            dsProbadorNew.getGlobalPrendasProbador("B", numPageBottom + 1, "10");
        }else {
            //cuando esta aumentando cargar de mas de 10 prendas
            gd.dismiss();
            reloadTop=true;
        }
    }
    @Subscribe
    public void successPrendasBottom(DSProbadorNew.ResponseProbadorBottom responseProbadorBottom){
        if (responseProbadorBottom.success==1){//conexion hecha
            conexionBottom=true;
            Log.e("Bottom", "conexion hecha");
            if (!responseProbadorBottom.prendas.isEmpty()){
                Log.e("Bottom", "si hay prendas");
                hayPrendaBottom=true;
                numPageBottom++;
                rangSupBottom=(responseProbadorBottom.prendas.get(0).getNumPrendProb()-1);
                if (numPageBottom==1){
                    if (hayPrendaTop==true){
                        frameTop.setVisibility(View.VISIBLE);
                        if (vestido==true){
                            frameBottom.setVisibility(View.GONE);
                        }else{
                            frameBottom.setVisibility(View.VISIBLE);
                        }
                    }else { //vestido=false
                        frameTop.setVisibility(View.INVISIBLE);
                        frameBottom.setVisibility(View.VISIBLE);
                    }
                    rangInfBottom=9;
                    prendasBottom=responseProbadorBottom.prendas;
                    pagerBottomAdapter= new PagerBottomAdapter(getActivity(),prendasBottom);
                    pagerBotton.setAdapter(pagerBottomAdapter);
                    reloadBot=true;//basta q la primera vez cargue bot para que se active
                    reloadTop=true;
                    superior.setVisibility(View.VISIBLE);
                    medio.setVisibility(View.VISIBLE);
                    titleProbador.setVisibility(View.VISIBLE);

                }else{
                    //reload bot sigue en true
                    boolean siEsta=false;
                    for (Prenda prendLlegan:responseProbadorBottom.prendas){
                        for (Prenda prendActual:prendasBottom){
                            siEsta=false;
                            if(prendLlegan.getCod_prenda().equals(prendActual.getCod_prenda())){
                                siEsta=true;
                                break;
                            }
                        }
                        if (siEsta){
                            Log.e("encontro","mismo codigo");
                        }else{
                            prendasBottom.add(prendLlegan);
                        }
                    }
                    rangInfBottom=prendasBottom.size()-1;
                    //prendasBottom.addAll(responseProbadorBottom.prendas);
                    pagerBottomAdapter.notifyDataSetChanged();
                    //pagerBottomAdapter= new PagerBottomAdapter(getActivity(),prendasBottom);
                    //pagerBotton.setAdapter(pagerBottomAdapter);
                }
                if (rangInfBottom==9){
                    pagerBotton.setCurrentItem(0);
                }
                if (prendasBottom.size()>=2){
                    frameNextBottom.setVisibility(View.VISIBLE);
                }
            }else{
                //falta gestionar cuando en la siguiente carga ya no hay mas prendas
                Log.e("Bottom","no hay prendas");
                hayPrendaBottom= false;
                if (hayPrendaTop==true){
                    frameTop.setVisibility(View.VISIBLE);
                    emptyView.setVisibility(View.GONE);
                    if (vestido==true){
                        frameBottom.setVisibility(View.GONE);
                    }else{
                        frameBottom.setVisibility(View.INVISIBLE);
                    }

                }else{
                    if (prendasTop!=null){
                        frameTop.setVisibility(View.VISIBLE);
                        if (vestido==true){
                            frameBottom.setVisibility(View.GONE);
                        }else{
                            frameBottom.setVisibility(View.INVISIBLE);
                        }
                    }else{
                        if (numPagTop==0 && (prendasBottom==null || prendasBottom.size()==0)){
                            frameProbador.setVisibility(View.GONE);
                            emptyView.setVisibility(View.VISIBLE);
                        }else{
                            Log.e("Carga","ya no hay mas prendas para carga top/botton");
                        }
                    }
                }
                if(numPagTop<=1){
                    if (numPagTop==1){
                        reloadTop=true;//si carga top pero en bot no
                    }
                    superior.setVisibility(View.VISIBLE);
                    medio.setVisibility(View.VISIBLE);
                    titleProbador.setVisibility(View.VISIBLE);
                }
            }
            Log.e("numPagBot:", numPageBottom + "");
        }else{
            conexionBottom=false;
            //falta manejar que saldra cuando la conexion falle
            if (conexionTop==false){
                if (numPagTop==0){
                    Log.e("Conexion:","bot fallo,top fallo cuando se cargaba prendas por primera vez");
                    frameProbador.setVisibility(View.GONE);
                    emptyView.setVisibility(View.VISIBLE);
                }else{
                    Log.e("Conexion:","bot fallo,top fallo cuando se buscaba cargar mas prendas");
                }
            }else{
                Log.e("ConexionBot", "fallo");
            }
        }
        gd.dismiss();
        Log.e("CargaProbador", "top:" + hayPrendaTop + " bottom:" + hayPrendaBottom + " vestido:" + vestido+" numPagTop:"+numPagTop+" numPagBot"+numPageBottom);
    }
    private void initSliderButtons() {
        if (new Session_Manager(getActivity()).getCurrentUserSexo().equals("H")){
            superior.setImageResource(R.drawable.superior_man);
            medio.setImageResource(R.drawable.medio_man);
        }else{
            superior.setImageResource(R.drawable.superior_woman);
            medio.setImageResource(R.drawable.medio_woman);
        }
    }
    private boolean isVestido(Prenda prenda){
        if (prenda.getTipo().toUpperCase().equals("VESTIDO")){
            return  true;
        }else {
            return false;
        }
    }


    @Override
    public void onDrawerSlide(View drawerView, float slideOffset) {
        //Log.e("slide", drawerView.getTag().toString() + " offset " + slideOffset);
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
    private void changePosButton(String s, ImageView izq, ImageView der) {
        if (s.equals("Drawerabierto")) {
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)izq.getLayoutParams();
            params.addRule(RelativeLayout.ALIGN_PARENT_LEFT, 0);
            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, 1);
            izq.setLayoutParams(params);
            RelativeLayout.LayoutParams params1 = (RelativeLayout.LayoutParams)der.getLayoutParams();
            params1.addRule(RelativeLayout.ALIGN_PARENT_LEFT,1);
            params1.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, 0);
            der.setLayoutParams(params1);
        } else {
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)izq.getLayoutParams();
            params.addRule(RelativeLayout.ALIGN_PARENT_LEFT,1);
            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, 0);
            izq.setLayoutParams(params);
            RelativeLayout.LayoutParams params1 = (RelativeLayout.LayoutParams)der.getLayoutParams();
            params1.addRule(RelativeLayout.ALIGN_PARENT_RIGHT,1);
            params1.addRule(RelativeLayout.ALIGN_PARENT_LEFT, 0);
            der.setLayoutParams(params1);
        }
    }
    @Override
    public void onDrawerStateChanged(int newState) {
        Log.e("state", newState + "");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_previous_top:
                Log.e("clic", "previos_top");
                if (pagerTop!=null && pagerTop.getAdapter()!=null)
                    previousPageTop();
                break;
            case R.id.button_next_top:
                Log.e("clic", "next_top");
                if (pagerTop!=null && pagerTop.getAdapter()!=null)
                    nextPageTop();
                break;
            case R.id.button_previous_bottom:
                Log.e("clic", "previos_bottom");
                if (pagerBotton!=null && pagerBotton.getAdapter()!=null)
                    previousPageBottom();
                break;
            case R.id.button_next_bottom:
                Log.e("clic","next_bottom");
                if (pagerBotton!=null)
                    nextPageBottom();
                break;
            case R.id.superior:
                Log.e("clic", "superior");
                FooterGone footerGone=new FooterGone();
                BusHolder.getInstance().post(footerGone);
                drawerLayout.openDrawer(GravityCompat.START);
                break;
            case R.id.medio:
                Log.e("clic", "medio");
                FooterGone footerGone1=new FooterGone();
                BusHolder.getInstance().post(footerGone1);
                drawerLayout.openDrawer(GravityCompat.END);
                break;
        }
    }

    private void nextPageTop() {
        int currentPage = pagerTop.getCurrentItem();
        int totalPages = pagerTop.getAdapter().getCount();
        int nextPage = currentPage+1;
        if (nextPage >= totalPages) {
            nextPage = 0;
        }
        pagerTop.setCurrentItem(nextPage, false);
    }
    private void previousPageTop() {
        int currentPage = pagerTop.getCurrentItem();
        int totalPages = pagerTop.getAdapter().getCount();
        int previousPage = currentPage-1;
        if (previousPage < 0) {
            previousPage = totalPages - 1;
        }
        pagerTop.setCurrentItem(previousPage, false);
    }
    private void nextPageBottom() {
        int currentPage = pagerBotton.getCurrentItem();
        int totalPages = pagerBotton.getAdapter().getCount();
        int nextPage = currentPage+1;
        if (nextPage >= totalPages) {
            nextPage = 0;
        }
        pagerBotton.setCurrentItem(nextPage, false);
    }
    private void previousPageBottom() {
        int currentPage = pagerBotton.getCurrentItem();
        int totalPages = pagerBotton.getAdapter().getCount();
        int previousPage = currentPage-1;
        if (previousPage < 0) {
            previousPage = totalPages - 1;
        }
        pagerBotton.setCurrentItem(previousPage, false);
    }

    public class FooterVisible{}
    public class FooterGone{}

    @Subscribe
    public  void getReloadPrendas(PrendaAdapterMenu.Holder holder ){
        Log.e("check", String.valueOf(holder.corazon.isChecked())+" filtro:"+holder.filtro+" operacion:"+holder.operacion+" prendaCod:"+holder.prenda.getCod_prenda()+" numPagTop:"+numPagTop+" numPagBot:"+numPageBottom);
        Log.e("rangSupTop:",rangSupTop+" rangInfTop:"+ rangInfTop+ " rangSupBot:"+rangSupBottom+" rangInfBot:"+rangInfBottom);
        if (holder.filtro.equals("A")){
            if (holder.corazon.isChecked()){
                if (prendasTop==null){
                    prendasTop=new ArrayList<Prenda>();
                    Log.e("prendTopNull",prendasTop.size()+"");
                    rangSupTop=-1;
                }
                Prenda prendAdd = holder.prenda;
                prendasTop.add(prendAdd);

                pagerTopAdapter=new PagerTopAdapter(getActivity(),prendasTop);
                pagerTop.setAdapter(pagerTopAdapter);
                //pagerTopAdapter.notifyDataSetChanged();

                    if (prendasTop.size()!=0){
                        rangInfTop+=1;
                        addTop=true;//
                        frameTop.setVisibility(View.VISIBLE);
                    }
                    //numPagTop++;

                rangSupTop+=1;
                if (prendasTop.size()>=2){
                    frameNextTop.setVisibility(View.VISIBLE);
                }
            }else {
                Log.e("PrevPrenTop:", prendasTop.size() + "");
                Iterator<Prenda> iterator= prendasTop.iterator();
                while (iterator.hasNext()){
                    Prenda prendDel = iterator.next();
                    if (prendDel.getCod_prenda().equals(holder.prenda.getCod_prenda())) {
                        prendasTop.remove(prendDel);
                        Log.e("PostPrenTop:", prendasTop.size() + "");
                        break;
                    }
                }
                pagerTopAdapter=new PagerTopAdapter(getActivity(),prendasTop);
                pagerTop.setAdapter(pagerTopAdapter);
                //pagerTopAdapter.notifyDataSetChanged();
                if (numPagTop!=0 && prendasTop.size()==1){
                    numPagTop=0;
                    rangInfTop=9;
                }
                if (numPagTop!=0){
                    if (prendasTop.size()!=0){
                        rangInfTop-=1;
                        delTop=true;//
                        if (prendasTop.size()%10==0){
                            numPagTop--;
                        }
                    }
                }
                if(prendasTop.size()==0){
                    if (prendasBottom==null || prendasBottom.size()==0){
                        frameProbador.setVisibility(View.GONE);
                        emptyView.setVisibility(View.VISIBLE);
                        prendasTop=null;prendasBottom=null;
                        numPagTop=0;numPageBottom=0;
                        rangInfTop=9;rangInfBottom=9;
                    }else{
                        frameProbador.setVisibility(View.VISIBLE);
                        emptyView.setVisibility(View.GONE);
                        frameTop.setVisibility(View.INVISIBLE);
                    }
                }else if (prendasTop.size()==1){
                    frameTop.setVisibility(View.VISIBLE);
                    frameNextTop.setVisibility(View.GONE);
                    framePreviousTop.setVisibility(View.GONE);
                    //si es vestido ocupa todo sin next y previus y si hay bottom aun
                }else{
                    if (isVestido(prendasTop.get(0))){
                        frameTop.setVisibility(View.VISIBLE);
                        frameBottom.setVisibility(View.GONE);
                    }else{
                        frameTop.setVisibility(View.VISIBLE);
                        frameBottom.setVisibility(View.VISIBLE);
                    }
                }
                framePreviousTop.setVisibility(View.GONE);//siempre comienza de position 0
                rangSupTop-=1;
            }
            Log.e("reloadA"," rangoinftop:"+rangInfTop+" rangoSupTop:"+ rangSupTop+" numPagTop:"+numPagTop);
        }else{
            if (holder.corazon.isChecked()){
                if (prendasBottom==null){
                    prendasBottom=new ArrayList<Prenda>();
                    rangSupBottom=-1;
                }
                prendasBottom.add(holder.prenda);
                pagerBottomAdapter=new PagerBottomAdapter(getActivity(),prendasBottom);
                pagerBotton.setAdapter(pagerBottomAdapter);
                //pagerBottomAdapter.notifyDataSetChanged();
                if (prendasBottom.size()!=0){
                    rangInfBottom+=1;
                    addBot=true;//
                    frameBottom.setVisibility(View.VISIBLE);
                }
                    //numPageBottom++;

                rangSupBottom+=1;
                if (prendasBottom.size()>=2){
                    frameNextBottom.setVisibility(View.VISIBLE);
                }
            }else {

                Log.e("PrevPrenBot:", prendasBottom.size() + "");
                Iterator<Prenda> iterator= prendasBottom.iterator();
                while (iterator.hasNext()){
                    Prenda prendDel = iterator.next();
                    if (prendDel.getCod_prenda().equals(holder.prenda.getCod_prenda())) {
                        prendasBottom.remove(prendDel);
                        Log.e("PostPrenBot:", prendasBottom.size() + "");
                        break;
                    }
                }
                pagerBottomAdapter=new PagerBottomAdapter(getActivity(),prendasBottom);
                pagerBotton.setAdapter(pagerBottomAdapter);
                //pagerBottomAdapter.notifyDataSetChanged();
                //prendasBottom.remove(holder.prenda);
                //pagerBottomAdapter.notifyDataSetChanged();
                if (numPageBottom!=0 && prendasBottom.size()==1){
                    numPageBottom=0;
                    rangInfBottom=9;
                }
                if (numPageBottom!=0){
                    if (prendasBottom.size()!=0){
                        rangInfBottom-=1;
                        delBot=true;//
                        if (prendasBottom.size()%10==0){
                            numPageBottom--;
                        }
                    }
                }
                if(prendasBottom.size()==0){
                    if (prendasTop==null || prendasTop.size()==0){
                        frameProbador.setVisibility(View.GONE);
                        emptyView.setVisibility(View.VISIBLE);
                        prendasTop=null;prendasBottom=null;
                        numPagTop=0;numPageBottom=0;
                        rangInfTop=9;rangInfBottom=9;
                    }else{
                        frameProbador.setVisibility(View.VISIBLE);
                        emptyView.setVisibility(View.GONE);
                        //estado frameBottom depende de la prenda top
                    }
                }else if (prendasBottom.size()==1){
                    //frameBottom.setVisibility(View.VISIBLE);
                    frameNextBottom.setVisibility(View.GONE);
                    framePreviousBottom.setVisibility(View.GONE);
                    //si es vestido ocupa todo sin next y previus y si hay bottom aun
                }
                framePreviousBottom.setVisibility(View.GONE);
                rangSupBottom -= 1;
            }
            Log.e("reloadB"," rangoinfbot:"+rangInfBottom+" rangoSupbot:"+ rangSupBottom+" numPagBot:"+numPageBottom);
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
}