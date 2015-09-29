package pe.com.glup.adapters;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import pe.com.glup.R;
import pe.com.glup.beans.Prenda;
import pe.com.glup.beans.ReservaItem;
import pe.com.glup.beans.Tienda;

/**
 * Created by Glup on 28/09/15.
 */
public class ReservaAdapter extends RecyclerView.Adapter<ReservaAdapter.ReservaViewHolder>
                            implements View.OnClickListener{
    private int itemLayout;
    private ArrayList<ReservaItem> reservaItems;
    private Context context;
    public ReservaAdapter(Context context,int itemLayout,ArrayList<ReservaItem> reservaItems){
        this.context=context;
        this.itemLayout= itemLayout;
        this.reservaItems=reservaItems;
    }

    public ReservaAdapter(Context context, int itemLayout) {

        reservaItems = new ArrayList<ReservaItem>();
        for (int i=0;i<10;i++){
            ReservaItem reservaItem = new ReservaItem();
            Tienda tienda = new Tienda();
            tienda.setLocal("tienda " + i);
            Prenda prenda = new Prenda();
            prenda.setMarca("Marca " + i);
            prenda.setTipo("Tipo " + i);
            prenda.setPrecio("Precio " + i);
            reservaItem.setTienda(tienda);
            reservaItem.setPrenda(prenda);
            reservaItems.add(reservaItem);
            notifyDataSetChanged();
        }
        this.context=context;
        this.itemLayout= itemLayout;
    }

    @Override
    public ReservaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(itemLayout,parent,false);
        return new ReservaViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ReservaViewHolder holder, int position) {

        holder.eliminarReserva.setOnClickListener(this);
        holder.nombreTienda.setText(reservaItems.get(position).getTienda().getLocal());
        holder.marcaPrenda.setText(reservaItems.get(position).getPrenda().getMarca());
        holder.tipo.setText(reservaItems.get(position).getPrenda().getTipo());
        holder.precio.setText(reservaItems.get(position).getPrenda().getPrecio());

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
        public ReservaViewHolder(View itemView) {
            super(itemView);
            nombreTienda = (TextView) itemView.findViewById(R.id.reserva_tienda);
            marcaPrenda = (TextView) itemView.findViewById(R.id.reserva_marca);
            tipo = (TextView) itemView.findViewById(R.id.reserva_tipo);
            precio = (TextView) itemView.findViewById(R.id.precio_parcial);
            eliminarReserva = (ImageView) itemView.findViewById(R.id.reserva_eliminar);
        }
    }
}
