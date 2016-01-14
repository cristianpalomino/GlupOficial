package pe.com.glup.glup;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ToggleButton;

import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.zip.GZIPOutputStream;

import pe.com.glup.R;
import pe.com.glup.adapters.PagerDetalleAdapter;
import pe.com.glup.adapters.PrendaAdapter2;
import pe.com.glup.dialog.GlupDialogNew;
import pe.com.glup.managers.session.Session_Manager;
import pe.com.glup.models.Prenda;
import pe.com.glup.managers.bus.BusHolder;
import pe.com.glup.dialog.DetailActivity;
import pe.com.glup.models.interfaces.OnSuccessCatalogo;
import pe.com.glup.network.DSCatalogo;

/**
 * Created by Glup on 6/10/15.
 */
public class DetalleNew  extends Glup implements
        View.OnClickListener, ViewPager.OnPageChangeListener,OnSuccessCatalogo {

    private ArrayList<Prenda> prendas;
    private int current_position;

    private ViewPager pagerDetalle;
    private PagerDetalleAdapter pagerDetalleAdapter;
    private ImageView atras;
    private ToggleButton info;
    private RelativeLayout frameInfo;
    private int numPag;
    private static String TAG;
    private DSCatalogo dsCatalogo;
    private Glup glup;
    private Session_Manager session_manager;
    private GlupDialogNew gd;

    @Override
    protected  void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        BusHolder.getInstance().register(this);
        setContentView(R.layout.fragment_detalle_new);
        glup= this;
        session_manager= new Session_Manager(this);

        pagerDetalle = (ViewPager) findViewById(R.id.scroll_detalle);
        atras = (ImageView) findViewById(R.id.cerrar_detalle);
        info = (ToggleButton) findViewById(R.id.info_detalle);
        frameInfo = (RelativeLayout) findViewById(R.id.frame_info_detalle);


        TAG=getIntent().getStringExtra("buscar");
        Log.e("TAGdetalle",TAG);
        current_position = getIntent().getIntExtra("current", 0);
        prendas = (ArrayList<Prenda>) getIntent().getSerializableExtra("prendas");


        atras.setOnClickListener(this);
        info.setOnClickListener(this);
        frameInfo.setOnClickListener(this);

        //pagerDetalle.setOnPageChangeListener(this);
        pagerDetalleAdapter = new PagerDetalleAdapter(
                this, this.prendas
        );

        pagerDetalle.setAdapter(pagerDetalleAdapter);
        pagerDetalle.setCurrentItem(current_position);
        numPag=current_position/10+1;
        /*if (TAG.equals("genm") || TAG.equals("genM")){
            numPag=session_manager.getCurrentNumPagesMujer();
            Log.e("numPageMujer:",session_manager.getCurrentNumPagesMujer()+"");
        }else if (TAG.equals("genH")|| TAG.equals("genh")){
            numPag=session_manager.getCurrentNumPagesHombre();
        }else{
            Log.e("todos","cargo H y M");
            numPag=session_manager.getCurrentNumPages();
        }*/
        pagerDetalle.addOnPageChangeListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.cerrar_detalle){
            onBackPressed();
            //this.onBackPressed();
        } else if (v.getId()==R.id.frame_info_detalle || v.getId() == R.id.info_detalle ){
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
        Log.e("detalleCanPren",prendas.size()+"");
        Log.e("numPag",numPag+"");
        Log.e("CurrentPosition",current_position+"");
        int rangoInf=((10*numPag)-1);
        int rangoSup=0;
        if (TAG.equals("genm") || TAG.equals("genM")){
            rangoSup=session_manager.getTotalPrendasMujer();
        }else if (TAG.equals("genH")|| TAG.equals("genh")){
            rangoSup=session_manager.getTotalPrendasHombre();
        }else{
            Log.e("todos","cargo H y M");
            rangoSup=session_manager.getTotalPrendas();
        }
        Log.e("rangosup",rangoSup+" rangoinf:"+rangoInf);
        if (current_position==rangoInf && prendas.get(current_position).getFlagCarga()==false && current_position!=rangoSup-1){
            this.prendas.get(current_position).setFlagCarga(true);
            Log.e("elemento", "multiplo de 10");
            numPag++;
            dsCatalogo=new DSCatalogo(this);
            dsCatalogo.getGlobalPrendas(TAG, String.valueOf(numPag), "10");

            gd = new GlupDialogNew();
            gd.setCancelable(false);
            gd.show(getSupportFragmentManager(), GlupDialogNew.class.getSimpleName());
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
        gd.dismiss();
        Log.e("cargo", "10+");
        if(prendas != null) {
            if (!prendas.isEmpty()) {
                for (int i = 0; i < prendas.size(); i++) {
                    this.prendas.add(prendas.get(i));
                }
            }
        }
        session_manager.setFlagReload(false);
        if (TAG.equals("genm") || TAG.equals("genM")){
            session_manager.setNumPagesMujer(numPag);
        }else if (TAG.equals("genH")|| TAG.equals("genh")){
            session_manager.setNumPagesHombre(numPag);
        }else{
            Log.e("todos","cargo H y M");
            session_manager.setNumPages(numPag);
        }
        pagerDetalleAdapter.notifyDataSetChanged();
        PrendaAdapter2 prendaAdapter = new PrendaAdapter2(this,this.prendas);
        glup.setPrendas(prendaAdapter.getmPrendas());
        Log.e("tamaño",glup.getPrendas().size()+"");

        /*UploadPrendas uploadPrendas = new UploadPrendas();
        uploadPrendas.listPrendas=this.prendas;
        uploadPrendas.numberPag=this.numPag;
        uploadPrendas.flag=false;
        BusHolder.getInstance().post(uploadPrendas);
        Log.e("tamañO",uploadPrendas.listPrendas.size()+"");*/

    }

    @Override
    public void onFailed(String error_msg) {

    }

    public class UploadPrendas{public ArrayList<Prenda> listPrendas;public int numberPag;public boolean flag;}

    @Subscribe
    public void loaderPrendas(Principal.SignalUploadPrendas signalUploadPrendas){
        /*UploadPrendas uploadPrendas = new UploadPrendas();
        uploadPrendas.listPrendas=this.prendas;
        uploadPrendas.numberPag=this.numPag;
        uploadPrendas.flag=false;
        BusHolder.getInstance().post(uploadPrendas);
        Log.e("tamañO",uploadPrendas.listPrendas.size()+"");*/
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("listPrendas", prendas);
        intent.putExtra("numberPage", numPag);
        if (getParent() == null){
            setResult(RESULT_OK,intent);
        }else {
            getParent().setResult(RESULT_OK,intent);
        }
        this.finish();
    }



}
