package pe.com.glup.adapters;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import pe.com.glup.R;
import pe.com.glup.beans.Prenda;
import pe.com.glup.datasource.DSProbador;
import pe.com.glup.glup.Glup;
import pe.com.glup.utils.Util_Fonts;

/**
 * Created by Glup on 6/10/15.
 */
public class PagerDetalleAdapter extends PagerAdapter {

    private Context context;
    private LayoutInflater layoutInflater;
    private ArrayList<Prenda> prendas;

    public PagerDetalleAdapter(Context context, ArrayList<Prenda> prendas) {
        this.context = context;
        this.layoutInflater = (LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.prendas = prendas;
    }

    @Override
    public int getCount() {return prendas.size();}
    public Object getItem(int position){ return prendas.get(position);}

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view== ((LinearLayout) object);
    }
    @Override
    public Object instantiateItem(final ViewGroup container, final int position) {
        View itemView=null;
        final Holder holder;
        final Prenda prenda = prendas.get(position);
        if (itemView==null){
            itemView = this.layoutInflater.inflate(R.layout.item_detalle,container,false);
            holder = new Holder();
            holder.marca = (TextView) itemView.findViewById(R.id.item_marca_prenda);
            holder.precio = (TextView) itemView.findViewById(R.id.precio_prenda);
            holder.contado = (TextView) itemView.findViewById(R.id.contador_corazon);
            holder.modelo = (TextView) itemView.findViewById(R.id.modelo_prenda);
            holder.imagen = (ImageView) itemView.findViewById(R.id.item_imagen_prenda);
            holder.check = (CheckBox) itemView.findViewById(R.id.check);
            holder.corazon = (ToggleButton) itemView.findViewById(R.id.corazon_prenda);

            holder.contado.setText(prenda.getNumGusta());
            boolean checked = prenda.getIndProbador().equals("1");
            holder.corazon.setChecked(checked);
        }else {
            holder = (Holder) itemView.getTag();
        }

        holder.marca.setText(prenda.getMarca());
        holder.modelo.setText(prenda.getTipo());


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

        holder.imagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("code:",prenda.getCod_prenda());
            }
        });


        Picasso.with(context).load(prenda.getImagen()).fit().placeholder(R.drawable.progress_animator).centerInside().noFade().into(holder.imagen);



        final String checkUpdated=prenda.getIndProbador();
        final Integer cont = Integer.parseInt(holder.contado.getText().toString());
        //final View finalConvertView = convertView;
        //
        final View finalConvertView = itemView;
        final Prenda finalprenda = prenda;
        final Holder finalHolder = holder;

        holder.corazon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("contador", String.valueOf(cont));
                int dis = cont - 1;
                int au = cont + 1;
                Log.e("disAu", String.valueOf(dis) + " " + String.valueOf(au));

                if (holder.corazon.isChecked()==true) { //los que eran falsos
                    prendas.get(position).setNumGusta(""+String.valueOf(au)+"");
                    prendas.get(position).setIndProbador("1");
                    holder.contado.setText(""+String.valueOf(au)+"");
                    holder.corazon.setChecked(true);
                    Log.e("corazon:", prenda.getCod_prenda());
                    Toast.makeText(context, "Se agrego al Probador", Toast.LENGTH_LONG).show();

                } else {
                    prendas.get(position).setNumGusta(""+String.valueOf(dis)+"");
                    prendas.get(position).setIndProbador("0");
                    Log.e("sincorazon:", prenda.getCod_prenda());
                    holder.contado.setText("" + String.valueOf(dis) + "");
                    holder.corazon.setChecked(false);
                    Toast.makeText(context, "Se elimino del Probador", Toast.LENGTH_LONG).show();
                }

                DSProbador dsProbador = new DSProbador(finalConvertView.getContext());
                dsProbador.setIndProbador(finalprenda.getCod_prenda());

            }
        });

        container.addView(itemView);
        return itemView;
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }

    public class Holder {
        public TextView marca;
        public TextView contado;
        public TextView modelo;
        public TextView precio;
        public ImageView imagen;
        public CheckBox check;
        public ToggleButton corazon;
    }
}