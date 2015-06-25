package pe.com.glup.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import pe.com.glup.R;
import pe.com.glup.beans.Prenda;
import pe.com.glup.holders.PrendaHolder;

/**
 * Created by Glup on 25/06/15.
 */
public class PrendaAdapter extends RecyclerView.Adapter<PrendaHolder> {

    private ArrayList<Prenda> prendas;

    public PrendaAdapter(ArrayList<Prenda> prendas) {
        this.prendas = prendas;
    }

    @Override
    public PrendaHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_catalogo, viewGroup, false);
        return new PrendaHolder(view);
    }

    @Override
    public void onBindViewHolder(PrendaHolder prendaHolder, int i) {

    }

    @Override
    public int getItemCount() {
        return prendas.size();
    }
}
