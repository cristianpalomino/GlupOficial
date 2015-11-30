package pe.com.glup.glup;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ToggleButton;

import com.squareup.otto.Subscribe;

import java.util.ArrayList;

import pe.com.glup.R;
import pe.com.glup.adapters.PagerDetalleAdapter;
import pe.com.glup.models.Prenda;
import pe.com.glup.managers.bus.BusHolder;
import pe.com.glup.dialog.DetailActivity;
import pe.com.glup.models.interfaces.OnSuccessCatalogo;
import pe.com.glup.network.DSCatalogo;

/**
 * Created by Glup on 6/10/15.
 */
public class DetalleNew extends AppCompatActivity implements
        View.OnClickListener, ViewPager.OnPageChangeListener,OnSuccessCatalogo {

    private ArrayList<Prenda> prendas;
    private int current_position;

    private ViewPager pagerDetalle;
    private PagerDetalleAdapter pagerDetalleAdapter;
    private ImageView atras;
    private ToggleButton info;
    private RelativeLayout frameInfo;
    private Button btnInfo;
    private int numPag;
    private static String TAG = "todos";
    private DSCatalogo dsCatalogo;


    @Override
    protected  void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        BusHolder.getInstance().register(this);
        setContentView(R.layout.fragment_detalle_new);
        pagerDetalle = (ViewPager) findViewById(R.id.scroll_detalle);
        atras = (ImageView) findViewById(R.id.cerrar_detalle);
        info = (ToggleButton) findViewById(R.id.info_detalle);
        btnInfo = (Button) findViewById(R.id.btn_info);
        frameInfo = (RelativeLayout) findViewById(R.id.frame_info_detalle);

        current_position = getIntent().getIntExtra("current", 0);
        prendas = (ArrayList<Prenda>) getIntent().getSerializableExtra("prendas");

        atras.setOnClickListener(this);
        info.setOnClickListener(this);
        btnInfo.setOnClickListener(this);
        frameInfo.setOnClickListener(this);

        //pagerDetalle.setOnPageChangeListener(this);
        pagerDetalleAdapter = new PagerDetalleAdapter(
                this, this.prendas
        );

        pagerDetalle.setAdapter(pagerDetalleAdapter);
        pagerDetalle.setCurrentItem(current_position);
        numPag=current_position/10+1;
        pagerDetalle.addOnPageChangeListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.cerrar_detalle){
            this.onBackPressed();
        } else if (v.getId()==R.id.frame_info_detalle || v.getId() == R.id.info_detalle ||
                v.getId() == R.id.btn_info ){
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
        //Log.e("positionView",position+"");
    }

    @Override
    public void onPageSelected(int position) {
        this.current_position=position;
        Log.e("CurrentPosition",current_position+"");
        if (current_position%10==9 && prendas.get(current_position).getFlagCarga()==false){
            this.prendas.get(current_position).setFlagCarga(true);
            Log.e("elemento","multiplo de 10");
            numPag++;
            dsCatalogo=new DSCatalogo(this);
            dsCatalogo.getGlobalPrendas(TAG, String.valueOf(numPag), "10");
            try{
                dsCatalogo.setOnSuccessCatalogo(this);
            }catch (ClassCastException e){
                Log.e("errorClass",e.getMessage());
            }
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        //Log.e("estado",state+"");
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

    @Override
    public void onSuccess(String success_msg, ArrayList<Prenda> prendas) {
        Log.e("cargo", "10+");
        if (prendas != null) {
            if (!prendas.isEmpty()) {
                for (int i = 0; i < prendas.size(); i++) {
                    this.prendas.add(prendas.get(i));
                }
            }
        }
        pagerDetalleAdapter.notifyDataSetChanged();
        UploadPrendas uploadPrendas = new UploadPrendas();
        uploadPrendas.listPrendas=this.prendas;
        uploadPrendas.numberPag=this.numPag;
        uploadPrendas.flag=false;
        BusHolder.getInstance().post(uploadPrendas);
    }

    @Override
    public void onFailed(String error_msg) {

    }

    public class UploadPrendas{public ArrayList<Prenda> listPrendas;public int numberPag;public boolean flag;}
}
