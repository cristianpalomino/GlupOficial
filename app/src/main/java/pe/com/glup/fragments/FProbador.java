package pe.com.glup.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.IllegalFormatCodePointException;

import pe.com.glup.R;
import pe.com.glup.adapters.PagerBottomAdapter;
import pe.com.glup.adapters.PagerTopAdapter;
import pe.com.glup.adapters.PrendaAdapter;
import pe.com.glup.adapters.PrendaAdapter2;
import pe.com.glup.adapters.PrendaAdapterMenu;
import pe.com.glup.beans.Prenda;
import pe.com.glup.bus.BusHolder;
import pe.com.glup.datasource.DSProbador;
import pe.com.glup.dialog.DetailActivity;
import pe.com.glup.interfaces.OnClickProbador;
import pe.com.glup.interfaces.OnClickTopProbador;
import pe.com.glup.interfaces.OnSuccessDisableSliding;
import pe.com.glup.interfaces.OnSuccessPrendas;
import pe.com.glup.session.Session_Manager;


public class FProbador extends Fragment implements View.OnClickListener,OnSuccessPrendas{

    private ImageButton superior,medio;
    private ViewPager pagerTop;
    private ViewPager pagerBotton;
    private ArrayList<Prenda> prendasTop;
    private ArrayList<Prenda> prendasBottom;
    private SlidingMenu menuright;
    private OnClickProbador onClickProbador;
    private PagerTopAdapter pagerTopAdapter;
    private PagerBottomAdapter pagerBottomAdapter;
    private int posCurrentTop,posCurrentBottom;

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
        menuright = new SlidingMenu(getActivity());
        menuright.setMode(SlidingMenu.LEFT_RIGHT);
        menuright.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
//        menu.setShadowWidthRes(R.dimen.shadow_width);
//        menu.setShadowDrawable(R.drawable.shadow);
        menuright.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        menuright.setFadeDegree(0.35f);
        menuright.attachToActivity(getActivity(), SlidingMenu.SLIDING_CONTENT);
        menuright.setMenu(R.layout.menu_left);
        menuright.setSecondaryMenu(R.layout.menu_right);

        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.menu_left, FMenuLeft.newInstance(), FMenuLeft.class.getName())
                .commit();
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.menu_rigth, FMenuRigth.newInstance(), FMenuRigth.class.getName())
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
            dsProbadorA.setOnSuccessPrendas(FProbador.this);
        }catch (ClassCastException c){
            Log.e("errorProb",c.toString());
        }
        dsProbadorA.getGlobalPrendas("A", "1", "20");

        DSProbador dsProbadorB = new DSProbador(getActivity());
        try{
            dsProbadorB.setOnSuccessPrendas(FProbador.this);
        }catch (ClassCastException c){
            Log.e("errorProb",c.toString());
        }

        dsProbadorB.getGlobalPrendas("B", "1", "20");

    }

    @Override
    public void succesPrendas(DSProbador.ResponseProbador responseProbador) {
        Log.e(null,"Recargando prendas para probador ..."+responseProbador.success);
        if (responseProbador.tipo.equals("A"))
        {   Log.e(null,responseProbador.toString());
            //this.prendasTop = responseProbador.prendas;
            this.prendasTop = new ArrayList<Prenda>();
            for (int i=0;i<responseProbador.prendas.size();i++){
                if (responseProbador.prendas.get(i).getIndProbador().equals("1")){
                    this.prendasTop.add(responseProbador.prendas.get(i));
                }
            }
            pagerTopAdapter = new PagerTopAdapter(getActivity(), this.prendasTop);
            pagerTop.setAdapter(pagerTopAdapter);


        } else if (responseProbador.tipo.equals("B"))
        {
            //this.prendasBottom = responseProbador.prendas;
            this.prendasBottom = new ArrayList<Prenda>();
            for (int i=0;i<responseProbador.prendas.size();i++){
                if (responseProbador.prendas.get(i).getIndProbador().equals("1")){
                    this.prendasBottom.add(responseProbador.prendas.get(i));
                }
            }
            pagerBottomAdapter = new PagerBottomAdapter(getActivity(), this.prendasBottom);
            pagerBotton.setAdapter(pagerBottomAdapter);
        }
        if (responseProbador.success==1){
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
                menuright.toggle();
                break;
            case R.id.medio:
                Log.e(null, "medio");
                menuright.showSecondaryMenu(true);
                break;
            case R.id.button_previous_top:
                Log.e(null,"previos_top");
                previousPageTop();
                break;
            case R.id.button_next_top:
                Log.e(null,"next_top");
                nextPageTop();
                break;
            case R.id.button_previous_bottom:
                Log.e(null,"previos_bottom");
                previousPageBottom();
                break;
            case R.id.button_next_bottom:
                Log.e(null,"next_bottom");
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
            dsProbadorA.setOnSuccessPrendas(FProbador.this);
        }catch (ClassCastException c){
            Log.e("errorProb",c.toString());
        }
        dsProbadorA.getGlobalPrendas("A", "1", "20");

        DSProbador dsProbadorB = new DSProbador(getActivity());
        try{
            dsProbadorB.setOnSuccessPrendas(FProbador.this);
        }catch (ClassCastException c){
            Log.e("errorProb",c.toString());
        }
        dsProbadorB.getGlobalPrendas("B", "1", "20");

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

}
