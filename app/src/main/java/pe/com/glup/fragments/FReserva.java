package pe.com.glup.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ToggleButton;

import pe.com.glup.R;
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
        glup = (Glup) getActivity();
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.e("entra","createview");
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

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.reserva:
                ticket.setChecked(false);
                fReservaInfo = new FReservaInfo();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_reserva_ticket,fReservaInfo);
                fragmentTransaction.commit();
                break;
            case R.id.ticket:
                reserva.setChecked(false);
                fReservaTicket = new FReservaTicket();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_reserva_ticket,fReservaTicket);
                fragmentTransaction.commit();
                break;
        }
    }

}
