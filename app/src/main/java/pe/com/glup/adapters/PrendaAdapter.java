package pe.com.glup.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.squareup.otto.Subscribe;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import pe.com.glup.R;
import pe.com.glup.beans.Prenda;
import pe.com.glup.bus.BusHolder;
import pe.com.glup.datasource.DSProbador;
import pe.com.glup.glup.Principal;
import pe.com.glup.utils.Util_Fonts;
import android.os.Looper;
import android.os.Handler;

/**
 * Created by Glup on 25/06/15.
 */
public class PrendaAdapter extends BaseAdapter  {

    private ArrayList<Prenda> mPrendas;
    private Context context;
    private LayoutInflater inflater;
    private String checkUpdated;
    private  Holder holder;

    private DSProbador dsProbador;

    public PrendaAdapter(Context context, ArrayList<Prenda> prendas) {
        this.context = context;
        this.mPrendas = prendas;
        this.inflater = LayoutInflater.from(context);
        BusHolder.getInstance().register(this);
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
    public View getView(int position, View convertView, ViewGroup parent) {
        holder = null;
        final Prenda prenda = mPrendas.get(position);
        Log.e("MenuLeft",prenda.getCod_prenda()+ " "+prenda.getIndProbador());
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_catalogo, parent, false);
            holder = new Holder();

            holder.marca = (TextView) convertView.findViewById(R.id.item_marca_prenda);
            holder.precio = (TextView) convertView.findViewById(R.id.precio_prenda);
            holder.contado = (TextView) convertView.findViewById(R.id.contador_corazon);
            holder.modelo = (TextView) convertView.findViewById(R.id.modelo_prenda);
            holder.imagen = (ImageView) convertView.findViewById(R.id.item_imagen_prenda);
            holder.check = (CheckBox) convertView.findViewById(R.id.check);
            holder.corazon = (ToggleButton) convertView.findViewById(R.id.corazon_prenda);

            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

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

        boolean checked = prenda.getIndProbador().equals("1");
        holder.corazon.setChecked(checked);

        checkUpdated=prenda.getIndProbador();
        final Integer cont = Integer.parseInt(holder.contado.getText().toString());
        //final View finalConvertView = convertView;
        //
        final View finalConvertView = convertView;
        final Prenda finalprenda = prenda;
        final boolean[] finalChecked = {checked};
        final Holder finalHolder = holder;

        holder.corazon.setOnCheckedChangeListener( new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton toggleButton, boolean isChecked) {
                Log.e("checkChange", String.valueOf(isChecked));
                if (isChecked) {
                    holder.contado.setText(String.valueOf(cont - 1));
                    holder.corazon.setChecked(false);
                }else {
                    holder.contado.setText(String.valueOf(cont + 1));
                    holder.corazon.setChecked(true);
                }
                notifyDataSetChanged();


            }
        }) ;
        holder.corazon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("contador",String.valueOf(cont));
                int dis = cont - 1;
                int au = cont + 1;
                Log.e("disAu",String.valueOf(dis)+" "+String.valueOf(au));

                if (checkUpdated.equals("1")){
                    //holder.contado.setText(String.valueOf(dis));
                    //holder.corazon.setChecked(false);
                }else{
                    //holder.contado.setText(String.valueOf(au));
                    //holder.corazon.setChecked(true);
                }
                dsProbador = new DSProbador(finalConvertView.getContext());
                dsProbador.setIndProbador(finalprenda.getCod_prenda());
            }
        });
        /*
        holder.imagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("click", "en IMAGEN");
                int dis = cont - 1;
                int au = cont + 1;
                Log.e("disAu", String.valueOf(dis) + " " + String.valueOf(au));

                // if (checkUpdated.equals("1")){
                //    holder.contado.setText(String.valueOf(dis));
                //    holder.corazon.setChecked(false);
                //}else{
                //    holder.contado.setText(String.valueOf(au));
                //    holder.corazon.setChecked(true);
                //}
                //notifyDataSetChanged();
                BusHolder.getInstance().post(prenda);
                dsProbador = new DSProbador(finalConvertView.getContext());
                dsProbador.setIndProbador(finalprenda.getCod_prenda());

            }
        });*/

        return convertView;
    }



    @Subscribe
    public void getIndProbador(String indProb) {
        checkUpdated=indProb;
        Log.e("enAdapter",indProb);
        BusHolder.getInstance().post(holder);
    }



    public class Holder {

        public TextView marca;
        public TextView contado;
        public ToggleButton corazon;
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