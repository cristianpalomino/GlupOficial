package pe.com.glup.adapters;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import pe.com.glup.R;
import pe.com.glup.models.Prenda;
import pe.com.glup.managers.bus.BusHolder;
import pe.com.glup.models.interfaces.OnClickTopProbador;
import pe.com.glup.utils.Util_Fonts;

/**
 * Created by Glup on 22/08/15.
 */
public class PagerTopAdapter extends PagerAdapter {

    private Context context;
    private LayoutInflater layoutInflater;
    private ArrayList<Prenda> prendas;
    OnClickTopProbador onClickTopProbador;

    public PagerTopAdapter() {
    }

    public PagerTopAdapter(Context context,ArrayList<Prenda> prendas) {
        this.context = context;
        this.prendas = prendas;
        BusHolder.getInstance().register(this);
    }

    @Override
    public int getCount() {
        return prendas.size();
    }

    public Object getItem(int position) {
        return prendas.get(position);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((LinearLayout) object);
    }

    @Override
    public Object instantiateItem(final ViewGroup container, int position) {
        this.layoutInflater = (LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = this.layoutInflater.inflate(R.layout.item_pager_top, container, false);
        Log.e("topAdapter",prendas.get(position).getIndProbador());
        final Prenda prenda = prendas.get(position);
        TextView marca = (TextView) itemView.findViewById(R.id.item_marca_prenda);
        ImageView imagen = (ImageView) itemView.findViewById(R.id.item_imagen_prenda);

        marca.setText(prenda.getMarca());
        marca.setTypeface(Util_Fonts.setRegular(context));
        Picasso.with(context).load(prenda.getImagen()).fit().centerInside().placeholder(R.drawable.progress_animator).noFade().into(imagen);

        imagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("pagerTop", prenda.getCod_prenda());
                /*try {
                    onClickTopProbador= (OnClickTopProbador) context.getApplicationContext();
                    onClickTopProbador.onClickTopProbador(prenda.getCod_prenda());
                }catch (ClassCastException c){
                    Log.e(null,c.toString());
                }*/
                String tag="BusTopAdapter";
                SuccessTopLongClick successLongClick = new SuccessTopLongClick();
                successLongClick.tag=tag;
                successLongClick.succcess=true;
                successLongClick.codigo_prenda=prenda.getCod_prenda();
                BusHolder.getInstance().post(successLongClick);

            }
        });
        container.addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }
    public class SuccessTopLongClick{
        public String tag;
        public boolean succcess;
        public String codigo_prenda;
    }
}
