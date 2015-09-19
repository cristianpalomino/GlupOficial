package pe.com.glup.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.squareup.otto.Subscribe;

import java.util.ArrayList;

import pe.com.glup.R;
import pe.com.glup.adapters.PrendaAdapter;
import pe.com.glup.beans.Prenda;
import pe.com.glup.bus.BusHolder;
import pe.com.glup.datasource.DSProbador;
import pe.com.glup.interfaces.OnSuccessPrendas;


public class FMenuRigth extends Fragment implements OnSuccessPrendas{

    private ListView listView;
    private ArrayList<Prenda> prendasTop;

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

        DSProbador dsProbadorA = new DSProbador(getActivity());
        dsProbadorA.setOnSuccessPrendas(FMenuRigth.this);
        dsProbadorA.getGlobalPrendas("B", "1", "20");
    }

    @Override
    public void succesPrendas(DSProbador.ResponseProbador responseProbador) {
        if (responseProbador.tipo.equals("B"))
        {
            this.prendasTop = responseProbador.prendas;
            PrendaAdapter prendaAdapter = new PrendaAdapter(getActivity(),this.prendasTop);
            listView.setAdapter(prendaAdapter);
        }
    }
}
