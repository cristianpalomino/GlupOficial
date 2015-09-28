package pe.com.glup.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.OnScrollListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;

import pe.com.glup.R;
import pe.com.glup.adapters.ReservaAdapter;
import pe.com.glup.beans.Prenda;
import pe.com.glup.beans.ReservaItem;
import pe.com.glup.beans.Tienda;

import static android.support.v7.widget.RecyclerView.*;

/**
 * Created by Glup on 28/09/15.
 */
public class FReservaInfo extends Fragment{
    private RecyclerView recyclerReserva;
    private ReservaAdapter reservaAdapter;
    private LinearLayoutManager linearLayoutManager;
    private boolean loading = true;
    private int pastVisiblesItems, visibleItemCount, totalItemCount;
    @Override
    public void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
    }
    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,
                             Bundle savedInstance){


        return inflater.inflate(R.layout.fragment_clic_reserva,container,false);
    }
    @Override
    public void onActivityCreated(Bundle savedInstance){
        super.onActivityCreated(savedInstance);
        ArrayList<ReservaItem> reservaItems = new ArrayList<ReservaItem>();
        for (int i=0;i<10;i++){
            ReservaItem reservaItem = new ReservaItem();
            Tienda tienda = new Tienda();
            tienda.setLocal("tienda " + i);
            Prenda prenda = new Prenda();
            prenda.setMarca("Marca " + i);
            prenda.setTipo("Tipo " + i);
            prenda.setPrecio("Precio " + i);
            reservaItem.setTienda(tienda);
            reservaItem.setPrenda(prenda);
            reservaItems.add(reservaItem);
        }

        linearLayoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
        reservaAdapter = new ReservaAdapter(getActivity(),R.layout.item_prenda_reserva,reservaItems);
        recyclerReserva = (RecyclerView) getView().findViewById(R.id.recycler_prendas_reserva);
        recyclerReserva.setAdapter(reservaAdapter);
        recyclerReserva.setLayoutManager(linearLayoutManager);


    }

}
