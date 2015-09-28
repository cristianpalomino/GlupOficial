package pe.com.glup.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import pe.com.glup.R;

/**
 * Created by Glup on 28/09/15.
 */
public class FReservaTicket extends Fragment {
    @Override
    public void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstance){
        return inflater.inflate(R.layout.fragment_clic_ticket,container,false);
    }
    @Override
    public void onActivityCreated(Bundle savedInstance){
        super.onActivityCreated(savedInstance);
    }
}
