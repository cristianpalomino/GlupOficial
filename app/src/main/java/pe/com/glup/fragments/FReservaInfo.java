package pe.com.glup.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;

import pe.com.glup.R;
import pe.com.glup.adapters.ReservaListAdapter;
import pe.com.glup.models.Prenda;
import pe.com.glup.models.ReservaItem;
import pe.com.glup.models.ReservaList;
import pe.com.glup.managers.bus.BusHolder;
import pe.com.glup.network.DSReserva;
import pe.com.glup.dialog.SendEmailDialog;

/**
 * Created by Glup on 28/09/15.
 */
public class FReservaInfo extends Fragment implements View.OnClickListener{
    private TextView cantReserva;
    private ListView recyclerReserva, listView;
    private Button confirmar;
    private ReservaListAdapter reservaAdapter;
    private LinearLayoutManager linearLayoutManager;
    private FragmentManager fragmentManager;
    private boolean loading = true;
    private int pastVisiblesItems, visibleItemCount, totalItemCount;
    private ArrayList<ReservaItem> reservaItems;
    private Context context;
    private ArrayList<HashMap> reservas = new ArrayList<HashMap>();
    private int contReservas=0;
    private ProgressBar progressBar;
    private float total;
    private ArrayList<Integer> unicos=new ArrayList<Integer>();


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
        Log.e(null, "on create view reserva info");
        View view = inflater.inflate(R.layout.fragment_clic_reserva,container,false);
        return view;
    }
    @Override
    public void onActivityCreated(Bundle savedInstance){
        super.onActivityCreated(savedInstance);
        context=getActivity();
        fragmentManager = getActivity().getSupportFragmentManager();
        Log.e("TAG", this.getTag());
        Log.e("fragment", this.toString());
        reservas = new ArrayList<HashMap>();
        listView = (ListView) getView().findViewById(R.id.recycler_prendas_reserva);
        listView.setDivider(null);
        cantReserva = ((TextView)getView().findViewById(R.id.contReserva));
        reservaAdapter = new ReservaListAdapter(context,reservas,this.getTag());
        listView.setAdapter(reservaAdapter);
        reservaAdapter.notifyDataSetChanged();
        confirmar = (Button) getView().findViewById(R.id.confirmar_reserva);
        progressBar = (ProgressBar) getView().findViewById(R.id.temp_progress);
        progressBar.setVisibility(View.VISIBLE);
        confirmar.setOnClickListener(this);

    }



    @Subscribe
    public void getReservas(DSReserva.ResponseReserva responseReserva){
        if (responseReserva.success==1){
            Log.e("entro", "a listar reservas");
            contReservas=0;
            reservas.removeAll(reservas);
            int cont=0;
            String nomMarca="";
            total=0;
            for (ReservaList list:responseReserva.listaReserva){
                for (Prenda prenda:list.getDatos()){
                    Log.e("MiMarca", "cont " + cont + " nomMarca " + nomMarca + "  compara " + list.getMarca().toString());
                    HashMap hashMap = new HashMap();
                    hashMap.put("marca", list.getMarca());
                    hashMap.put("local", list.getLocal());
                    hashMap.put("prenda", prenda);
                    /*precio total*/
                    float precio= Float.parseFloat(prenda.getPrecio());
                    total+=precio;
                    hashMap.put("total",total);
                    /*unicos items en los que debe aparecer el nombre del local y la marca*/
                    if (cont==0){
                        nomMarca=hashMap.get("marca").toString();
                        unicos.add(cont);
                        hashMap.put("visible",1);
                    }
                    if (cont>=1){
                        if (nomMarca.equals(hashMap.get("marca").toString())){
                            //sigue invi
                            hashMap.put("visible",0);
                        }else{
                            Log.e("agrego",cont+"");
                            nomMarca=hashMap.get("marca").toString();
                            unicos.add(cont);
                            hashMap.put("visible",1);
                        }
                    }
                    cont++;
                    reservas.add(hashMap);
                    contReservas++;
                }
            }

            reservaAdapter.notifyDataSetChanged();
            if(contReservas==1){
                cantReserva.setText(contReservas + " Reserva");
            }else{
                cantReserva.setText(contReservas + " Reservas");
            }
            progressBar.setVisibility(View.GONE);
            confirmar.setVisibility(View.VISIBLE);


        }else {
            Log.e(null,"success get reservas "+responseReserva.success);
            progressBar.setVisibility(View.GONE);
            cantReserva.setText("No hay reservas");
            confirmar.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.confirmar_reserva:
                Log.e(null, "tamaño reservas " + reservas.size());
                if (reservas.size()>0){
                    for (HashMap hashMap1:reservas){
                        Log.e(null, hashMap1.get("local")+ " "+((Prenda) hashMap1.get("prenda")).getTipo() + " " + "Perecio " + ((Prenda) hashMap1.get("prenda")).getPrecio());
                    }
                }
                DSReserva dsReserva = new DSReserva(context);
                dsReserva.sendEmail();
                new SendEmailDialog(context).show(fragmentManager,
                        SendEmailDialog.class.getSimpleName());

                break;
        }
    }


}
