package pe.com.glup.holders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import pe.com.glup.R;

/**
 * Created by Glup on 25/06/15.
 */
public class PrendaHolder extends RecyclerView.ViewHolder {

    public TextView marca;
    public ImageView imagen;

    public PrendaHolder(View v) {
        super(v);

        marca = (TextView) v.findViewById(R.id.item_marca_prenda);
        imagen = (ImageView) v.findViewById(R.id.item_imagen_prenda);
    }
}
