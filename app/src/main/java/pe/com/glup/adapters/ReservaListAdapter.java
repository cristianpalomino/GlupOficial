package pe.com.glup.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import pe.com.glup.R;
import pe.com.glup.beans.Prenda;
import pe.com.glup.bus.BusHolder;
import pe.com.glup.datasource.DSReserva;
import pe.com.glup.dialog.ConfirmationDeleteReserva;
import pe.com.glup.dialog.ConfirmationPassDialog;
import pe.com.glup.glup.Glup;

/**
 * Created by Glup on 30/09/15.
 */
public class ReservaListAdapter extends BaseAdapter {
    private ArrayList<HashMap> reservaItems;
    private Context context;
    private LayoutInflater inflater;
    private String tag;
    private float total=0;
    private  ArrayList<Integer> unicos;
    private int contador=0;

    public ReservaListAdapter(Context context, ArrayList<HashMap> reservaItems, String tag, ArrayList<Integer> unicos){
        this.context=context;
        this.reservaItems=reservaItems;
        this.inflater=LayoutInflater.from(context);
        this.tag=tag;
        this.unicos=unicos;
    }

    public ReservaListAdapter(Context context, ArrayList<HashMap> reservaItems, String tag) {
        this.context=context;
        this.reservaItems=reservaItems;
        this.inflater=LayoutInflater.from(context);
        this.tag=tag;
    }


    private float calcularTotal() {
        for (HashMap hashMap:reservaItems){
            float precio= Float.parseFloat(((Prenda) hashMap.get("prenda")).getPrecio());
            total+=precio;
        }
        return total;
    }

    private void TiendasComunes(){
        int cont=0;
        String nomMarca="";
        Log.e("cant.",getCount()+" reservaItems "+reservaItems.size());
        for (HashMap hashMap:reservaItems){
            Log.e("MiMarca","cont "+ cont+" nomMarca "+ nomMarca + "  compara "+ hashMap.get("marca").toString());
            if (cont==0){
                nomMarca=hashMap.get("marca").toString();
                unicos.add(cont);
            }
            if (cont>=1){
                if (nomMarca.equals(hashMap.get("marca").toString())){
                    //sigue invi
                }else{
                    Log.e("agrego",cont+"");
                    nomMarca=hashMap.get("marca").toString();
                    unicos.add(cont);
                }
            }
            cont++;
        }
        for (int i=0;i<unicos.size();i++){
            Log.e("Unicos",unicos.get(i)+"");
        }
    }

    @Override
    public int getCount() {
        return reservaItems.size();
    }

    @Override
    public Object getItem(int position) {
        return reservaItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        //TiendasComunes();
        Holder holder=null;
        HashMap hashMap = reservaItems.get(position);
        holder = new Holder();
        if (convertView==null){
            convertView=inflater.inflate(R.layout.item_prenda_reserva,parent,false);

            // convertView.setTag(holder);
            Log.e("logrep0","0");
        } else {
           // holder = (Holder) convertView.getTag();
           Log.e("logrep1","1");
        }
        Log.e("contador",contador+"");
        contador++;

        holder.nombreTienda = (TextView) convertView.findViewById(R.id.reserva_tienda);
        holder.marcaPrenda = (TextView) convertView.findViewById(R.id.reserva_marca);
        holder.tipo = (TextView) convertView.findViewById(R.id.reserva_tipo);
        holder.precio = (TextView) convertView.findViewById(R.id.precio_parcial);
        holder.eliminarReserva = (ImageView) convertView.findViewById(R.id.reserva_eliminar);
        holder.layoutTotal = (RelativeLayout) convertView.findViewById(R.id.layout_total);
        holder.total = (TextView) convertView.findViewById(R.id.total_reserva);
        holder.contenedor = (RelativeLayout) convertView.findViewById(R.id.layout_item_prenda_reserva);
        holder.contenedorBeforeLocal = (RelativeLayout) convertView.findViewById(R.id.layout_prenda_local);


        holder.nombreTienda.setText(reservaItems.get(position).get("local").toString());
        //holder.marcaPrenda.setText(((Prenda) reservaItems.get(position).get("prenda")).getMarca());
        holder.marcaPrenda.setText(reservaItems.get(position).get("marca").toString());
        int visibilidad = (int) reservaItems.get(position).get("visible");
        if (visibilidad==1){
            Log.e("positionReserva", position + "");
            holder.nombreTienda.setVisibility(View.VISIBLE);
            holder.marcaPrenda.setVisibility(View.VISIBLE);
        }else {
            holder.nombreTienda.setVisibility(View.GONE);
            holder.marcaPrenda.setVisibility(View.GONE);
        }

        holder.tipo.setText(((Prenda) reservaItems.get(position).get("prenda")).getTipo());
        holder.precio.setText("S/."+((Prenda) reservaItems.get(position).get("prenda")).getPrecio());

        if (position==getCount()-1){

            holder.layoutTotal.setVisibility(View.VISIBLE);
            Log.e("calculo","total");
            //holder.total.setText("S/."+calcularTotal()); //pasar valor de calculo total fijo como reservaItems
            holder.total.setText("S/."+reservaItems.get(position).get("total").toString());
        }else {
                holder.layoutTotal.setVisibility(View.GONE);

        }

        if (tag.equals("FReservaInfo")){
            holder.eliminarReserva.setVisibility(View.VISIBLE);
        }else {
            holder.contenedor.setPadding(10,0,0,0);
            holder.contenedorBeforeLocal.setPadding(0, 8, 0, 0);
            holder.eliminarReserva.setVisibility(View.INVISIBLE);
            holder.eliminarReserva.setEnabled(false);
            holder.eliminarReserva.setPadding(0, 0, 0, 0);
            holder.nombreTienda.setTextSize(14);
            holder.marcaPrenda.setTextSize(14);
            holder.tipo.setTextSize(14);
            holder.precio.setTextSize(14);
            holder.total.setVisibility(View.GONE);
        }


        //for (Integer integer:unicos){ //pasar valor fijo como map reservaItems

        //}

        holder.eliminarReserva.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String codPrenda = ((Prenda) reservaItems.get(position).get("prenda")).getCod_prenda();
                ConfirmationDeleteReserva confirmationPassDialog=
                        new ConfirmationDeleteReserva(context,codPrenda,tag);
                confirmationPassDialog.show(((Glup) context).getSupportFragmentManager(), ConfirmationPassDialog.class.getSimpleName());
                Log.e("eliminar", position + "");
                //remove(reservaItems.get(position));


            }
        });

        return convertView;
    }
    
    class Holder {
         TextView nombreTienda;
         TextView marcaPrenda;
         TextView tipo;
         TextView precio;
         ImageView eliminarReserva;
         RelativeLayout layoutTotal;
         TextView total;
         RelativeLayout contenedor;
         RelativeLayout contenedorBeforeLocal;
    }
    public void add(HashMap hashMap){
        reservaItems.add(hashMap);
        notifyDataSetChanged();
    }
    public void remove(HashMap hashMap){
        reservaItems.remove(hashMap);
        notifyDataSetChanged();
    }

}
