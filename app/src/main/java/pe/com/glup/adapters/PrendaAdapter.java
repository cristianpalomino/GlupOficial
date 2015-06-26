package pe.com.glup.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import pe.com.glup.R;
import pe.com.glup.beans.Prenda;
import pe.com.glup.holders.PrendaHolder;
import pe.com.glup.utils.Util_Fonts;

/**
 * Created by Glup on 25/06/15.
 */
public class PrendaAdapter extends RecyclerView.Adapter<PrendaHolder> {

    private ArrayList<Prenda> prendas;
    private Context context;

    public PrendaAdapter(ArrayList<Prenda> prendas, Context context) {
        this.prendas = prendas;
        this.context = context;
    }

    @Override
    public PrendaHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_catalogo, viewGroup, false);
        return new PrendaHolder(view);
    }

    @Override
    public void onBindViewHolder(PrendaHolder holder, int i) {
        Prenda prenda = prendas.get(i);

        holder.marca.setText(prenda.getMarca());
        Picasso.with(context).load(prenda.getImagen()).fit().centerInside().placeholder(R.drawable.ic_panorama_white_48dp).into(holder.imagen);

        holder.marca.setTypeface(Util_Fonts.setRegular(context));
    }

    @Override
    public int getItemCount() {
        return prendas.size();
    }

    public void addPrenda(Prenda prenda){
        prendas.add(prenda);
        notifyDataSetChanged();
    }
}
