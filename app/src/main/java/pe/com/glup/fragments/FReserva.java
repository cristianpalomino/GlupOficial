package pe.com.glup.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ToggleButton;

import com.squareup.otto.Subscribe;

import pe.com.glup.R;
import pe.com.glup.managers.bus.BusHolder;
import pe.com.glup.network.DSReserva;
import pe.com.glup.glup.Glup;

/**
 * Created by Glup on 26/09/15.
 */
public class FReserva extends Fragment implements View.OnClickListener{

    private Glup glup;
    private ToggleButton reserva,ticket;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private FReservaInfo fReservaInfo;
    private FReservaTicket fReservaTicket;

    public static FReserva newInstance() {
        FReserva fragment = new FReserva();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        Log.e("entra", "instancia");
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("entra", "create");
        BusHolder.getInstance().register(this);
        glup = (Glup) getActivity();
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.e("entra", "createview");
        return inflater.inflate(R.layout.fragment_reserva, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.e("entra", "activityCreate");
        fragmentManager = getActivity().getSupportFragmentManager();
        reserva = (ToggleButton) getView().findViewById(R.id.reserva);
        ticket = (ToggleButton)getView().findViewById(R.id.ticket);
        reserva.setOnClickListener(this);
        ticket.setOnClickListener(this);
        inicilizarTicketReservaInfo();


    }

    private void inicilizarTicketReservaInfo() {
        Log.e("inicializoReservaInfo", "true");
        ticket.setEnabled(false);
        Handler handler2 = new android.os.Handler();
        handler2.postDelayed(new Runnable() {
            @Override
            public void run() {
                ticket.setChecked(false);
                reserva.setChecked(true);
                ticket.setEnabled(true);
            }
        }, 250);
        fReservaInfo = new FReservaInfo();
        String tag=fReservaInfo.getClass().getSimpleName().toString();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_reserva_ticket,fReservaInfo,tag);
        fragmentTransaction.commit();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.reserva:
                ticket.setChecked(false);
                ticket.setEnabled(false);
                fReservaInfo = new FReservaInfo();
                String tag=fReservaInfo.getClass().getSimpleName().toString();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_reserva_ticket,fReservaInfo,tag);
                fragmentTransaction.commit();
                Handler handler2 = new android.os.Handler();
                handler2.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        reserva.setChecked(true);
                        ticket.setEnabled(true);
                    }
                }, 1250);

                break;
            case R.id.ticket:
                reserva.setChecked(false);
                reserva.setEnabled(false);
                fReservaTicket = new FReservaTicket();
                String tag2 = fReservaTicket.getClass().getSimpleName().toString();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_reserva_ticket,fReservaTicket,tag2);
                fragmentTransaction.commit();
                Handler handler = new android.os.Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ticket.setChecked(true);
                        reserva.setEnabled(true);
                    }
                }, 1250);
                break;

        }
    }

    @Subscribe
    public void reloadFragment(DSReserva.ResponseReload responseReload){
        Log.e("tagRecarga",responseReload.fragment.getTag());
        if (responseReload.fragment!=null){
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.detach(responseReload.fragment);
            fragmentTransaction.attach(responseReload.fragment);
            fragmentTransaction.replace(R.id.fragment_reserva_ticket, responseReload.fragment, responseReload.fragment.getTag());
            fragmentTransaction.commit();
        }
    }

}
