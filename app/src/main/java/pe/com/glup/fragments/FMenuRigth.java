package pe.com.glup.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.squareup.otto.Subscribe;

import java.util.ArrayList;

import pe.com.glup.R;
import pe.com.glup.adapters.PrendaAdapterMenu;
import pe.com.glup.beans.Prenda;
import pe.com.glup.bus.BusHolder;
import pe.com.glup.datasource.DSProbador;
import pe.com.glup.interfaces.OnSuccessPrendas;


public class FMenuRigth extends Fragment implements OnSuccessPrendas,
        ListView.OnItemClickListener{

    private ListView listView;
    private ArrayList<Prenda> prendasBottom;
    private  PrendaAdapterMenu prendaAdapter;

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
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_menu_left, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listView = (ListView) getView().findViewById(R.id.listView);
        listView.setOnItemClickListener(this);
        DSProbador dsProbadorA = new DSProbador(getActivity());

        try {
            dsProbadorA.setOnSuccessPrendas(FMenuRigth.this);
        }catch (ClassCastException c){
            Log.e(null, c.toString());
        }
        dsProbadorA.getGlobalPrendasCatalogo("B", "1", "20");
    }

    @Override
    public void succesPrendas(DSProbador.ResponseCatalogo responseCatalogo) {
        if (responseCatalogo.tipo.equals("B"))
        {
            this.prendasBottom = responseCatalogo.prendas;
            prendaAdapter = new PrendaAdapterMenu(getActivity(),this.prendasBottom);
            listView.setAdapter(prendaAdapter);
        }
    }
    @Subscribe
    public void getIndProbador(String indProb) {
        prendaAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}
