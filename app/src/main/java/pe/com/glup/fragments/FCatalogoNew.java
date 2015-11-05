package pe.com.glup.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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
import pe.com.glup.bus.BusHolder;
import pe.com.glup.glup.Principal;
import pe.com.glup.session.Session_Manager;

/**
 * Created by Glup on 5/10/15.
 */
public class FCatalogoNew extends Fragment implements View.OnClickListener {
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private FCatalogo fCatalogo;
    private ToggleButton hombre,mujer;
    public  static FCatalogoNew newInstance(){
        FCatalogoNew fragment = new FCatalogoNew();
        return fragment;
    }
    @Override
    public  void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        BusHolder.getInstance().register(this);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup
                             container, Bundle savedInstanceState){
        return inflater.inflate(R.layout.fragment_catalogo,container,false);
    }
    @Override
    public void onActivityCreated(Bundle savedInstance){
        super.onActivityCreated(savedInstance);
        fragmentManager=getActivity().getSupportFragmentManager();
        hombre = (ToggleButton) getView().findViewById(R.id.catalogo_hombre);
        mujer = (ToggleButton) getView().findViewById(R.id.catalogo_mujer);
        hombre.setOnClickListener(this);
        mujer.setOnClickListener(this);
        inicializarCatalogoSexo();
    }

    public void inicializarCatalogoSexo() {
       if (new Session_Manager(getActivity()).getCurrentUserSexo().equals("H")){
            Log.e(null,"es hombre");
            mujer.setChecked(false);
            hombre.setChecked(true);
            fCatalogo = FCatalogo.newInstance("genH");
            String tagFragment = FCatalogo.class.getSimpleName()+"Hombre";
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_catalogo,fCatalogo,tagFragment);
            fragmentTransaction.commit();
        }else {
           Log.e(null,"no es hombre");
           hombre.setChecked(false);
            mujer.setChecked(true);
            fCatalogo = FCatalogo.newInstance("genM");
            String tagFragment2 = FCatalogo.class.getSimpleName()+"Mujer";
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_catalogo, fCatalogo, tagFragment2);
            fragmentTransaction.commit();
       }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.catalogo_hombre:
                mujer.setChecked(false);
                mujer.setEnabled(false);
                fCatalogo = FCatalogo.newInstance("genH");
                String tagFragment = FCatalogo.class.getSimpleName()+"Hombre";
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_catalogo,fCatalogo,tagFragment);
                fragmentTransaction.commit();
                Handler handler2 = new android.os.Handler();
                handler2.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        hombre.setChecked(true);
                        mujer.setEnabled(true);
                    }
                }, 1500);
                break;
            case R.id.catalogo_mujer:
                hombre.setChecked(false);
                hombre.setEnabled(false);
                fCatalogo = FCatalogo.newInstance("genM");
                String tagFragment2 = FCatalogo.class.getSimpleName()+"Mujer";
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_catalogo, fCatalogo, tagFragment2);
                fragmentTransaction.commit();
                Handler handler = new android.os.Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mujer.setChecked(true);
                        hombre.setEnabled(true);
                    }
                }, 1500);

                break;
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        inicializarCatalogoSexo();
        //mujer.postInvalidate();
        //hombre.postInvalidate();
            //getActivity().finish();
    }
}
