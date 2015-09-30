package pe.com.glup.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import pe.com.glup.R;
import pe.com.glup.RecyclerViewDemoApp;
import pe.com.glup.adapters.RecyclerViewDemoAdapter;
import pe.com.glup.adapters.ReservaAdapter;
import pe.com.glup.beans.DemoModel;
import pe.com.glup.beans.Prenda;
import pe.com.glup.beans.ReservaItem;
import pe.com.glup.beans.ReservaList;
import pe.com.glup.beans.Tienda;
import pe.com.glup.bus.BusHolder;
import pe.com.glup.datasource.DSReserva;
import pe.com.glup.utils.DividerItemDecoration;

/**
 * Created by Glup on 28/09/15.
 */
public class FReservaInfo extends Fragment{
    private TextView cantReserva;
    private RecyclerView recyclerReserva, recyclerView;
    private RecyclerViewDemoAdapter reservaAdapterDemo;
    private ReservaAdapter reservaAdapter;
    private LinearLayoutManager linearLayoutManager;
    private boolean loading = true;
    private int pastVisiblesItems, visibleItemCount, totalItemCount;
    private ArrayList<ReservaItem> reservaItems;
    private Context context;
    private ArrayList<HashMap> reservas = new ArrayList<HashMap>();
    private int contReservas=0;

    @Override
    public void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        BusHolder.getInstance().register(this);
    }
    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,
                             Bundle savedInstance){
        DSReserva dsReserva = new DSReserva(getActivity());
        dsReserva.listarReserva();
        View view = inflater.inflate(R.layout.fragment_clic_reserva,container,false);
        return view;
    }
    @Override
    public void onActivityCreated(Bundle savedInstance){
        super.onActivityCreated(savedInstance);
        context=getActivity();

      /* linearLayoutManager = new ScrollingLinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false, 2000);
        linearLayoutManager.setSmoothScrollbarEnabled(true);

        linearLayoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
        linearLayoutManager.scrollToPosition(0);
        reservaAdapter = new ReservaAdapter(getActivity(),R.layout.item_prenda_reserva);
        recyclerReserva = (RecyclerView) getView().findViewById(R.id.recycler_prendas_reserva);
        recyclerReserva.setAdapter(reservaAdapter);
        recyclerReserva.setLayoutManager(linearLayoutManager);
        recyclerReserva.setHasFixedSize(true);
*/

        recyclerView = (RecyclerView) getView().findViewById(R.id.recycler_prendas_reserva);
        cantReserva = ((TextView)getView().findViewById(R.id.contReserva));



        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        // you can set the first visible item like this:
        layoutManager.scrollToPosition(0);
        recyclerView.setLayoutManager(layoutManager);

        // allows for optimizations if all items are of the same size:
        recyclerView.setHasFixedSize(true);
        reservaAdapter = new ReservaAdapter(context,R.layout.item_prenda_reserva,reservas);
        recyclerView.setAdapter(reservaAdapter);

        RecyclerView.ItemDecoration itemDecoration =
                new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST);
        recyclerView.addItemDecoration(itemDecoration);
        // this is the default; this call is actually only necessary with custom ItemAnimators
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        cargarTotal(contReservas);

    }

    private void cargarTotal(int contReservas) {

    }

    @Subscribe
    public void getReservas(DSReserva.ResponseReserva responseReserva){
        if (responseReserva.success==1){

            for (ReservaList list:responseReserva.listaReserva){
                for (Prenda prenda:list.getDatos()){
                    HashMap hashMap = new HashMap();
                    hashMap.put("local",list.getLocal());
                    hashMap.put("prenda", prenda);
                    reservas.add(hashMap);
                    contReservas++;

                }

            }
            reservaAdapter.notifyDataSetChanged();
            cantReserva.setText(contReservas+" Reservas");
        }
    }

}
