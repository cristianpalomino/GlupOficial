package pe.com.glup.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import pe.com.glup.R;
import pe.com.glup.models.Prenda;
import pe.com.glup.managers.bus.BusHolder;
import pe.com.glup.network.DSReserva;

/**
 * Created by Glup on 28/09/15.
 */
public class ReservaAdapter extends RecyclerView.Adapter<ReservaAdapter.ReservaViewHolder>
                            implements View.OnClickListener{
    private int itemLayout;
    private ArrayList<HashMap> reservaItems;
    private Context context;
    private float total=0;
    public ReservaAdapter(Context context,int itemLayout,ArrayList<HashMap> reservaItems){
        this.context=context;
        this.itemLayout= itemLayout;
        this.reservaItems=reservaItems;
        calcularTotal();
        BusHolder.getInstance().register(this);
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

    public ReservaAdapter(Context context, int itemLayout) {
        this.context=context;
        this.itemLayout= itemLayout;
    }

    @Override
    public ReservaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(itemLayout,parent,false);
        return new ReservaViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ReservaViewHolder holder, final int position) {

        holder.eliminarReserva.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DSReserva dsReserva = new DSReserva(context);
                dsReserva.eliminarDeReserva(((Prenda) reservaItems.get(position).get("prenda")).getCod_prenda());
                reservaItems.remove(position);
                notifyDataSetChanged();
            }
        });
        holder.nombreTienda.setText(reservaItems.get(position).get("local").toString());
        holder.marcaPrenda.setText(((Prenda) reservaItems.get(position).get("prenda")).getMarca());
        holder.tipo.setText(((Prenda) reservaItems.get(position).get("prenda")).getTipo());
        holder.precio.setText("S/."+((Prenda) reservaItems.get(position).get("prenda")).getPrecio());
        if (position==getItemCount()-1){
            holder.layoutTotal.setVisibility(View.VISIBLE);
            holder.total.setText("S/."+calcularTotal());
        }
        for (Integer integer:TiendasComunes()){
            if (position==integer){
                holder.nombreTienda.setVisibility(View.VISIBLE);
            }
        }

    }

    @Override
    public int getItemCount() {
        return reservaItems.size();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.reserva_eliminar:

                break;
        }
    }

    public static class ReservaViewHolder extends RecyclerView.ViewHolder {
        protected static TextView nombreTienda;
        protected static TextView marcaPrenda;
        protected static TextView tipo;
        protected static TextView precio;
        protected static ImageView eliminarReserva;
        protected static RelativeLayout layoutTotal;
        protected static TextView total;
        public ReservaViewHolder(View itemView) {
            super(itemView);
            nombreTienda = (TextView) itemView.findViewById(R.id.reserva_tienda);
            marcaPrenda = (TextView) itemView.findViewById(R.id.reserva_marca);
            tipo = (TextView) itemView.findViewById(R.id.reserva_tipo);
            precio = (TextView) itemView.findViewById(R.id.precio_parcial);
            eliminarReserva = (ImageView) itemView.findViewById(R.id.reserva_eliminar);
            layoutTotal = (RelativeLayout) itemView.findViewById(R.id.layout_total);
            total = (TextView) itemView.findViewById(R.id.total_reserva);
        }
    }
}
