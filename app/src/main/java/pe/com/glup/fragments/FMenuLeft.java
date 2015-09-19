package pe.com.glup.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.squareup.otto.Subscribe;

import java.util.ArrayList;

import pe.com.glup.R;
import pe.com.glup.adapters.PagerBottomAdapter;
import pe.com.glup.adapters.PagerTopAdapter;
import pe.com.glup.adapters.PrendaAdapter;
import pe.com.glup.beans.Prenda;
import pe.com.glup.bus.BusHolder;
import pe.com.glup.datasource.DSProbador;
import pe.com.glup.interfaces.OnSuccessPrendas;


public class FMenuLeft extends Fragment implements OnSuccessPrendas{

    private ListView listView;
    private ArrayList<Prenda> prendasTop;
    private DSProbador dsProbador;

    public static FMenuLeft newInstance() {
        FMenuLeft fragment = new FMenuLeft();
        return fragment;
    }

    public FMenuLeft() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
        dsProbadorA.setOnSuccessPrendas(FMenuLeft.this);
        dsProbadorA.getGlobalPrendas("A", "1", "20");
    }


    @Override
    public void succesPrendas(DSProbador.ResponseProbador responseProbador) {
        if (responseProbador.tipo.equals("A"))
        {
            this.prendasTop = responseProbador.prendas;
            PrendaAdapter prendaAdapter = new PrendaAdapter(getActivity(),this.prendasTop);
            listView.setAdapter(prendaAdapter);
        }
    }
}
