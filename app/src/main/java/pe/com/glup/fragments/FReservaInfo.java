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
import java.util.List;

import pe.com.glup.R;
import pe.com.glup.adapters.LargeAdapter;
import pe.com.glup.adapters.ReservaAdapter;
import pe.com.glup.beans.Prenda;
import pe.com.glup.beans.ReservaItem;
import pe.com.glup.beans.Tienda;
import pe.com.glup.utils.DividerItemDecoration;
import pe.com.glup.utils.FastScroller;
import pe.com.glup.utils.ScrollingLinearLayoutManager;

import static android.support.v7.widget.RecyclerView.*;

/**
 * Created by Glup on 28/09/15.
 */
public class FReservaInfo extends Fragment{
    private RecyclerView recyclerReserva, recyclerView;
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
        View view = inflater.inflate(R.layout.fragment_clic_reserva,container,false);
        /*recyclerView = (RecyclerView)view.findViewById(R.id.recycler_prendas_reserva);
        recyclerView.setAdapter(LargeAdapter.newInstance(getActivity()));
        int duration = getResources().getInteger(R.integer.scroll_duration);
        recyclerView.setLayoutManager(new ScrollingLinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false, duration));
        FastScroller fastScroller = (FastScroller) view.findViewById(R.id.fastscroller);
        fastScroller.setRecyclerView(recyclerView);*/
        return view;
    }
    @Override
    public void onActivityCreated(Bundle savedInstance){
        super.onActivityCreated(savedInstance);


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

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        // actually VERTICAL is the default,
        // just remember: LinearLayoutManager
        // supports HORIZONTAL layout out of the box
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        // you can set the first visible item like this:
        layoutManager.scrollToPosition(0);
        recyclerView.setLayoutManager(layoutManager);


        // allows for optimizations if all items are of the same size:
        recyclerView.setHasFixedSize(true);

        reservaAdapter = new ReservaAdapter(getActivity(),R.layout.item_prenda_reserva);
        recyclerView.setAdapter(reservaAdapter);

        RecyclerView.ItemDecoration itemDecoration =
                new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST);
        recyclerView.addItemDecoration(itemDecoration);

        // this is the default; this call is actually only necessary with custom ItemAnimators
        recyclerView.setItemAnimator(new DefaultItemAnimator());
      /*
        FastScroller fastScroller = (FastScroller) getView().findViewById(R.id.fastscroller);
        fastScroller.setRecyclerView(recyclerReserva);*/

    }

}
