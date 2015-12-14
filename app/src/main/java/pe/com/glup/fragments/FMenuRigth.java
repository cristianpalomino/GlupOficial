package pe.com.glup.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.squareup.otto.Subscribe;

import java.util.ArrayList;

import pe.com.glup.R;
import pe.com.glup.adapters.PrendaAdapterMenu;
import pe.com.glup.dialog.GlupDialogNew;
import pe.com.glup.models.Prenda;
import pe.com.glup.managers.bus.BusHolder;
import pe.com.glup.network.DSProbadorNew;


public class FMenuRigth extends Fragment implements
        ListView.OnItemClickListener,ListView.OnScrollListener{

    private ListView listView;
    private ArrayList<Prenda> prendasBottom;
    private  PrendaAdapterMenu prendaAdapter;
    private DSProbadorNew dsProbadorNew;
    private int numPagBotMenu;
    private GlupDialogNew gd;
    private boolean flagscroll=false;
    private int totalPrendas;

    public static FMenuRigth newInstance() {
        FMenuRigth fragment = new FMenuRigth();
        return fragment;
    }

    public FMenuRigth() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BusHolder.getInstance().register(this);
        dsProbadorNew=new DSProbadorNew(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_menu_left, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        numPagBotMenu =0;totalPrendas=0;
        flagscroll=false;
        listView = (ListView) getView().findViewById(R.id.listView);
        listView.setOnItemClickListener(this);
        listView.setOnScrollListener(this);
        dsProbadorNew.getGlobalPrendasCatalogo("B", String.valueOf(numPagBotMenu + 1), "10");
    }
    @Subscribe
    public void successPrendasTop(DSProbadorNew.ResponseCatalogo responseCatalogo){
        if (responseCatalogo.tipo.equals("B"))
        {   numPagBotMenu++;
            flagscroll=true;
            if (numPagBotMenu ==1){
                prendasBottom = responseCatalogo.prendas;
                totalPrendas=responseCatalogo.prendas.get(0).getNumPrendCP();
                Log.e("totalright",responseCatalogo.prendas.get(0).getNumPrendCP()+"");
                prendaAdapter = new PrendaAdapterMenu(getActivity(),this.prendasBottom);
                listView.setAdapter(prendaAdapter);}else{
                prendasBottom.addAll(responseCatalogo.prendas);
                prendaAdapter.notifyDataSetChanged();
            }
        }
        //gd.dismiss();
    }

    @Subscribe
    public void getIndProbador(String indProb) {
        prendaAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        Log.e("listright","firstvisible "+firstVisibleItem+" visibleitemcount "+visibleItemCount+" numPag:"+ numPagBotMenu);
        if (firstVisibleItem+visibleItemCount==10*(numPagBotMenu) && flagscroll && firstVisibleItem+visibleItemCount!=totalPrendas){
            flagscroll=false;
            Log.e("reloadBot", numPagBotMenu +"");
            dsProbadorNew.getGlobalPrendasCatalogo("B",String.valueOf(numPagBotMenu +1),"10");
            /*gd = new GlupDialogNew();
            gd.setCancelable(false);
            gd.show(getActivity().getSupportFragmentManager(), GlupDialogNew.class.getSimpleName());*/
        }
    }
}
