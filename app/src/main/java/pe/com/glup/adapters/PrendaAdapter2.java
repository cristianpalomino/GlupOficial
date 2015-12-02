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
import android.widget.ToggleButton;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import pe.com.glup.R;
import pe.com.glup.models.Prenda;
import pe.com.glup.managers.bus.BusHolder;
import pe.com.glup.network.DSProbador;
import pe.com.glup.glup.DetalleNew;
import pe.com.glup.glup.Glup;
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
    static final int SHORT_DELAY = 1000;

    public PrendaAdapter2(Context context, ArrayList<Prenda> prendas) {
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        final Holder holder;
        final Prenda prenda = mPrendas.get(position);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {

            convertView = inflater.inflate(R.layout.item_catalogo, parent, false);

            //convertView.setTag(holder);
        } else {
          //  holder = (Holder) convertView.getTag();
        }
        Log.e("Despues", "codigoPrenda:" + prenda.getCod_prenda() + " ,indProbador:" + prenda.getIndProbador() + " ," +
                "numMeGusta:" + prenda.getNumGusta());

        holder = new Holder();
        Log.e("logrepcata","1");
        holder.marca = (TextView) convertView.findViewById(R.id.item_marca_prenda);
        holder.precio = (TextView) convertView.findViewById(R.id.precio_prenda);
        holder.contado = (TextView) convertView.findViewById(R.id.contador_corazon);
        holder.modelo = (TextView) convertView.findViewById(R.id.modelo_prenda);
        holder.imagen = (ImageView) convertView.findViewById(R.id.item_imagen_prenda);
        holder.check = (CheckBox) convertView.findViewById(R.id.check);
        holder.corazon = (ToggleButton) convertView.findViewById(R.id.corazon_prenda);



        holder.contado.setText(prenda.getNumGusta());
        //notifyDataSetChanged();
        boolean checked = prenda.getIndProbador().equals("1");
        holder.corazon.setChecked(checked);
        //notifyDataSetChanged();


        glup = (Glup) context;

        this.position=position;
        this.viewGroup=parent;
        this.codPrenda=prenda.getCod_prenda();
        this.tipo=prenda.getTipo();


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


        Picasso.with(context).load(prenda.getImagen()).fit().placeholder(R.drawable.progress_animator).centerInside().noFade().into(holder.imagen);

//        boolean checked = prenda.getIndProbador().equals("1");
//        holder.check.setChecked(checked);
        holder.imagen.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Log.e("click:", position + " codPrenda:" + codPrenda);
                ArrayList<Prenda> prendas = glup.getPrendas();
                Intent intent = new Intent(glup, DetalleNew.class);
                intent.putExtra("prendas", prendas);
                intent.putExtra("current", position);
                glup.startActivityForResult(intent,1);
                return false;
            }
        });
        //holder.imagen.setOnLongClickListener(this);


        final String checkUpdated=prenda.getIndProbador();
        final Integer cont = Integer.parseInt(holder.contado.getText().toString());
        //final View finalConvertView = convertView;
        //
        final View finalConvertView = convertView;
        final Prenda finalprenda = prenda;

        final Holder finalHolder = holder;
/*
        holder.corazon.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton toggleButton, boolean isChecked) {
                Log.e("checkChange", String.valueOf(isChecked));
                if (isChecked) {
                    holder.contado.setText(String.valueOf(cont - 1));
                    holder.corazon.setChecked(false);
                } else {
                    holder.contado.setText(String.valueOf(cont + 1));
                    holder.corazon.setChecked(true);
                }
                notifyDataSetChanged();


            }
        });*/
        holder.corazon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("contadorPrendaAdapter2", String.valueOf(cont));
                int dis = cont - 1;
                int au = cont + 1;
                Log.e("disAu", String.valueOf(dis) + " " + String.valueOf(au));
                int action=0;//1 agregando 2 eliminando
                if (holder.corazon.isChecked()==true) { //los que eran falsos
                    mPrendas.get(position).setNumGusta("" + String.valueOf(au) + "");
                    mPrendas.get(position).setIndProbador("1");
                    holder.contado.setText("" + String.valueOf(au) + "");
                    holder.corazon.setChecked(true);
                    Log.e("corazonPrendaAdap:", prenda.getCod_prenda());
                    //Toast.makeText(context, "Se agrego al Probador", SHORT_DELAY).show();
                    action=1;
                } else {
                    mPrendas.get(position).setNumGusta(""+String.valueOf(dis) + "");
                    mPrendas.get(position).setIndProbador("0");
                    Log.e("sincorazonPrendaAdap:", prenda.getCod_prenda());
                    holder.contado.setText("" + String.valueOf(dis) + "");
                    holder.corazon.setChecked(false);
                    //Toast.makeText(context, "Se elimino del Probador", SHORT_DELAY).show();
                    action=2;

                }

                DSProbador dsProbador = new DSProbador(finalConvertView.getContext());
                dsProbador.setIndProbador(finalprenda.getCod_prenda(),action);
                BusHolder.getInstance().post(holder);
            }
        });
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
        public ToggleButton corazon;
    }

    public ArrayList<Prenda> getmPrendas() {
        return mPrendas;
    }

    public void add(Prenda prenda) {
        mPrendas.add(prenda);
        notifyDataSetChanged();
    }
}