package pe.com.glup.glup;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ToggleButton;

import com.squareup.otto.Subscribe;

import java.util.ArrayList;

import pe.com.glup.R;
import pe.com.glup.adapters.PagerDetalleAdapter;
import pe.com.glup.beans.Prenda;
import pe.com.glup.bus.BusHolder;
import pe.com.glup.dialog.DetailActivity;

/**
 * Created by Glup on 6/10/15.
 */
public class DetalleNew extends AppCompatActivity implements
        View.OnClickListener, ViewPager.OnPageChangeListener {

    private ArrayList<Prenda> prendas;
    private int current_position;

    private ViewPager pagerDetalle;
    private PagerDetalleAdapter pagerDetalleAdapter;
    private ImageView atras;
    private ToggleButton info;
    private Button btnInfo;

    @Override
    protected  void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        BusHolder.getInstance().register(this);
        setContentView(R.layout.fragment_detalle_new);
        pagerDetalle = (ViewPager) findViewById(R.id.scroll_detalle);
        atras = (ImageView) findViewById(R.id.cerrar_detalle);
        info = (ToggleButton) findViewById(R.id.info_detalle);
        btnInfo = (Button) findViewById(R.id.btn_info);

        current_position = getIntent().getIntExtra("current", 0);
        prendas = (ArrayList<Prenda>) getIntent().getSerializableExtra("prendas");

        atras.setOnClickListener(this);
        info.setOnClickListener(this);
        btnInfo.setOnClickListener(this);

        //pagerDetalle.setOnPageChangeListener(this);
        pagerDetalleAdapter = new PagerDetalleAdapter(
                this, this.prendas
        );

        pagerDetalle.setAdapter(pagerDetalleAdapter);
        pagerDetalle.setCurrentItem(current_position);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.cerrar_detalle){
            this.onBackPressed();
        } else if (v.getId() == R.id.info_detalle ||
                v.getId() == R.id.btn_info){
            info.setChecked(false);
            Log.e("codever:", prendas.get(pagerDetalle.getCurrentItem()).getCod_prenda());
            Intent intent = new Intent(this,DetailActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("codigoPrenda", prendas.get(pagerDetalle.getCurrentItem()).getCod_prenda());
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        this.current_position=position;
        Log.e(null,current_position+"");
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
    @Subscribe
    public void getIndProbador(String indProb) {
        try{
            Log.e("reloadPager", "contador");
            pagerDetalleAdapter.notifyDataSetChanged();//no trabaja

        }
        catch(NullPointerException e)
        {
            Log.e(null,"prendaAdapter en detallenew null-recarga de corazones fail");
        }
    }

    @Subscribe
    public void reloadItem(PagerDetalleAdapter.Holder holder){
        pagerDetalleAdapter.notifyDataSetChanged();//no trabaja
    }

}
