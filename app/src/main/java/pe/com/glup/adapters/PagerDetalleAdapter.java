package pe.com.glup.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import pe.com.glup.R;
import pe.com.glup.glup.Vista3d;
import pe.com.glup.models.Prenda;
import pe.com.glup.managers.bus.BusHolder;
import pe.com.glup.network.DSProbador;
import pe.com.glup.dialog.DetailActivity;
import pe.com.glup.utils.Util_Fonts;

/**
 * Created by Glup on 6/10/15.
 */
public class PagerDetalleAdapter extends PagerAdapter {

    private Context context;
    private LayoutInflater layoutInflater;
    private ArrayList<Prenda> prendas;
    static final int SHORT_DELAY = 1000;
    private Button btnInfo;
    private RelativeLayout frameBtnInfo;


    public PagerDetalleAdapter(Context context, ArrayList<Prenda> prendas) {
        this.context = context;
        this.layoutInflater = (LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.prendas = prendas;
        BusHolder.getInstance().register(this);
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

        }else {
           // holder = (Holder) itemView.getTag();
        }
        holder = new Holder();
        holder.marca = (TextView) itemView.findViewById(R.id.item_marca_prenda);
        holder.precio = (TextView) itemView.findViewById(R.id.precio_prenda);
        holder.contado = (TextView) itemView.findViewById(R.id.contador_corazon);
        holder.modelo = (TextView) itemView.findViewById(R.id.modelo_prenda);
        holder.imagen = (ImageView) itemView.findViewById(R.id.item_imagen_prenda);
        holder.check = (CheckBox) itemView.findViewById(R.id.check);
        holder.corazon = (ToggleButton) itemView.findViewById(R.id.corazon_prenda);

        holder.detalle = (Button) itemView.findViewById(R.id.btn_detalle);
        Spannable buttonLabel = new SpannableString("   Detalles");
        buttonLabel.setSpan(new ImageSpan(context, R.drawable.lentes_icon,
                ImageSpan.ALIGN_BASELINE), 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        holder.detalle.setText(buttonLabel);
        holder.detalle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("codever:", prenda.getCod_prenda());
                Intent intent = new Intent(context,DetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("codigoPrenda", prenda.getCod_prenda());
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });

        frameBtnInfo = (RelativeLayout) itemView.findViewById(R.id.frame_btn_info);
        btnInfo = (Button) itemView.findViewById(R.id.btn_info);
        Spannable buttonLabel2 = new SpannableString("   Detalles");
        ImageSpan imageSpan= new ImageSpan(context, R.drawable.lentes_icon,
                ImageSpan.ALIGN_BASELINE);

        buttonLabel2.setSpan(new ImageSpan(context, R.drawable.lentes_icon,
                ImageSpan.ALIGN_BASELINE), 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        btnInfo.setText(buttonLabel2);
        btnInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("codever:", prenda.getCod_prenda());
                Intent intent = new Intent(context,DetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("codigoPrenda", prenda.getCod_prenda());
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });

        BtnDetailVisible btnDetail;
        if (prenda.getInd_exhibicion()==1){
            btnDetail=new BtnDetailVisible(false);
            holder.precio.setVisibility(View.GONE);
            holder.detalle.setVisibility(View.GONE);//change de visible a gone
            btnInfo.setVisibility(View.GONE);
            frameBtnInfo.setVisibility(View.GONE);
        }else{
            btnDetail=new BtnDetailVisible(true);
            holder.precio.setVisibility(View.VISIBLE);
            holder.detalle.setVisibility(View.GONE);
            btnInfo.setVisibility(View.GONE);//change de visible a gone
            frameBtnInfo.setVisibility(View.GONE);//change de visible a gone
        }
        BusHolder.getInstance().post(btnDetail);

        holder.contado.setText(prenda.getNumGusta());
        boolean checked = prenda.getIndProbador().equals("1");
        holder.corazon.setChecked(checked);


        holder.marca.setText(prenda.getMarca());
        holder.modelo.setText(prenda.getTipo());


        //Log.e("fragmento",this.context.getApplicationContext().);

        if(prenda.getPrecio() == null){
            holder.precio.setText("");
        }else{
            float costo=Float.parseFloat(prenda.getPrecio());
            int parteEntera=(int)costo;
            float parteDecimal=costo-parteEntera;
            if (parteDecimal>0){
                holder.precio.setText("S/ " + prenda.getPrecio());
            }else{
                holder.precio.setText("S/ " + parteEntera);

            }
        }
        holder.marca.setTypeface(Util_Fonts.setLatoBold(context));
        holder.marca.setAllCaps(true);
        holder.modelo.setTypeface(Util_Fonts.setLatoLight(context));
        holder.precio.setTypeface(Util_Fonts.setLatoRegular(context));
        holder.contado.setTypeface(Util_Fonts.setLatoRegular(context));



        holder.imagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Log.e("code:",prenda.getCod_prenda());
                Intent intent = new Intent(context,DetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("codigoPrenda", prenda.getCod_prenda());
                intent.putExtras(bundle);
                context.startActivity(intent);*/
                Intent intent= new Intent(context, Vista3d.class);
                intent.putExtra("codigoPrenda",prendas.get(position).getCod_prenda());
                context.startActivity(intent);
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
        final int[] dis = {cont - 1};
        final int[] au = {cont + 1};
        holder.corazon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("contadorPagerDetalle", String.valueOf(cont));

                int action=0;//1 agregando 2 eliminando
                Log.e("disAu", String.valueOf(dis[0]) + " " + String.valueOf(au[0]));

                if (holder.corazon.isChecked()==true) { //los que eran falsos
                    dis[0] = au[0] -1;
                    prendas.get(position).setNumGusta("" + String.valueOf(au[0]) + "");
                    prendas.get(position).setIndProbador("1");
                    holder.contado.setText("" + String.valueOf(au[0]) + "");
                    holder.corazon.setChecked(true);
                    Log.e("corazonDetalle:", prenda.getCod_prenda());
                    //Toast.makeText(context, "Se agrego al Probador",SHORT_DELAY).show();
                    action=1;
                } else {
                    au[0] = dis[0] +1;
                    prendas.get(position).setNumGusta("" + String.valueOf(dis[0]) + "");
                    prendas.get(position).setIndProbador("0");
                    Log.e("sincorazonDetalle:", prenda.getCod_prenda());
                    holder.contado.setText("" + String.valueOf(dis[0]) + "");
                    holder.corazon.setChecked(false);
                    //Toast.makeText(context, "Se elimino del Probador", SHORT_DELAY).show();
                    action=2;
                }

                DSProbador dsProbador = new DSProbador(finalConvertView.getContext());
                dsProbador.setIndProbador(finalprenda.getCod_prenda(),action);
                BusHolder.getInstance().post(holder);

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
        public Button detalle;
    }
    public class BtnDetailVisible{
        public boolean visible;
        public BtnDetailVisible(boolean v){visible=v;}}
}
