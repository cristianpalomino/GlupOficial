package pe.com.glup.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import pe.com.glup.R;
import pe.com.glup.beans.Prenda;
import pe.com.glup.utils.Util_Fonts;

/**
 * Created by Glup on 25/06/15.
 */
public class PrendaAdapter extends RecyclerView.Adapter<PrendaAdapter.Holder> {

    private ArrayList<Prenda> prendas;
    private Context context;
    private OnItemClickListener onItemClickListener;

    public PrendaAdapter(ArrayList<Prenda> prendas, Context context) {
        this.prendas = prendas;
        this.context = context;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup viewGroup, final int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_catalogo, viewGroup, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(Holder holder, int i) {
        Prenda prenda = prendas.get(i);

        holder.marca.setText(prenda.getMarca());
        Picasso.with(context).load(prenda.getImagen()).fit().centerInside().placeholder(R.drawable.ic_panorama_white_48dp).into(holder.imagen);

        holder.marca.setTypeface(Util_Fonts.setRegular(context));
    }

    @Override
    public int getItemCount() {
        return prendas.size();
    }

    public void addPrenda(Prenda prenda) {
        prendas.add(prenda);
        notifyDataSetChanged();
    }

    class Holder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView marca;
        public ImageView imagen;

        public Holder(View v) {
            super(v);
            marca = (TextView) v.findViewById(R.id.item_marca_prenda);
            imagen = (ImageView) v.findViewById(R.id.item_imagen_prenda);
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(v,getPosition());
            }
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        public void onItemClick(View view , int position);
    }

    public ArrayList<Prenda> getPrendas() {
        return prendas;
    }
}
