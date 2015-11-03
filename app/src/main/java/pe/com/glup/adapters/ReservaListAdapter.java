package pe.com.glup.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
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

    public ReservaListAdapter(Context context,ArrayList<HashMap> reservaItems,String tag){
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

    private ArrayList<Integer> TiendasComunes(){
        int cont=0;
        String nomLocal="";
        ArrayList<Integer> unicos=new ArrayList<Integer>();
        for (HashMap hashMap:reservaItems){
            if (cont==0){
                nomLocal=hashMap.get("local").toString();
                unicos.add(cont);
            }
            if (cont>=1){
                if (nomLocal.equals(hashMap.get("local").toString())){
                    //sigue invi
                }else{
                    nomLocal=hashMap.get("local").toString();
                    unicos.add(cont);
                }
            }
            cont++;
        }
        return unicos;
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
        Holder holder=null;
        HashMap hashMap = reservaItems.get(position);
        if (convertView==null){
            convertView=inflater.inflate(R.layout.item_prenda_reserva,parent,false);
            holder = new Holder();

            holder.nombreTienda = (TextView) convertView.findViewById(R.id.reserva_tienda);
            holder.marcaPrenda = (TextView) convertView.findViewById(R.id.reserva_marca);
            holder.tipo = (TextView) convertView.findViewById(R.id.reserva_tipo);
            holder.precio = (TextView) convertView.findViewById(R.id.precio_parcial);
            holder.eliminarReserva = (ImageView) convertView.findViewById(R.id.reserva_eliminar);
            holder.layoutTotal = (RelativeLayout) convertView.findViewById(R.id.layout_total);
            holder.total = (TextView) convertView.findViewById(R.id.total_reserva);
            holder.contenedor = (RelativeLayout) convertView.findViewById(R.id.layout_item_prenda_reserva);
            holder.contenedorBeforeLocal = (RelativeLayout) convertView.findViewById(R.id.layout_prenda_local);

            if (position==getCount()-1){
                holder.layoutTotal.setVisibility(View.VISIBLE);
                Log.e("calculo","total");
                holder.total.setText("S/."+calcularTotal());
            }
            for (Integer integer:TiendasComunes()){
                if (position==integer){
                    holder.nombreTienda.setVisibility(View.VISIBLE);
                    holder.marcaPrenda.setVisibility(View.VISIBLE);
                }
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
                //holder.marcaPrenda.setTextSize(14);
                holder.tipo.setTextSize(14);
                holder.precio.setTextSize(14);
                holder.total.setVisibility(View.GONE);
            }

            holder.nombreTienda.setText(reservaItems.get(position).get("local").toString());
            //holder.marcaPrenda.setText(((Prenda) reservaItems.get(position).get("prenda")).getMarca());
            holder.marcaPrenda.setText(reservaItems.get(position).get("marca").toString());
            holder.tipo.setText(((Prenda) reservaItems.get(position).get("prenda")).getTipo());
            holder.precio.setText("S/."+((Prenda) reservaItems.get(position).get("prenda")).getPrecio());

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
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

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
