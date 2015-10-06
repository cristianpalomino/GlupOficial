package pe.com.glup.adapters;


import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import pe.com.glup.R;
import pe.com.glup.beans.Prenda;
import pe.com.glup.glup.Detalle;
import pe.com.glup.glup.Glup;
import pe.com.glup.glup.Principal;
import pe.com.glup.utils.Util_Fonts;
/**
 * Created by Glup on 18/09/15.
 */
public class PrendaAdapter2 extends BaseAdapter implements View.OnLongClickListener{

    private ArrayList<Prenda> mPrendas;
    private Context context;
    private LayoutInflater inflater;
    private ViewGroup viewGroup;
    private int position;
    private String codPrenda;
    private String tipo;
    private Glup glup;

    public PrendaAdapter2(Context context, ArrayList<Prenda> prendas) {
        this.context = context;
        this.mPrendas = prendas;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mPrendas.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object getItem(int position) {
        return mPrendas.get(position);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Holder holder = null;
        Prenda prenda = mPrendas.get(position);

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_catalogo, parent, false);
            holder = new Holder();

            holder.marca = (TextView) convertView.findViewById(R.id.item_marca_prenda);
            holder.precio = (TextView) convertView.findViewById(R.id.precio_prenda);
            holder.contado = (TextView) convertView.findViewById(R.id.contador_corazon);
            holder.modelo = (TextView) convertView.findViewById(R.id.modelo_prenda);
            holder.imagen = (ImageView) convertView.findViewById(R.id.item_imagen_prenda);
            holder.check = (CheckBox) convertView.findViewById(R.id.check);

            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        glup = (Glup) context;

        this.position=position;
        this.viewGroup=parent;
        this.codPrenda=prenda.getCod_prenda();
        this.tipo=prenda.getTipo();


        holder.marca.setText(prenda.getMarca());
        holder.modelo.setText(prenda.getTipo());
        holder.contado.setText(prenda.getNumGusta());

        //Log.e("fragmento",this.context.getApplicationContext().);
        if(prenda.getPrecio() == null){
            holder.precio.setText("");
        }else{
            holder.precio.setText("S/." + prenda.getPrecio());
        }

        holder.marca.setTypeface(Util_Fonts.setBold(context));
        holder.precio.setTypeface(Util_Fonts.setRegular(context));
        holder.modelo.setTypeface(Util_Fonts.setRegular(context));
        holder.contado.setTypeface(Util_Fonts.setRegular(context));


        Picasso.with(context).load(prenda.getImagen()).fit().placeholder(R.drawable.progress_animator).centerInside().noFade().into(holder.imagen);

//        boolean checked = prenda.getIndProbador().equals("1");
//        holder.check.setChecked(checked);
        holder.imagen.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Log.e("click:",position+" codPrenda:"+codPrenda);
                ArrayList<Prenda> prendas = glup.getPrendas();
                Intent intent = new Intent(glup, Detalle.class);
                intent.putExtra("prendas", prendas);
                intent.putExtra("current", position);
                glup.startActivity(intent);
                return false;
            }
        });
        //holder.imagen.setOnLongClickListener(this);


        return convertView;
    }

    @Override
    public boolean onLongClick(View v) {
        Log.e("Prenda:", "posicion " + position + " codigo:" +
                codPrenda+ " Tipo:" +
               tipo);
        return false;
    }

    public class Holder {
        public TextView marca;
        public TextView contado;
        public TextView modelo;
        public TextView precio;
        public ImageView imagen;
        public CheckBox check;
    }

    public ArrayList<Prenda> getmPrendas() {
        return mPrendas;
    }

    public void add(Prenda prenda) {
        mPrendas.add(prenda);
        notifyDataSetChanged();
    }
}