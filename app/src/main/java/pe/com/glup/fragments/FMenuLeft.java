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
import pe.com.glup.models.Prenda;
import pe.com.glup.managers.bus.BusHolder;
import pe.com.glup.network.DSProbador;
import pe.com.glup.models.interfaces.OnSuccessPrendas;


public class FMenuLeft extends Fragment implements OnSuccessPrendas,
        ListView.OnItemClickListener{

    private ListView listView;
    private ArrayList<Prenda> prendasTop;
    private DSProbador dsProbador;
    private PrendaAdapterMenu prendaAdapter;

    public static FMenuLeft newInstance() {
        FMenuLeft fragment = new FMenuLeft();
        return fragment;
    }

    public FMenuLeft() {
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
            dsProbadorA.setOnSuccessPrendas(FMenuLeft.this);
        }catch (ClassCastException c){
            Log.e(null,c.toString());
        }

        dsProbadorA.getGlobalPrendasCatalogo("A", "1", "20");
    }


    @Override
    public void succesPrendas(DSProbador.ResponseCatalogo responseCatalogo) {
        if (responseCatalogo.tipo.equals("A"))
        {
            this.prendasTop = responseCatalogo.prendas;
            prendaAdapter = new PrendaAdapterMenu(getActivity(),this.prendasTop);
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
