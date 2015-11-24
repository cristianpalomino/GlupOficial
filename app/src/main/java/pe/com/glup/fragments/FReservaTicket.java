package pe.com.glup.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.otto.Subscribe;

import java.util.ArrayList;

import pe.com.glup.R;
import pe.com.glup.adapters.TicketListAdapter;
import pe.com.glup.models.TicketList;
import pe.com.glup.managers.bus.BusHolder;
import pe.com.glup.network.DSReserva;
import pe.com.glup.dialog.DetalleTicketDialog;

/**
 * Created by Glup on 28/09/15.
 */
public class FReservaTicket extends Fragment implements ListView.OnItemClickListener{
    private ListView listView;
    private ProgressBar progressBar;
    private TextView empty;
    private ArrayList<TicketList> tickets;
    private Context context;
    private TicketListAdapter ticketListAdapter;
    private String tag;
    private FragmentManager fragmentManager;
    private float total;
    @Override
    public void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        BusHolder.getInstance().register(this);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstance){
        DSReserva dsReserva = new DSReserva(getActivity());
        dsReserva.listarTicket();
        return inflater.inflate(R.layout.fragment_clic_ticket,container,false);
    }
    @Override
    public void onActivityCreated(Bundle savedInstance){
        super.onActivityCreated(savedInstance);
        context=getActivity();
        fragmentManager=getActivity().getSupportFragmentManager();
        listView = (ListView) getView().findViewById(R.id.list_ticket);
        progressBar = (ProgressBar) getView().findViewById(R.id.progress_ticket);
        empty = (TextView) getView().findViewById(R.id.empty_tickets);
        progressBar.setVisibility(View.VISIBLE);

        tickets = new ArrayList<TicketList>();
        ticketListAdapter = new TicketListAdapter(tickets,context,this.getTag());
        listView.setAdapter(ticketListAdapter);
        ticketListAdapter.notifyDataSetChanged();
        listView.setOnItemClickListener(this);

    }
    @Subscribe
    public  void getTickets(DSReserva.ResponseTicket responseTicket){
        if (responseTicket.success==1){

            listView.setVisibility(View.VISIBLE);
            empty.setVisibility(View.GONE);

            for (TicketList list:responseTicket.listaTicket){
                Log.e("ticket", list.getCodVenta());
                tickets.add(list);
            }
            ticketListAdapter.notifyDataSetChanged();

            progressBar.setVisibility(View.GONE);

        }else {

            listView.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);
            empty.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.e("clic", position + " " + ((TicketList) parent.getItemAtPosition(position)).getCodVenta());
        DSReserva dsReserva = new DSReserva(context);
        dsReserva.listarDetalleTicket(((TicketList)parent.getItemAtPosition(position)).getCodVenta());

        new DetalleTicketDialog().show(fragmentManager,
                DetalleTicketDialog.class.getSimpleName());
    }
}
