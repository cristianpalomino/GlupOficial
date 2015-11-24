package pe.com.glup.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;

import pe.com.glup.R;
import pe.com.glup.adapters.ReservaListAdapter;
import pe.com.glup.models.Prenda;
import pe.com.glup.models.ReservaList;
import pe.com.glup.managers.bus.BusHolder;
import pe.com.glup.network.DSReserva;
import pe.com.glup.utils.MessageUtil;

/**
 * Created by Glup on 2/10/15.
 */
public class DetalleTicketDialog extends DialogFragment {
    private String tag;
    private ListView listView;
    private ArrayList<HashMap> reservas= new ArrayList<HashMap>();
    private ReservaListAdapter reservaAdapter;
    private ProgressBar progressBar;
    private float total;

    public DetalleTicketDialog() {
        tag=DetalleTicketDialog.class.getSimpleName();
        BusHolder.getInstance().register(this);
    }
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        return createDialog();
    }

    private AlertDialog createDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = (getActivity()).getLayoutInflater();
        View v=inflater.inflate(R.layout.dialog_show_detalle_ticket,null);
        listView = (ListView) v.findViewById(R.id.list_detalle_ticket);
        listView.setDivider(null);
        reservas = new ArrayList<HashMap>();
        reservaAdapter = new ReservaListAdapter(getActivity(),reservas,this.getTag());
        listView.setAdapter(reservaAdapter);
        builder.setView(v);
        progressBar = (ProgressBar) v.findViewById(R.id.progress_detalle_ticket);
        progressBar.setVisibility(View.VISIBLE);

        return builder.create();
    }
    @Subscribe
    public void getDetalleTicket(DSReserva.ResponseDetalleTicket responseDetalleTicket){
        if (responseDetalleTicket.success==1){
            Log.e(null,"cant reservas "+responseDetalleTicket.detalleTicket.size());
            if (responseDetalleTicket.detalleTicket.size()==0){
                //nunca va ver tickets sin reservas
            }
            int cont=0;
            String nomMarca="";
            total=0;
            reservas.removeAll(reservas);
            for (ReservaList list:responseDetalleTicket.detalleTicket){
                for (Prenda prenda:list.getDatos()){
                    Log.e("MiMarca", "cont " + cont + " nomMarca " + nomMarca + "  compara " + list.getMarca());
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
                        nomMarca= hashMap.get("marca").toString();
                        hashMap.put("visible",1);
                    }
                    if (cont>=1){
                        if (nomMarca.equals(hashMap.get("marca").toString())){
                            //sigue invi
                            hashMap.put("visible",0);
                        }else{
                            Log.e("agrego",cont+"");
                            nomMarca=hashMap.get("marca").toString();
                            hashMap.put("visible",1);
                        }
                    }
                    cont++;
                    reservas.add(hashMap);


                }

            }
            reservaAdapter.notifyDataSetChanged();
            progressBar.setVisibility(View.GONE);

        }else {
            MessageUtil.showToast(getActivity(),"Desconectado de Internet, no hay reservas");
        }
    }
}
